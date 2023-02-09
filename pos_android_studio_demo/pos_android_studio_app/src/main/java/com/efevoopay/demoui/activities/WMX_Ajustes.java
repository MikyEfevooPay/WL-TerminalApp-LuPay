package com.efevoopay.demoui.activities;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.blumonpay.capx.functions.RSA;
import com.blumonpay.capx.model.RSAData;
import com.dspread.xpos.CQPOSService;
import com.dspread.xpos.QPOSService;
import com.efevoopay.demoui.BuildConfig;
import com.efevoopay.demoui.R;
import com.efevoopay.demoui.keyboard.KeyBoardNumInterface;
import com.efevoopay.demoui.keyboard.KeyboardUtil;
import com.efevoopay.demoui.keyboard.MyKeyboardView;
import com.efevoopay.demoui.utils.DUKPK2009_CBC;
import com.efevoopay.demoui.utils.TRACE;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

public class WMX_Ajustes extends BaseActivity implements View.OnClickListener{
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1001;
    private String blueTootchAddress = "";
    private String posId = "";
    private String TransportKey = "";
    private Button initialize;
    private String _rsa = "";
    private String _tk = "";
    private String _pk = "";
    private TextView txt_ksn,txt_version,txtmodelo;
    private Intent intent;
    private String ksn_posId;
    public String name="";
    SharedPreferences sharpref;
    Context eContext;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(getString(R.string.wmx_title_welcome));
        txt_ksn=(TextView)findViewById(R.id.txtksn);
        txt_version=(TextView)findViewById(R.id.txtversion);
        txtmodelo=(TextView)findViewById(R.id.txtmodelo);
        initialize = (Button) findViewById(R.id.WMX_btn_initialize_keys);
        initialize.setOnClickListener(this);
        txt_version.setText(BuildConfig.VERSION_NAME);
        txtmodelo.setText(Build.MODEL);
        intent = getIntent();
        ksn_posId = intent.getStringExtra("ksn_posId");
        txt_ksn.setText(ksn_posId);
        //initSDK();
        //initUart(QPOSService.CommunicationMode.UART);
        //pos.getQposId();
        sharpref=getPreferences(eContext.MODE_PRIVATE);
        String valor= sharpref.getString("tk","No hay dato");
        Toast.makeText(getApplicationContext(),"Dato guardado: "+valor,Toast.LENGTH_LONG).show();
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
                TRACE.d("posID: " + ksn_posId);
                TRACE.d("TransportKey: " + TransportKey);

                try {
                    RSA rsa = new RSA();

                    RSAData rsaD = new RSAData();
                    rsaD = rsa.generateKeys("3082010902820100CF57041EC2E7399C2BBD6CB0E8EDFC126B7837442541BCE86CC2804F9D90FE06EAE65B07014D789ED17300540D665213054E3E3A2A16D7FE1CFCC1382AF1485C542469D2AB327522444BF1A1EF1D8B79D9E9317B87D3531B364A8FCD24C0C6476E534D0D89070EEE2CBC999F00C5BEF3B935719AB459BBEE4EA86FEBEAC0F02A4F25D4007BA948E7B1E4A0456EB77107C4FCDAC79125EEE5A9D039995B6111F339DB1296A21D9F2048A8213BE29CE36DF0338D1BC04C3D42C0F6965E9694AFB05203D0BC05E6113AA6DA20DF0AB23DEA631144A8891352D866CBA9423B71890A4FD2B2112CE7BB57081581816232CD831932834EF05AA050C6FEBD434E9512ED0203010001");

                    _rsa=rsaD.getRsa();
                    _pk=rsaD.getPublicKey();
                    _tk=rsaD.getTk();

                    TRACE.d("rsaD.getRsa: " + _rsa);
                    TRACE.d("rsaD.getPublicKey: " + _pk);
                    TRACE.d("rsaD.getTk: " + _tk);

                    SharedPreferences.Editor editor=sharpref.edit();
                    editor.putString("tk",_tk);
                    editor.apply();
                }catch (Throwable t){
                    TRACE.d("error rsa: " + t);
                }


                call();

                break;
        }
    }

    private void call() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://wmx-iso-apps1.eba-iai89mzk.us-west-2.elasticbeanstalk.com/admin/tpv/registro";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("device_id", ksn_posId);
            jsonBody.put("device_tk", _tk);
            jsonBody.put("device_rsa", _rsa);

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

}
