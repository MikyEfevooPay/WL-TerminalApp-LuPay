package com.efevoopay.demoui.activities;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dspread.xpos.CQPOSService;
import com.dspread.xpos.QPOSService;
import com.efevoopay.demoui.R;
import com.efevoopay.demoui.keyboard.KeyBoardNumInterface;
import com.efevoopay.demoui.keyboard.KeyboardUtil;
import com.efevoopay.demoui.keyboard.MyKeyboardView;
import com.efevoopay.demoui.utils.DUKPK2009_CBC;
import com.efevoopay.demoui.utils.TRACE;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

public class WMX_Ajustes extends BaseActivity implements View.OnClickListener{
    private QPOSService pos;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1001;
    private String blueTootchAddress = "";
    private String posId = "";
    private String TransportKey = "";
    private Button initialize;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(getString(R.string.wmx_title_welcome));
        initialize = (Button) findViewById(R.id.WMX_btn_initialize_keys);
        initialize.setOnClickListener(this);
        initSDK();
    }

    @Override
    public void onToolbarLinstener() {
        onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_ajustes;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.WMX_btn_initialize_keys:
                TRACE.d("posID: " + posId);
                TRACE.d("TransportKey: " + TransportKey);
                call();
                break;
        }
    }

    private void call() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://wmx-iso-apps1.eba-iai89mzk.us-west-2.elasticbeanstalk.com/admin/tpv/registro";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("device_id", posId);
            jsonBody.put("device_tk", TransportKey);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    TRACE.d("** ResponseResult " +  TRACE.NEW_LINE + response.toString() );
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + error.toString() );
                    WMX_Ajustes.super.showAlert("ERROR", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {

            TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + e.toString() );

        }
    }

    private POS_TYPE posType = POS_TYPE.BLUETOOTH;
    private enum POS_TYPE {
        BLUETOOTH, AUDIO, UART, USB, OTG, BLUETOOTH_BLE
    }

    private void initSDK (){
        if(true){
            open(QPOSService.CommunicationMode.UART);
            posType = POS_TYPE.UART;
            blueTootchAddress = "/dev/ttyS1";
            pos.setDeviceAddress(blueTootchAddress);
            pos.openUart();
        }else{
            open(QPOSService.CommunicationMode.AUDIO);
            posType = POS_TYPE.AUDIO;
            pos.openAudio();
        }

        pos.getQposId();

        //pos.updateIPEKByTransportKey();

    }

    private void open(QPOSService.CommunicationMode mode){
        MyPosListener listener = new MyPosListener();
        pos = QPOSService.getInstance(mode);
        if(pos == null){
            //Error CommunicationMode unknow
            return;
        }
        if (mode == QPOSService.CommunicationMode.USB_OTG_CDC_ACM) {
            pos.setUsbSerialDriver(QPOSService.UsbOTGDriver.CDCACM);
        }
        pos.setD20Trade(true);
        pos.setConext(this);
        Handler handler = new Handler(Looper.myLooper());
        pos.initListener(handler, listener);

    }

    class MyPosListener extends CQPOSService {
        public void onRequestQposConnected() {
            TRACE.d("onRequestQposConnected()");
            if (ActivityCompat.checkSelfPermission(WMX_Ajustes.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                //申请权限
                ActivityCompat.requestPermissions(WMX_Ajustes.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
        @Override
        public void onRequestGenerateTransportKey(Hashtable result){
            TRACE.d("onRequestGenerateTransportKey(Hashtable<String, String> arg0):" + result.toString());
            TransportKey =  result.get("transportKey") == null ? "" : result.get("transportKey").toString() ;
            /*AQUI DEBE IR LO DE PROSA, TE LO TENGO QUE DEVOLVER*/
            String groupId = "00";
            String trackKsn = "00000093893403000001";
            String trackipek = "A492A46AE8B005ACD6082E499B6A3696";
            String trackipekKCV = "4836AE0000000000";
            String pinKsn = "00000093893403000001";
            String pinipek = "A492A46AE8B005ACD6082E499B6A3696";
            String pinipekKCV = "4836AE0000000000";
            String emvKsn = "00000093893403000001";
            String emvIPEK = "A492A46AE8B005ACD6082E499B6A3696";
            String emipekKCV = "4836AE0000000000";
            pos.updateIPEKByTransportKey(groupId, trackKsn, trackipek, trackipekKCV, emvKsn, emvIPEK, emipekKCV,
                    pinKsn, pinipek, pinipekKCV);
            pos.getQposInfo();
        }
        @Override
        public void onQposIdResult(Hashtable<String, String> posIdTable) {
//            TRACE.w("onQposIdResult():" + posIdTable.toString());
            posId = posIdTable.get("posId") == null ? "" : posIdTable.get("posId");
            pos.generateTransportKey(20);
        }
    }
}
