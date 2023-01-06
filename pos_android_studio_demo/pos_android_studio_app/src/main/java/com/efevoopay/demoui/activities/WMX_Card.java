package com.efevoopay.demoui.activities;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blumonpay.capx.model.RSAData;
import com.efevoopay.demoui.keyboard.KeyBoardNumInterface;
import com.efevoopay.demoui.keyboard.KeyboardUtil;
import com.efevoopay.demoui.keyboard.MyKeyboardView;
import com.efevoopay.demoui.utils.DUKPK2009_CBC;
import com.efevoopay.demoui.utils.TRACE;
import com.dspread.xpos.QPOSService;
import com.dspread.xpos.QPOSService.TransactionType;
import com.dspread.xpos.CQPOSService;

import com.efevoopay.demoui.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

import com.blumonpay.capx.functions.CypherFunctions;
import com.blumonpay.capx.functions.RSA;
import com.blumonpay.capx.model.DUKPTData;
import com.blumonpay.capx.model.TransactionData;






public class WMX_Card extends BaseActivity implements View.OnClickListener {
    private Button trading, pruebas;
    private TextView Total_Amount, Status_lector;
    private EditText Pruebaedittext;
    private String Amount, AmountToShow;
    private QPOSService pos;
    private String blueTootchAddress = "";
    private boolean isPinCanceled = false;
    private Context mContext;
    private Dialog dialog;
    private Dialog dialogPin;
    private Intent intent;

    private String FinalPin = "";
    private LinearLayout lin;

    private String FinalTradeType= "";

    private Hashtable<String, String> ICCTag ;

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1001;

    private GifImageView gif;
    private Button WMX_btn_trade;
    private TextView tv_card_label_1,tv_card_label_2;

    private String cardNofinal ="";
    private String type_transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        super.setInvisiblemargin(true);
        super.setWhiteLogo();
        super.setCustomToolbarColor("#002344ED");
        super.setMarginLogo();
        setTitle(getString(R.string.wmx_title_welcome));
        Intent intent = getIntent();
        Amount = intent.getStringExtra("Amount");
        AmountToShow = intent.getStringExtra("AmountToShow");
        type_transaction = intent.getStringExtra("type_transaction");

        Status_lector = (TextView) findViewById(R.id.wmx_status_lector);
        Total_Amount = (TextView) findViewById(R.id.wmx_text_total_Amount);
        Pruebaedittext = (EditText) findViewById(R.id.pruebaedittext);
        lin = findViewById(R.id.linFhater);
        Total_Amount.setText(AmountToShow);
        trading = (Button) findViewById(R.id.WMX_btn_trade);
        trading.setOnClickListener(this);

        pruebas = (Button) findViewById(R.id.WMX_btn_pruebas_tmp);
        pruebas.setOnClickListener(this);

        mContext = this;

        gif = findViewById(R.id.giv_card_status);
        tv_card_label_1 = findViewById(R.id.tv_card_label_1);
        tv_card_label_2 = findViewById(R.id.tv_card_label_2);
        initSDK();

        /** open(QPOSService.CommunicationMode.UART);
        posType = POS_TYPE.UART;
        blueTootchAddress = "/dev/ttyS1";
        pos.setDeviceAddress(blueTootchAddress);
        pos.openUart();**/

       /* Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ChangeViewToTicket();
            }
        }, 2000);*/
    }

    public String formatMoney(String amount){
        String str="";
        str = "$" + amount +" MXN";
        return str;
    }

    @Override
    public void onToolbarLinstener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_card;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.WMX_btn_trade:
                pos.cancelSetAmount();
                pos.cancelTrade();
                onBackPressed();
            break;
            case R.id.WMX_btn_pruebas_tmp:
                pos.cancelSetAmount();
                pos.cancelTrade();
//                pos.getKsn();

                break;

        }
    }

    private void DoTrade(){
        Status_lector.setText("");
        if (pos == null) {
            Status_lector.setText(R.string.scan_bt_pos_error);
            return;
        }
        isPinCanceled = false;
        Status_lector.setText(R.string.starting);
        if (posType == POS_TYPE.UART) {
            pos.setCardTradeMode(QPOSService.CardTradeMode.SWIPE_TAP_INSERT_CARD_NOTUP);
            pos.doTrade(20);
        }
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
//        pos.generateTransportKey(20);
        DoTrade();

    }

    private POS_TYPE posType = POS_TYPE.BLUETOOTH;

    private enum POS_TYPE {
        BLUETOOTH, AUDIO, UART, USB, OTG, BLUETOOTH_BLE
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
//        pos.getQposId();

    }

    private void setPin(String x, TextView tv){

        if(x != "del" && FinalPin.length()<4){
            FinalPin = FinalPin + x;
        }else if(x == "del" && FinalPin.length()>0){
            FinalPin = FinalPin.substring(0,FinalPin.length()-1);
        }

        if(FinalPin.length()<=4){
            if(FinalPin.length()==0)tv.setText("○ ○ ○ ○");
            else if(FinalPin.length()==1)tv.setText("● ○ ○ ○");
            else if(FinalPin.length()==2)tv.setText("● ● ○ ○");
            else if(FinalPin.length()==3)tv.setText("● ● ● ○");
            else if(FinalPin.length()==4)tv.setText("● ● ● ●");
        }

        if(FinalPin.length()==4) {
            pos.pinMapSync(FinalPin,20);
            dialog.dismiss();
        }

    }

    private void call(String content) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://wmx-iso-apps1.eba-iai89mzk.us-west-2.elasticbeanstalk.com/matriz/certificacion/transaccion";
            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("Method", FinalTradeType);
//            jsonBody.put("Response", content);
//            jsonBody.put("Amount", Amount);
            jsonBody.put("msn", content);
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
                    WMX_Card.super.showAlert("ERROR", error.toString());
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

    private KeyboardUtil keyboardUtil;

    private List<String> keyBoardList = new ArrayList<>();

    private void ChangeViewtoProccess (){
        gif.setImageResource(R.drawable.puntos_anim);
        tv_card_label_1.setText("Procesando Transacción");
        tv_card_label_2.setText("");
        trading.setVisibility(View.GONE);
    }

    private void ChangeViewToTicket (){
        Intent thisIntent = getIntent();
        String v_total = thisIntent.getStringExtra("total");

        intent = new Intent(this, WMX_final_ticket_transaction.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TRACE.d("date   "+getTime());

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
        Date date = new Date();

        intent.putExtra("type_transaction",type_transaction);
        intent.putExtra("v_total",Total_Amount.getText().toString());
        intent.putExtra("v_time",dateFormat.format(date).toString());
        intent.putExtra("v_card","**** 9999");

        if(type_transaction.equals("msi")){
            String v_months = thisIntent.getStringExtra("months");
            String v_months_total = thisIntent.getStringExtra("months_total");
            intent.putExtra("v_months",v_months.toString());
            intent.putExtra("v_months_total",v_months_total.toString());

        }else{
            String v_subtotal = thisIntent.getStringExtra("subtotal");
            String v_tip = thisIntent.getStringExtra("tips");

            intent.putExtra("v_tip",v_tip.toString());
            intent.putExtra("v_subtotal",v_subtotal.toString());
        }

       startActivity(intent);
    }

    private String getTime(){
        //YYMMDDHHmmss
        //DateTimeFormat dtf = DateTimeFormatter.SimpleDateFormat("YY/MM/DD HH:mm:ss");
        //LocalTime localDate = LocalTime.now();

        DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();


        return dateFormat.format(date).toString();
    }

    /** CLASS **/

    class MyPosListener extends CQPOSService {
        public void onRequestQposConnected() {
            TRACE.d("onRequestQposConnected()");
            /*AQUI DEBE IR LO DE PROSA, TE LO TENGO QUE DEVOLVER*/
            String groupId = "00";
            String trackKsn = "00000081958255400001";
            String trackipek = "10830111867971FC5EADB46E085C3043";
            String trackipekKCV = "B34512";
            String pinKsn = "00000081958255400001";
            String pinipek = "10830111867971FC5EADB46E085C3043";
            String pinipekKCV = "B34512";
            String emvKsn = "00000081958255400001";
            String emvIPEK = "10830111867971FC5EADB46E085C3043";
            String emipekKCV = "B34512";
//            pos.updateIPEKByTransportKey(groupId, trackKsn, trackipek, trackipekKCV, emvKsn, emvIPEK, emipekKCV,
//                    pinKsn, pinipek, pinipekKCV);
            TRACE.d("INYECCION()");

//            Toast.makeText(mContext, "onRequestQposConnected", Toast.LENGTH_LONG).show();
            //dismissDialog();
            //statusEditText.setText(getString(R.string.device_plugged));
            trading.setEnabled(true);
            //btnDisconnect.setEnabled(true);
            if (ActivityCompat.checkSelfPermission(WMX_Card.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                //申请权限
                ActivityCompat.requestPermissions(WMX_Card.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }

        public void onRequestSetAmount() {
            TRACE.d("enter amount -- start");
            TRACE.d("onRequestSetAmount()");
            Toast.makeText(mContext, "onRequestSetAmount", Toast.LENGTH_LONG).show();

            String amount = Amount;
            int cents = (int) Math.round(100*Float.parseFloat(amount));

            pos.setAmount(String.valueOf(cents), "0", "484", TransactionType.PAYMENT);

            TRACE.d("enter amount  -- end");

            /**dismissDialog();
             dialog = new Dialog(mContext);
             dialog.setContentView(R.layout.amount_dialog);
             dialog.setTitle(getString(R.string.set_amount));

             String[] transactionTypes = new String[]{"GOODS", "SERVICES", "CASH", "CASHBACK", "INQUIRY",
             "TRANSFER", "ADMIN", "CASHDEPOSIT",
             "PAYMENT", "PBOCLOG||ECQ_INQUIRE_LOG", "SALE",
             "PREAUTH", "ECQ_DESIGNATED_LOAD", "ECQ_UNDESIGNATED_LOAD",
             "ECQ_CASH_LOAD", "ECQ_CASH_LOAD_VOID", "CHANGE_PIN", "REFOUND", "SALES_NEW"};
             ((Spinner) dialog.findViewById(R.id.transactionTypeSpinner)).setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item,
             transactionTypes));

             dialog.findViewById(R.id.setButton).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

            String amount = ((EditText) (dialog.findViewById(R.id.amountEditText))).getText().toString();
            String cashbackAmount = ((EditText) (dialog.findViewById(R.id.cashbackAmountEditText))).getText().toString();
            String transactionTypeString = (String) ((Spinner) dialog.findViewById(R.id.transactionTypeSpinner)).getSelectedItem();

            TransactionType transactionType = null;
            if (transactionTypeString.equals("GOODS")) {
            transactionType = QPOSService.TransactionType.GOODS;
            } else if (transactionTypeString.equals("SERVICES")) {
            transactionType = QPOSService.TransactionType.SERVICES;
            } else if (transactionTypeString.equals("CASH")) {
            transactionType = QPOSService.TransactionType.CASH;
            } else if (transactionTypeString.equals("CASHBACK")) {
            transactionType = QPOSService.TransactionType.CASHBACK;
            } else if (transactionTypeString.equals("INQUIRY")) {
            transactionType = QPOSService.TransactionType.INQUIRY;
            } else if (transactionTypeString.equals("TRANSFER")) {
            transactionType = QPOSService.TransactionType.TRANSFER;
            } else if (transactionTypeString.equals("ADMIN")) {
            transactionType = QPOSService.TransactionType.ADMIN;
            } else if (transactionTypeString.equals("CASHDEPOSIT")) {
            transactionType = QPOSService.TransactionType.CASHDEPOSIT;
            } else if (transactionTypeString.equals("PAYMENT")) {
            transactionType = QPOSService.TransactionType.PAYMENT;
            } else if (transactionTypeString.equals("PBOCLOG||ECQ_INQUIRE_LOG")) {
            transactionType = QPOSService.TransactionType.PBOCLOG;
            } else if (transactionTypeString.equals("SALE")) {
            transactionType = QPOSService.TransactionType.SALE;
            } else if (transactionTypeString.equals("PREAUTH")) {
            transactionType = QPOSService.TransactionType.PREAUTH;
            } else if (transactionTypeString.equals("ECQ_DESIGNATED_LOAD")) {
            transactionType = QPOSService.TransactionType.ECQ_DESIGNATED_LOAD;
            } else if (transactionTypeString.equals("ECQ_UNDESIGNATED_LOAD")) {
            transactionType = QPOSService.TransactionType.ECQ_UNDESIGNATED_LOAD;
            } else if (transactionTypeString.equals("ECQ_CASH_LOAD")) {
            transactionType = QPOSService.TransactionType.ECQ_CASH_LOAD;
            } else if (transactionTypeString.equals("ECQ_CASH_LOAD_VOID")) {
            transactionType = QPOSService.TransactionType.ECQ_CASH_LOAD_VOID;
            } else if (transactionTypeString.equals("CHANGE_PIN")) {
            transactionType = QPOSService.TransactionType.UPDATE_PIN;
            } else if (transactionTypeString.equals("REFOUND")) {
            transactionType = QPOSService.TransactionType.REFUND;
            } else if (transactionTypeString.equals("SALES_NEW")) {
            transactionType = QPOSService.TransactionType.SALES_NEW;
            }


            OtherActivity.this.amount = amount;
            OtherActivity.this.cashbackAmount = cashbackAmount;

            pos.setAmount(amount, cashbackAmount, "156", transactionType);

            TRACE.d("enter amount  -- end");
            dismissDialog();
            }

            });

             dialog.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            pos.cancelSetAmount();
            dialog.dismiss();
            }

            });
             dialog.setCanceledOnTouchOutside(false);
             dialog.show();
             //            pos.setAmount("200", cashbackAmount, "156", QPOSService.TransactionType.GOODS);
             **/
        }

        public void onRequestWaitingUser() {//wait for card
            TRACE.d("onRequestWaitingUser()");
            Status_lector.setText(getString(R.string.waiting_for_card));
        }

        @Override
        public void onDoTradeResult(QPOSService.DoTradeResult result, Hashtable<String, String> decodeData) {
            TRACE.d("(DoTradeResult result, Hashtable<String, String> decodeData) " + result.toString() + TRACE.NEW_LINE + "decodeData:" + decodeData);
            FinalTradeType=result.toString();
            TRACE.d("FinalTradeType" + FinalTradeType + TRACE.NEW_LINE);

            if (result == QPOSService.DoTradeResult.NONE) {
                Status_lector.setText(getString(R.string.no_card_detected));
            }
            else if(result == QPOSService.DoTradeResult.TRY_ANOTHER_INTERFACE) {
                Status_lector.setText(getString(R.string.try_another_interface));
            }
            else if (result == QPOSService.DoTradeResult.ICC) {
                Status_lector.setText(getString(R.string.icc_card_inserted));
                TRACE.d("EMV ICC Start");
//
//

//                try{
//                    ICCTag=pos.getICCTag(QPOSService.EncryptType.PLAINTEXT,1,1,"57");
//                    TRACE.d("ICCTag" + ICCTag + TRACE.NEW_LINE);
//                }catch (Throwable t){
//
//                }
                pos.doEmvApp(QPOSService.EmvOption.START);
            }
            else if (result == QPOSService.DoTradeResult.NOT_ICC) {
                Status_lector.setText(getString(R.string.card_inserted));
            }
            else if (result == QPOSService.DoTradeResult.BAD_SWIPE) {
                Status_lector.setText(getString(R.string.bad_swipe));
            }
            else if (result == QPOSService.DoTradeResult.MCR) {//Magnetic card
                String content = getString(R.string.card_swiped);
                String formatID = decodeData.get("formatID");
                if (formatID.equals("31") || formatID.equals("40") || formatID.equals("37") || formatID.equals("17") || formatID.equals("11") || formatID.equals("10")) {
                    String maskedPAN = decodeData.get("maskedPAN");
                    String expiryDate = decodeData.get("expiryDate");
                    String cardHolderName = decodeData.get("cardholderName");
                    String serviceCode = decodeData.get("serviceCode");
                    String trackblock = decodeData.get("trackblock");
                    String psamId = decodeData.get("psamId");
                    String posId = decodeData.get("posId");
                    String pinblock = decodeData.get("pinblock");
                    String macblock = decodeData.get("macblock");
                    String activateCode = decodeData.get("activateCode");
                    String trackRandomNumber = decodeData.get("trackRandomNumber");

                    content += getString(R.string.format_id) + " " + formatID + "\n";
                    content += getString(R.string.masked_pan) + " " + maskedPAN + "\n";
                    content += getString(R.string.expiry_date) + " " + expiryDate + "\n";
                    content += getString(R.string.cardholder_name) + " " + cardHolderName + "\n";

                    content += getString(R.string.service_code) + " " + serviceCode + "\n";
                    content += "trackblock: " + trackblock + "\n";
                    content += "psamId: " + psamId + "\n";
                    content += "posId: " + posId + "\n";
                    content += getString(R.string.pinBlock) + " " + pinblock + "\n";
                    content += "macblock: " + macblock + "\n";
                    content += "activateCode: " + activateCode + "\n";
                    content += "trackRandomNumber: " + trackRandomNumber + "\n";
                }
                else if (formatID.equals("FF")) {
                    String type = decodeData.get("type");
                    String encTrack1 = decodeData.get("encTrack1");
                    String encTrack2 = decodeData.get("encTrack2");
                    String encTrack3 = decodeData.get("encTrack3");
                    content += "cardType:" + " " + type + "\n";
                    content += "track_1:" + " " + encTrack1 + "\n";
                    content += "track_2:" + " " + encTrack2 + "\n";
                    content += "track_3:" + " " + encTrack3 + "\n";
                }
                else {
                    String orderID = decodeData.get("orderId");
                    String maskedPAN = decodeData.get("maskedPAN");
                    String expiryDate = decodeData.get("expiryDate");
                    String cardHolderName = decodeData.get("cardholderName");
//					String ksn = decodeData.get("ksn");
                    String serviceCode = decodeData.get("serviceCode");
                    String track1Length = decodeData.get("track1Length");
                    String track2Length = decodeData.get("track2Length");
                    String track3Length = decodeData.get("track3Length");
                    String encTracks = decodeData.get("encTracks");
                    String encTrack1 = decodeData.get("encTrack1");
                    String encTrack2 = decodeData.get("encTrack2");
                    String encTrack3 = decodeData.get("encTrack3");
                    String partialTrack = decodeData.get("partialTrack");
                    String pinKsn = decodeData.get("pinKsn");
                    String trackksn = decodeData.get("trackksn");
                    String pinBlock = decodeData.get("pinBlock");
                    String encPAN = decodeData.get("encPAN");
                    String trackRandomNumber = decodeData.get("trackRandomNumber");
                    String pinRandomNumber = decodeData.get("pinRandomNumber");
                    if (orderID != null && !"".equals(orderID)) {
                        content += "orderID:" + orderID;
                    }
                    content += getString(R.string.format_id) + " " + formatID + "\n";
                    content += getString(R.string.masked_pan) + " " + maskedPAN + "\n";
                    content += getString(R.string.expiry_date) + " " + expiryDate + "\n";
                    content += getString(R.string.cardholder_name) + " " + cardHolderName + "\n";
//					content += getString(R.string.ksn) + " " + ksn + "\n";
                    content += getString(R.string.pinKsn) + " " + pinKsn + "\n";
                    content += getString(R.string.trackksn) + " " + trackksn + "\n";
                    content += getString(R.string.service_code) + " " + serviceCode + "\n";
                    content += getString(R.string.track_1_length) + " " + track1Length + "\n";
                    content += getString(R.string.track_2_length) + " " + track2Length + "\n";
                    content += getString(R.string.track_3_length) + " " + track3Length + "\n";
                    content += getString(R.string.encrypted_tracks) + " " + encTracks + "\n";
                    content += getString(R.string.encrypted_track_1) + " " + encTrack1 + "\n";
                    content += getString(R.string.encrypted_track_2) + " " + encTrack2 + "\n";
                    content += getString(R.string.encrypted_track_3) + " " + encTrack3 + "\n";
                    content += getString(R.string.partial_track) + " " + partialTrack + "\n";
                    content += getString(R.string.pinBlock) + " " + pinBlock + "\n";
                    content += "encPAN: " + encPAN + "\n";
                    content += "trackRandomNumber: " + trackRandomNumber + "\n";
                    content += "pinRandomNumber:" + " " + pinRandomNumber + "\n";
                    String realPan = null;
                    if (!TextUtils.isEmpty(trackksn) && !TextUtils.isEmpty(encTrack2)) {
                        String clearPan = DUKPK2009_CBC.getDate(trackksn, encTrack2, DUKPK2009_CBC.Enum_key.DATA, DUKPK2009_CBC.Enum_mode.CBC);
                        content += "encTrack2:" + " " + clearPan + "\n";
                        realPan = clearPan.substring(0, maskedPAN.length());
                        content += "realPan:" + " " + realPan + "\n";
                    }
                    if (!TextUtils.isEmpty(pinKsn) && !TextUtils.isEmpty(pinBlock) && !TextUtils.isEmpty(realPan)) {
                        String date = DUKPK2009_CBC.getDate(pinKsn, pinBlock, DUKPK2009_CBC.Enum_key.PIN, DUKPK2009_CBC.Enum_mode.CBC);
                        String parsCarN = "0000" + realPan.substring(realPan.length() - 13, realPan.length() - 1);
                        String s = DUKPK2009_CBC.xor(parsCarN, date);
                        content += "PIN:" + " " + s + "\n";
                    }
                }
                call(content);
                Status_lector.setText(content);
//                autoDoTrade(0);

            }
            else if ((result == QPOSService.DoTradeResult.NFC_ONLINE) || (result == QPOSService.DoTradeResult.NFC_OFFLINE)) {
                //nfcLog = decodeData.get("nfcLog");
                String content = getString(R.string.tap_card);
                String formatID = decodeData.get("formatID");
                if (formatID.equals("31") || formatID.equals("40")
                        || formatID.equals("37") || formatID.equals("17")
                        || formatID.equals("11") || formatID.equals("10")) {
                    String maskedPAN = decodeData.get("maskedPAN");
                    String expiryDate = decodeData.get("expiryDate");
                    String cardHolderName = decodeData.get("cardholderName");
                    String serviceCode = decodeData.get("serviceCode");
                    String trackblock = decodeData.get("trackblock");
                    String psamId = decodeData.get("psamId");
                    String posId = decodeData.get("posId");
                    String pinblock = decodeData.get("pinblock");
                    String macblock = decodeData.get("macblock");
                    String activateCode = decodeData.get("activateCode");
                    String trackRandomNumber = decodeData
                            .get("trackRandomNumber");

                    content += getString(R.string.format_id) + " " + formatID
                            + "\n";
                    content += getString(R.string.masked_pan) + " " + maskedPAN
                            + "\n";
                    content += getString(R.string.expiry_date) + " "
                            + expiryDate + "\n";
                    content += getString(R.string.cardholder_name) + " "
                            + cardHolderName + "\n";

                    content += getString(R.string.service_code) + " "
                            + serviceCode + "\n";
                    content += "trackblock: " + trackblock + "\n";
                    content += "psamId: " + psamId + "\n";
                    content += "posId: " + posId + "\n";
                    content += getString(R.string.pinBlock) + " " + pinblock
                            + "\n";
                    content += "macblock: " + macblock + "\n";
                    content += "activateCode: " + activateCode + "\n";
                    content += "trackRandomNumber: " + trackRandomNumber + "\n";
                }
                else {

                    String maskedPAN = decodeData.get("maskedPAN");
                    String expiryDate = decodeData.get("expiryDate");
                    String cardHolderName = decodeData.get("cardholderName");
//					String ksn = decodeData.get("ksn");
                    String serviceCode = decodeData.get("serviceCode");
                    String track1Length = decodeData.get("track1Length");
                    String track2Length = decodeData.get("track2Length");
                    String track3Length = decodeData.get("track3Length");
                    String encTracks = decodeData.get("encTracks");
                    String encTrack1 = decodeData.get("encTrack1");
                    String encTrack2 = decodeData.get("encTrack2");
                    String encTrack3 = decodeData.get("encTrack3");
                    String partialTrack = decodeData.get("partialTrack");
                    String pinKsn = decodeData.get("pinKsn");
                    String trackksn = decodeData.get("trackksn");
                    String pinBlock = decodeData.get("pinBlock");
                    String encPAN = decodeData.get("encPAN");
                    String trackRandomNumber = decodeData
                            .get("trackRandomNumber");
                    String pinRandomNumber = decodeData.get("pinRandomNumber");

                    content += getString(R.string.format_id) + " " + formatID
                            + "\n";
                    content += getString(R.string.masked_pan) + " " + maskedPAN
                            + "\n";
                    content += getString(R.string.expiry_date) + " "
                            + expiryDate + "\n";
                    content += getString(R.string.cardholder_name) + " "
                            + cardHolderName + "\n";
//					content += getString(R.string.ksn) + " " + ksn + "\n";
                    content += getString(R.string.pinKsn) + " " + pinKsn + "\n";
                    content += getString(R.string.trackksn) + " " + trackksn
                            + "\n";
                    content += getString(R.string.service_code) + " "
                            + serviceCode + "\n";
                    content += getString(R.string.track_1_length) + " "
                            + track1Length + "\n";
                    content += getString(R.string.track_2_length) + " "
                            + track2Length + "\n";
                    content += getString(R.string.track_3_length) + " "
                            + track3Length + "\n";
                    content += getString(R.string.encrypted_tracks) + " "
                            + encTracks + "\n";
                    content += getString(R.string.encrypted_track_1) + " "
                            + encTrack1 + "\n";
                    content += getString(R.string.encrypted_track_2) + " "
                            + encTrack2 + "\n";
                    content += getString(R.string.encrypted_track_3) + " "
                            + encTrack3 + "\n";
                    content += getString(R.string.partial_track) + " "
                            + partialTrack + "\n";
                    content += getString(R.string.pinBlock) + " " + pinBlock
                            + "\n";
                    content += "encPAN: " + encPAN + "\n";
                    content += "trackRandomNumber: " + trackRandomNumber + "\n";
                    content += "pinRandomNumber:" + " " + pinRandomNumber
                            + "\n";
                }

                TRACE.d(TRACE.NEW_LINE + "content in NNFC request(?)" +content);
                call(content);

                //sendMsg(8003);
            }
            else if ((result == QPOSService.DoTradeResult.NFC_DECLINED)) {
                TRACE.d(TRACE.NEW_LINE + getString(R.string.transaction_declined));

                //statusEditText.setText(getString(R.string.transaction_declined));
            }
            else if (result == QPOSService.DoTradeResult.NO_RESPONSE) {
                TRACE.d(TRACE.NEW_LINE + getString(R.string.card_no_response));

                //statusEditText.setText(getString(R.string.card_no_response));
            }

        }

        @Override
        public void onRequestTime() {
            TRACE.d("onRequestTime");

            String terminalTime = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
            pos.sendTime(terminalTime);
            Status_lector.setText(getString(R.string.request_terminal_time) + " " + terminalTime);
        }

        @Override
        public void onRequestDisplay(QPOSService.Display displayMsg) {
            TRACE.d("onRequestDisplay(Display displayMsg):" + displayMsg.toString());


            String msg = "";
            if (displayMsg == QPOSService.Display.CLEAR_DISPLAY_MSG) {
                msg = "";
            } else if (displayMsg == QPOSService.Display.MSR_DATA_READY) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Audio");
                builder.setMessage("Success,Contine ready");
                builder.setPositiveButton("Confirm", null);
                builder.show();
            } else if (displayMsg == QPOSService.Display.PLEASE_WAIT) {
                msg = getString(R.string.wait);
            } else if (displayMsg == QPOSService.Display.REMOVE_CARD) {
                msg = getString(R.string.remove_card);
            } else if (displayMsg == QPOSService.Display.TRY_ANOTHER_INTERFACE) {
                msg = getString(R.string.try_another_interface);
            } else if (displayMsg == QPOSService.Display.PROCESSING) {
                msg = getString(R.string.processing);
                ChangeViewtoProccess();
            } else if (displayMsg == QPOSService.Display.INPUT_PIN_ING) {
                msg = "please input pin on pos";
            } else if (displayMsg == QPOSService.Display.INPUT_OFFLINE_PIN_ONLY || displayMsg == QPOSService.Display.INPUT_LAST_OFFLINE_PIN) {
                msg = "please input offline pin on pos";
            } else if (displayMsg == QPOSService.Display.MAG_TO_ICC_TRADE) {
                msg = "please insert chip card on pos";
            } else if (displayMsg == QPOSService.Display.CARD_REMOVED) {
                msg = "card removed";
            }
            Status_lector.setText(msg);
        }

        @Override
        public void onRequestOnlineProcess(final String tlv) {
            TRACE.d("\nonRequestOnlineProcess \n" + tlv);
            Status_lector.setText(R.string.request_data_to_server);

            //pos.getIccCardNo(getTime());

            //dialog = new Dialog(mContext);
            //dialog.setContentView(R.layout.alert_dialog);
            //dialog.setTitle(R.string.request_data_to_server);
            Hashtable<String, String> decodeData = pos.anlysEmvIccData(tlv);
            TRACE.d("\nanlysEmvIccData(tlv):\n" + decodeData.toString());
            String decodeData2 = pos.anlysEmvTLVData(tlv);
            TRACE.d("\nanlysEmvTLVData(tlv):\n" + decodeData2);
//            call(tlv);
            if (isPinCanceled) {
                Status_lector.setText(R.string.replied_failed);

                //((TextView) dialog.findViewById(R.id.messageTextView))
                //        .setText(R.string.replied_failed);
            } else {
                Status_lector.setText(R.string.replied_success);
                /*intent = new Intent(mContext,WMX_TransactionResult.class);
                intent.putExtra("result", "success");
                startActivity(intent);*/
                //ChangeViewToTicket();
//                call(tlv);

                //((TextView) dialog.findViewById(R.id.messageTextView))
                //.setText(R.string.replied_success);
            }
            try {
//                    analyData(tlv);// analy tlv ,get the tag you need
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (isPinCanceled) {
                pos.sendOnlineProcessResult(null);
            } else {

                //String str = "5A0A6214672500000000056F5F24032307315F25031307085F2A0201565F34010182027C008407A00000033301018E0C000000000000000002031F009505088004E0009A031406179C01009F02060000000000019F03060000000000009F0702AB009F080200209F0902008C9F0D05D86004A8009F0E0500109800009F0F05D86804F8009F101307010103A02000010A010000000000CE0BCE899F1A0201569F1E0838333230314943439F21031826509F2608881E2E4151E527899F2701809F3303E0F8C89F34030203009F3501229F3602008E9F37042120A7189F4104000000015A0A6214672500000000056F5F24032307315F25031307085F2A0201565F34010182027C008407A00000033301018E0C000000000000000002031F00";
                // str = "9F26088930C9018CAEBCD69F2701809F101307010103A02802010A0100000000007EF350299F370415B4E5829F360202179505000004E0009A031504169C01009F02060000000010005F2A02015682027C009F1A0201569F03060000000000009F330360D8C89F34030203009F3501229F1E0838333230314943438408A0000003330101019F090200209F410400000001";
                 String str = "8A023030";//Currently the default value,
                // should be assigned to the server to return data,
                // the data format is TLV
                pos.sendOnlineProcessResult(str);//Script notification/55domain/ICCDATA

            }

        }

        @Override
        public void onRequestBatchData(String tlv) {
            TRACE.d(getString(R.string.end_transaction));
            String content = getString(R.string.batch_data);
            TRACE.d("\n\"onRequestBatchData(String tlv):\":\n" + tlv);
            content += tlv;
            Status_lector.setText(content);
            call(tlv);
//            autoDoTrade(0);
        }

        @Override
        public void onRequestTransactionResult(QPOSService.TransactionResult transactionResult) {
            TRACE.d("onRequestTransactionResult()" + transactionResult.toString());
            if (transactionResult == QPOSService.TransactionResult.CARD_REMOVED) {
                //clearDisplay();
                Status_lector.setText("CARD_REMOVED");
            }

            //dismissDialog();

            //dialog = new Dialog(mContext);
            //dialog.setContentView(R.layout.alert_dialog);
            //dialog.setTitle(R.string.transaction_result);
            //TextView messageTextView = (TextView) dialog.findViewById(R.id.messageTextView);


            if (transactionResult == QPOSService.TransactionResult.APPROVED) {
                TRACE.d("TransactionResult.APPROVED");
                String message = getString(R.string.transaction_approved) + "\n" + getString(R.string.amount) + ": $" + Amount + "\n";
                /**if (!cashbackAmount.equals("")) {
                 message += getString(R.string.cashback_amount) + ": INR" + cashbackAmount;
                 }**/
                //messageTextView.setText(message);
                Status_lector.setText(message);

                    ICCTag=pos.getICCTag(QPOSService.EncryptType.PLAINTEXT,1,1,"57");
                    TRACE.d("ICCTag" + ICCTag + TRACE.NEW_LINE);

                    TransactionData tr = new TransactionData();
                    CypherFunctions cy = new CypherFunctions();

//                    RSAData rsaD = new RSAData();
//                    RSA rsa = new RSA();


//                    try {
//                        rsaD = rsa.generateKeys("3082010902820100D45E88EE86A0DA2C0ED64A86FFDEAB3267117A918DE81E5FD0EA7559D870C9EBE4F8778B63EF1A952ACF5B57EC057867E37E985186EB08A75FB42108CB7CE07FDE834763A8AF599C96B4956583888C8C4A6E106485173C3D1AF505BC7379622BFEFF4FBCCB18FE15028DC6B4960CE0F0E5FA8C4D1E7A4FF6CAC86B0E9C13FC7651D9A9A41355FF9140265F66D770135218168E85ED41EA82F2426EC89A2F6AA140CB7F91CDDD7D35B9C0E086214EEBA0600B888D65987CD49AD210E5A6948B2B782A76D6861FA6A79040A8C6B5C44614C8025D59F228AEC1BA951CBCB29C236B3D20276E8878BD6D7A88BB601CF8669AD660DB14262CB970298E6874A7134D7F0203010001");
//                        tr_tk = rsaD.getTk();
//                    }catch (Throwable t){
//                        TRACE.d("Throwable RSA" + t.toString());
//                    }



                    String tr_key = "D7270B642DC710AA8B96071A527F47FE";
                    String tr_ksn = "00000205174019600001";
                    String tr_tk="95A204A86BE3F79512BBD077340C4E27";
                    String track2 = "2308840200445199D2307FFFFFFFFFFFFFFFFFFFFFFFFFFF";
                    Integer tr_counter = 2;

                    tr.setKey(tr_key);
                    tr.setKsn(tr_ksn);
                    tr.setTk(tr_tk);
                    tr.setTrack1("");
                    tr.setTrack2(track2);
                    tr.setCounter(tr_counter);

                    try {
                        DUKPTData dukpt = new DUKPTData();
                        dukpt = cy.encryptDUKPT(tr);
                        TRACE.d(TRACE.NEW_LINE + TRACE.NEW_LINE + "dukpt" + TRACE.NEW_LINE + dukpt.toString()+TRACE.NEW_LINE+TRACE.NEW_LINE);
                    }catch (Throwable t){
                        TRACE.d("Throwable" + t.toString());
                    }



                    ChangeViewToTicket();

            } else if (transactionResult == QPOSService.TransactionResult.TERMINATED) {
                //clearDisplay();
                Status_lector.setText(getString(R.string.transaction_terminated));
            } else if (transactionResult == QPOSService.TransactionResult.DECLINED) {
                Status_lector.setText(getString(R.string.transaction_declined));
            } else if (transactionResult == QPOSService.TransactionResult.CANCEL) {
                //clearDisplay();
                Status_lector.setText(getString(R.string.transaction_cancel));
            } else if (transactionResult == QPOSService.TransactionResult.CAPK_FAIL) {
                Status_lector.setText(getString(R.string.transaction_capk_fail));
            } else if (transactionResult == QPOSService.TransactionResult.NOT_ICC) {
                Status_lector.setText(getString(R.string.transaction_not_icc));
            } else if (transactionResult == QPOSService.TransactionResult.SELECT_APP_FAIL) {
                Status_lector.setText(getString(R.string.transaction_app_fail));
            } else if (transactionResult == QPOSService.TransactionResult.DEVICE_ERROR) {
                Status_lector.setText(getString(R.string.transaction_device_error));
            } else if (transactionResult == QPOSService.TransactionResult.TRADE_LOG_FULL) {
                //statusEditText.setText("pls clear the trace log and then to begin do trade");
                Status_lector.setText("the trade log has fulled!pls clear the trade log!");
            } else if (transactionResult == QPOSService.TransactionResult.CARD_NOT_SUPPORTED) {
                Status_lector.setText(getString(R.string.card_not_supported));
            } else if (transactionResult == QPOSService.TransactionResult.MISSING_MANDATORY_DATA) {
                Status_lector.setText(getString(R.string.missing_mandatory_data));
            } else if (transactionResult == QPOSService.TransactionResult.CARD_BLOCKED_OR_NO_EMV_APPS) {
                Status_lector.setText(getString(R.string.card_blocked_or_no_evm_apps));
            } else if (transactionResult == QPOSService.TransactionResult.INVALID_ICC_DATA) {
                Status_lector.setText(getString(R.string.invalid_icc_data));
            } else if (transactionResult == QPOSService.TransactionResult.FALLBACK) {
                Status_lector.setText("trans fallback");
            } else if (transactionResult == QPOSService.TransactionResult.NFC_TERMINATED) {
                //clearDisplay();
                Status_lector.setText("NFC Terminated");
            } else if (transactionResult == QPOSService.TransactionResult.CARD_REMOVED) {
                //clearDisplay();
                Status_lector.setText("CARD REMOVED");
            } else if (transactionResult == QPOSService.TransactionResult.TRANS_TOKEN_INVALID) {
                //clearDisplay();
                Status_lector.setText("TOKEN INVALID");
            }

            /**onRequestTransactionResult

             dialog.findViewById(R.id.confirmButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            dismissDialog();
            }
            });

             dialog.show();
             amount = "";
             cashbackAmount = "";
             **/
        }

        @Override
        public void onRequestSetPin() {

            TRACE.d("onRequestSetPin()");

            dialogPin = new Dialog(mContext);
            dialogPin.setContentView(R.layout.wmx_pin_keyboard);



            /**
             dialog = new Dialog(mContext);
             dialog.setContentView(R.layout.wmx_pin_keyboard);





             dialog.findViewById(R.id.confirmButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            String pin = ((EditText) dialog.findViewById(R.id.pinEditText)).getText().toString();
            if (pin.length() >= 4 && pin.length() <= 12) {
            if (pin.equals("000000")) {
            pos.sendEncryptPin("5516422217375116");

            } else {
            pos.sendPin(pin);
            }
            //dismissDialog();
            }
            }
            });

             dialog.findViewById(R.id.bypassButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            //					pos.bypassPin();
            pos.sendPin("");

            //dismissDialog();
            }
            });

             dialog.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            isPinCanceled = true;
            pos.cancelPin();
            //dismissDialog();
            }
            });

             dialog.show();**/

        }

        public void onQposRequestPinResult(List<String> dataList, int offlineTime) {
            TRACE.d("onQposRequestPinResult()");
            super.onQposRequestPinResult(dataList, offlineTime);
            keyBoardList = dataList;
            MyKeyboardView.setKeyBoardListener(new KeyBoardNumInterface() {
                @Override
                public void getNumberValue(String value) {
//                    statusEditText.setText("Pls click "+dataList.get(0));
                    pos.pinMapSync(value,20);

                }
            });
            keyboardUtil = new KeyboardUtil(WMX_Card.this, lin, dataList);
            keyboardUtil.initKeyboard(MyKeyboardView.KEYBOARDTYPE_Only_Num_Pwd, Pruebaedittext);
        }

        @Override
        public void onReturnGetPinInputResult(int num) {
            TRACE.d("onReturnGetPinInputResult() " + num);

            super.onReturnGetPinInputResult(num);
            String s = "";
            if(num == -1){
                if(keyboardUtil != null) {
                    keyboardUtil.hide();
                    TRACE.d("FINAL INOUT PIN " );
                }
            }else{
                for(int i = 0 ; i <num ; i ++){
                    s += "*";
                }
                keyboardUtil.setPinText(num);
                Pruebaedittext.setText(s);//"Pin ：
            }
        }

        @Override
        public void onReturnGetPinResult(Hashtable<String, String> result) {
            TRACE.d("onReturnGetPinResult(Hashtable<String, String> result):" + result.toString());
            String pinBlock = result.get("pinBlock");
            String pinKsn = result.get("pinKsn");
            String content = "get pin result\n";
            content += getString(R.string.pinKsn) + " " + pinKsn + "\n";
            content += getString(R.string.pinBlock) + " " + pinBlock + "\n";
            Pruebaedittext.setText(content);
            TRACE.i(content);
        }

        @Override
        public void onReturnGetKeyBoardInputResult(String result) {
            TRACE.d("onReturnGetKeyBoardInputResult()");
        }

        @Override
        public void onQposInfoResult(Hashtable<String, String> posInfoData) {
            TRACE.d("onQposInfoResult" + posInfoData.toString());
        }

        @Override
        public void onRequestTransactionLog(String tlv) {
            TRACE.d("onRequestTransactionLog(String tlv):" + tlv);
        }

        @Override
        public void onQposIdResult(Hashtable<String, String> posIdTable) {
            TRACE.w("onQposIdResult():" + posIdTable.toString());
            /*String posId = posIdTable.get("posId") == null ? "" : posIdTable.get("posId");
            String csn = posIdTable.get("csn") == null ? "" : posIdTable.get("csn");
            String psamId = posIdTable.get("psamId") == null ? "" : posIdTable
                    .get("psamId");
            String NFCId = posIdTable.get("nfcID") == null ? "" : posIdTable
                    .get("nfcID");
            String content = "";
            content += getString(R.string.posId) + posId + "\n";
            content += "csn: " + csn + "\n";
            content += "conn: " + pos.getBluetoothState() + "\n";
            content += "psamId: " + psamId + "\n";
            content += "NFCId: " + NFCId + "\n";
            statusEditText.setText(content);*/

        }

        @Override
        public void onRequestSelectEmvApp(ArrayList<String> appList) {
            TRACE.d("onRequestSelectEmvApp():" + appList.toString());
        }

        @Override
        public void onRequestIsServerConnected() {
            TRACE.d("onRequestIsServerConnected()");
            pos.isServerConnected(true);
        }

        @Override
        public void onRequestFinalConfirm() {
            TRACE.d("onRequestFinalConfirm() ");
        }

        @Override
        public void onRequestNoQposDetected() {
            TRACE.d("onRequestNoQposDetected()");
        }

        @Override
        public void onRequestQposDisconnected() {
            TRACE.d("onRequestQposDisconnected()");
        }

        @Override
        public void onError(QPOSService.Error errorState) {
            TRACE.d("onError" + errorState.toString());
            WMX_Card.super.showAlert("ERROR", errorState.toString());
        }

        @Override
        public void onReturnReversalData(String tlv) {
            TRACE.d("onReturnReversalData(): " + tlv);
        }

        @Override
        public void onReturnApduResult(boolean arg0, String arg1, int arg2) {
            // TODO Auto-generated method stub
            TRACE.d("onReturnApduResult(boolean arg0, String arg1, int arg2):" + arg0 + TRACE.NEW_LINE + arg1 + TRACE.NEW_LINE + arg2);
        }

        @Override
        public void onReturnPowerOffIccResult(boolean arg0) {
            // TODO Auto-generated method stub
            TRACE.d("onReturnPowerOffIccResult(boolean arg0):" + arg0);

        }

        @Override
        public void onReturnPowerOnIccResult(boolean arg0, String arg1, String arg2, int arg3) {
            // TODO Auto-generated method stub
            TRACE.d("onReturnPowerOnIccResult(boolean arg0, String arg1, String arg2, int arg3) :" + arg0 + TRACE.NEW_LINE + arg1 + TRACE.NEW_LINE + arg2 + TRACE.NEW_LINE + arg3);

            if (arg0) {
                pos.sendApdu("123456");
            }
        }

        @Override
        public void onReturnSetSleepTimeResult(boolean isSuccess) {
            TRACE.d("onReturnSetSleepTimeResult(boolean isSuccess):" + isSuccess);
        }

        @Override
        public void onGetCardNoResult(String cardNo) {//get card number result
            TRACE.d("onGetCardNoResult(String cardNo):" + cardNo);
            cardNofinal = cardNo;
        }

        @Override
        public void onRequestCalculateMac(String calMac) {
            TRACE.d("onRequestCalculateMac(String calMac):" + calMac);
        }

        @Override
        public void onRequestSignatureResult(byte[] arg0) {
            TRACE.d("onRequestSignatureResult(byte[] arg0):" + arg0.toString());
        }

        @Override
        public void onRequestUpdateWorkKeyResult(QPOSService.UpdateInformationResult result) {
            TRACE.d("onRequestUpdateWorkKeyResult(UpdateInformationResult result):" + result);
        }

        @Override
        public void onReturnCustomConfigResult(boolean isSuccess, String result) {
            TRACE.d("onReturnCustomConfigResult(boolean isSuccess, String result):" + isSuccess + TRACE.NEW_LINE + result);
        }

        @Override
        public void onReturnSetMasterKeyResult(boolean isSuccess) {
            TRACE.d("onReturnSetMasterKeyResult(boolean isSuccess) : " + isSuccess);
        }

        @Override
        public void onReturnBatchSendAPDUResult(LinkedHashMap<Integer, String> batchAPDUResult) {
            TRACE.d("onReturnBatchSendAPDUResult(LinkedHashMap<Integer, String> batchAPDUResult):" + batchAPDUResult.toString());
        }

        @Override
        public void onBluetoothBondFailed() {
            TRACE.d("onBluetoothBondFailed()");
        }

        @Override
        public void onBluetoothBondTimeout() {
            TRACE.d("onBluetoothBondTimeout()");
        }

        @Override
        public void onBluetoothBonded() {
            TRACE.d("onBluetoothBonded()");
        }

        @Override
        public void onBluetoothBonding() {
            TRACE.d("onBluetoothBonding()");
        }

        @Override
        public void onReturniccCashBack(Hashtable<String, String> result) {
            TRACE.d("onReturniccCashBack(Hashtable<String, String> result):" + result.toString());
        }

        @Override
        public void onLcdShowCustomDisplay(boolean arg0) {
            // TODO Auto-generated method stub
            TRACE.d("onLcdShowCustomDisplay(boolean arg0):" + arg0);
        }

        @Override
        public void onUpdatePosFirmwareResult(QPOSService.UpdateInformationResult arg0) {
            TRACE.d("onUpdatePosFirmwareResult(UpdateInformationResult arg0):" + arg0.toString());
        }

        @Override
        public void onReturnDownloadRsaPublicKey(HashMap<String, String> map) {
            TRACE.d("onReturnDownloadRsaPublicKey(HashMap<String, String> map):" + map.toString());
        }

        @Override
        public void onGetPosComm(int mod, String amount, String posid) {
            TRACE.d("onGetPosComm(int mod, String amount, String posid):" + mod + TRACE.NEW_LINE + amount + TRACE.NEW_LINE + posid);
        }

        @Override
        public void onPinKey_TDES_Result(String arg0) {
            TRACE.d("onPinKey_TDES_Result(String arg0):" + arg0);
        }

        @Override
        public void onUpdateMasterKeyResult(boolean arg0, Hashtable<String, String> arg1) {
            // TODO Auto-generated method stub
            TRACE.d("onUpdateMasterKeyResult(boolean arg0, Hashtable<String, String> arg1):" + arg0 + TRACE.NEW_LINE + arg1.toString());

        }

        @Override
        public void onEmvICCExceptionData(String arg0) {
            // TODO Auto-generated method stub
            TRACE.d("onEmvICCExceptionData(String arg0):" + arg0);

        }

        @Override
        public void onSetParamsResult(boolean arg0, Hashtable<String, Object> arg1) {
            // TODO Auto-generated method stub
            TRACE.d("onSetParamsResult(boolean arg0, Hashtable<String, Object> arg1):" + arg0 + TRACE.NEW_LINE + arg1.toString());

        }

        @Override
        public void onGetInputAmountResult(boolean arg0, String arg1) {
            // TODO Auto-generated method stub
            TRACE.d("onGetInputAmountResult(boolean arg0, String arg1):" + arg0 + TRACE.NEW_LINE + arg1.toString());

        }

        @Override
        public void onReturnNFCApduResult(boolean arg0, String arg1, int arg2) {
            // TODO Auto-generated method stub
            TRACE.d("onReturnNFCApduResult(boolean arg0, String arg1, int arg2):" + arg0 + TRACE.NEW_LINE + arg1 + TRACE.NEW_LINE + arg2);
        }

        @Override
        public void onReturnPowerOffNFCResult(boolean arg0) {
            // TODO Auto-generated method stub
            TRACE.d(" onReturnPowerOffNFCResult(boolean arg0) :" + arg0);
        }

        @Override
        public void onReturnPowerOnNFCResult(boolean arg0, String arg1, String arg2, int arg3) {
            // TODO Auto-generated method stub
            TRACE.d("onReturnPowerOnNFCResult(boolean arg0, String arg1, String arg2, int arg3):" + arg0 + TRACE.NEW_LINE + arg1 + TRACE.NEW_LINE + arg2 + TRACE.NEW_LINE + arg3);
        }

        @Override
        public void onCbcMacResult(String result) {
            TRACE.d("onCbcMacResult(String result):" + result);
        }

        @Override
        public void onReadBusinessCardResult(boolean arg0, String arg1) {
            // TODO Auto-generated method stub
            TRACE.d(" onReadBusinessCardResult(boolean arg0, String arg1):" + arg0 + TRACE.NEW_LINE + arg1);

        }

        @Override
        public void onWriteBusinessCardResult(boolean arg0) {
            // TODO Auto-generated method stub
            TRACE.d(" onWriteBusinessCardResult(boolean arg0):" + arg0);

        }

        @Override
        public void onConfirmAmountResult(boolean arg0) {
            // TODO Auto-generated method stub
            TRACE.d("onConfirmAmountResult(boolean arg0):" + arg0);

        }

        @Override
        public void onQposIsCardExist(boolean cardIsExist) {
            TRACE.d("onQposIsCardExist(boolean cardIsExist):" + cardIsExist);
        }

        @Override
        public void onSearchMifareCardResult(Hashtable<String, String> arg0) {
            if (arg0 != null) {
                TRACE.d("onSearchMifareCardResult(Hashtable<String, String> arg0):" + arg0.toString());
               /* String statuString = arg0.get("status");
                String cardTypeString = arg0.get("cardType");
                String cardUidLen = arg0.get("cardUidLen");
                String cardUid = arg0.get("cardUid");
                String cardAtsLen = arg0.get("cardAtsLen");
                String cardAts = arg0.get("cardAts");
                String ATQA = arg0.get("ATQA");
                String SAK = arg0.get("SAK");
                statusEditText.setText("statuString:" + statuString + "\n" + "cardTypeString:" + cardTypeString + "\ncardUidLen:" + cardUidLen
                        + "\ncardUid:" + cardUid + "\ncardAtsLen:" + cardAtsLen + "\ncardAts:" + cardAts
                        + "\nATQA:" + ATQA + "\nSAK:" + SAK);*/
            } else {
                TRACE.d("onSearchMifareCardResult poll card failed");
            }
        }

        @Override
        public void onBatchReadMifareCardResult(String msg, Hashtable<String, List<String>> cardData) {
            if (cardData != null) {
                TRACE.d("onBatchReadMifareCardResult(boolean arg0):" + msg + cardData.toString());
            }
        }

        @Override
        public void onBatchWriteMifareCardResult(String msg, Hashtable<String, List<String>> cardData) {
            if (cardData != null) {
                TRACE.d("onBatchWriteMifareCardResult(boolean arg0):" + msg + cardData.toString());
            }
        }

        @Override
        public void onSetBuzzerResult(boolean arg0) {
            TRACE.d("onSetBuzzerResult(boolean arg0):" + arg0);


        }

        @Override
        public void onSetBuzzerTimeResult(boolean b) {
            TRACE.d("onSetBuzzerTimeResult(boolean b):" + b);

        }

        @Override
        public void onSetBuzzerStatusResult(boolean b) {
            TRACE.d("onSetBuzzerStatusResult(boolean b):" + b);

        }

        @Override
        public void onGetBuzzerStatusResult(String s) {
            TRACE.d("onGetBuzzerStatusResult(String s):" + s);

        }

        @Override
        public void onSetManagementKey(boolean arg0) {
            TRACE.d("onSetManagementKey(boolean arg0):" + arg0);

        }

        @Override
        public void onReturnUpdateIPEKResult(boolean arg0) {
            TRACE.d("onReturnUpdateIPEKResult(boolean arg0):" + arg0);

        }

        @Override
        public void onReturnUpdateEMVRIDResult(boolean arg0) {
            TRACE.d("onReturnUpdateEMVRIDResult(boolean arg0):" + arg0);
        }

        @Override
        public void onReturnUpdateEMVResult(boolean arg0) {
            // TODO Auto-generated method stub
            TRACE.d("onReturnUpdateEMVResult(boolean arg0):" + arg0);
        }

        @Override
        public void onBluetoothBoardStateResult(boolean arg0) {
            // TODO Auto-generated method stub
            TRACE.d("onBluetoothBoardStateResult(boolean arg0):" + arg0);


        }

        @Override
        public void onDeviceFound(BluetoothDevice arg0) {
            TRACE.d("onDeviceFound()");

            // TODO Auto-generated method stub

        }

        @Override
        public void onSetSleepModeTime(boolean arg0) {
            TRACE.d("onSetSleepModeTime(boolean arg0):" + arg0);
        }

        @Override
        public void onReturnGetEMVListResult(String arg0) {
            // TODO Auto-generated method stub
            TRACE.d("onReturnGetEMVListResult(String arg0):" + arg0);

        }

        @Override
        public void onWaitingforData(String arg0) {
            // TODO Auto-generated method stub
            TRACE.d("onWaitingforData(String arg0):" + arg0);

        }

        @Override
        public void onRequestDeviceScanFinished() {
            // TODO Auto-generated method stub
            TRACE.d("onRequestDeviceScanFinished()");

        }

        @Override
        public void onRequestUpdateKey(String arg0) {
            // TODO Auto-generated method stub
            TRACE.d("onRequestUpdateKey(String arg0):" + arg0);
        }

        @Override
        public void onReturnGetQuickEmvResult(boolean arg0) {
            // TODO Auto-generated method stub
            TRACE.d("onReturnGetQuickEmvResult(boolean arg0):" + arg0);
        }

        @Override
        public void onQposDoGetTradeLogNum(String arg0) {
            TRACE.d("onQposDoGetTradeLogNum(String arg0):" + arg0);
        }

        @Override
        public void onQposDoTradeLog(boolean arg0) {
            TRACE.d("onQposDoTradeLog(boolean arg0) :" + arg0);

            // TODO Auto-generated method stub
        }

        @Override
        public void onAddKey(boolean arg0) {
            TRACE.d("onAddKey(boolean arg0) :" + arg0);
        }

        @Override
        public void onEncryptData(Hashtable<String, String> resultTable) {
            TRACE.d("onEncryptData()");

            if (resultTable != null) {
                TRACE.d("onEncryptData(String arg0) :" + resultTable);
            }
        }

        @Override
        public void onQposKsnResult(Hashtable<String, String> arg0) {
            TRACE.d("onQposKsnResult(Hashtable<String, String> arg0):" + arg0.toString());

            // TODO Auto-generated method stub
            String pinKsn = arg0.get("pinKsn");
            String trackKsn = arg0.get("trackKsn");
            String emvKsn = arg0.get("emvKsn");
            TRACE.d("get the ksn result is :" + "pinKsn" + pinKsn + "\ntrackKsn" + trackKsn + "\nemvKsn" + emvKsn);

        }

        @Override
        public void onQposDoGetTradeLog(String arg0, String arg1) {
            TRACE.d("onQposDoGetTradeLog(String arg0, String arg1):" + arg0 + TRACE.NEW_LINE + arg1);
        }

        @Override
        public void onRequestDevice() {
            TRACE.d("onRequestDevice()");
        }

        @Override
        public void onGetKeyCheckValue(List<String> checkValue) {
            TRACE.d("onGetKeyCheckValue()");
        }

        @Override
        public void onGetDevicePubKey(String clearKeys) {
            TRACE.d("onGetDevicePubKey(clearKeys):" + clearKeys);
        }

        @Override
        public void onTradeCancelled() {
            TRACE.d("onTradeCancelled");
        }

        @Override
        public void onReturnSetAESResult(boolean isSuccess, String result) {
            TRACE.d("onReturnSetAESResult()");

        }

        @Override
        public void onReturnAESTransmissonKeyResult(boolean isSuccess, String result) {
            TRACE.d("onReturnAESTransmissonKeyResult()");

        }

        @Override
        public void onReturnSignature(boolean b, String signaturedData) {
            TRACE.d("onReturnSignature()");
        }

        @Override
        public void onReturnConverEncryptedBlockFormat(String result) {
            TRACE.d("onReturnConverEncryptedBlockFormat()");
        }

        @Override
        public void onQposIsCardExistInOnlineProcess(boolean haveCard) {
            TRACE.d("onQposIsCardExistInOnlineProcess()");

        }

        @Override
        public void onFinishMifareCardResult(boolean arg0) {
            // TODO Auto-generated method stub
            TRACE.d("onFinishMifareCardResult(boolean arg0):" + arg0);
        }

        @Override
        public void onVerifyMifareCardResult(boolean arg0) {
            TRACE.d("onVerifyMifareCardResult(boolean arg0):" + arg0);

            // TODO Auto-generated method stub
//			String msg = pos.getMifareStatusMsg();
        }

        @Override
        public void onReadMifareCardResult(Hashtable<String, String> arg0) {
            TRACE.d("onReadMifareCardResult()");

            // TODO Auto-generated method stub
//			String msg = pos.getMifareStatusMsg();
        }

        @Override
        public void onWriteMifareCardResult(boolean arg0) {
            // TODO Auto-generated method stub
            TRACE.d("onWriteMifareCardResult(boolean arg0):" + arg0);

        }

        @Override
        public void onOperateMifareCardResult(Hashtable<String, String> arg0) {
            // TODO Auto-generated method stub
            if (arg0 != null) {
                TRACE.d("onOperateMifareCardResult(Hashtable<String, String> arg0):" + arg0.toString());

                String cmd = arg0.get("Cmd");
                String blockAddr = arg0.get("blockAddr");
                //statusEditText.setText("Cmd:" + cmd + "\nBlock Addr:" + blockAddr);
            } else {
                //statusEditText.setText("operate failed");
                TRACE.d("onOperateMifareCardResult operate failed" );

            }
        }

        @Override
        public void getMifareCardVersion(Hashtable<String, String> arg0) {

            // TODO Auto-generated method stub
            if (arg0 != null) {
                TRACE.d("getMifareCardVersion(Hashtable<String, String> arg0):" + arg0.toString());

                String verLen = arg0.get("versionLen");
                String ver = arg0.get("cardVersion");
                //statusEditText.setText("versionLen:" + verLen + "\nverison:" + ver);
            } else {
                //statusEditText.setText("get mafire UL version failed");
                TRACE.d("getMifareCardVersion get mafire UL version failed");

            }
        }

        @Override
        public void getMifareFastReadData(Hashtable<String, String> arg0) {
            // TODO Auto-generated method stub

            if (arg0 != null) {
                TRACE.d("getMifareFastReadData(Hashtable<String, String> arg0):" + arg0.toString());
                String startAddr = arg0.get("startAddr");
                String endAddr = arg0.get("endAddr");
                String dataLen = arg0.get("dataLen");
                String cardData = arg0.get("cardData");
                //statusEditText.setText("startAddr:" + startAddr + "\nendAddr:" + endAddr + "\ndataLen:" + dataLen
                //        + "\ncardData:" + cardData);
            } else {
                //statusEditText.setText("read fast UL failed");
                TRACE.d("getMifareFastReadData read fast UL failed" );

            }
        }

        @Override
        public void getMifareReadData(Hashtable<String, String> arg0) {

            if (arg0 != null) {
                TRACE.d("getMifareReadData(Hashtable<String, String> arg0):" + arg0.toString());

                String blockAddr = arg0.get("blockAddr");
                String dataLen = arg0.get("dataLen");
                String cardData = arg0.get("cardData");
                //statusEditText.setText("blockAddr:" + blockAddr + "\ndataLen:" + dataLen + "\ncardData:" + cardData);
            } else {
                //statusEditText.setText("read mafire UL failed");
                TRACE.d("getMifareReadData read mafire UL failed" );

            }
        }

        @Override
        public void writeMifareULData(String arg0) {

            if (arg0 != null) {
                TRACE.d("writeMifareULData(String arg0):" + arg0.toString());

                //statusEditText.setText("addr:" + arg0);
            } else {
                //statusEditText.setText("write UL failed");

                TRACE.d("writeMifareULData write UL failed" );

            }
        }

        @Override
        public void verifyMifareULData(Hashtable<String, String> arg0) {

            if (arg0 != null) {
                TRACE.d("verifyMifareULData(Hashtable<String, String> arg0):" + arg0.toString());

                String dataLen = arg0.get("dataLen");
                String pack = arg0.get("pack");
                //statusEditText.setText("dataLen:" + dataLen + "\npack:" + pack);
            } else {
                TRACE.d("verifyMifareULData verify UL failed" );

                //statusEditText.setText("verify UL failed");
            }
        }

        @Override
        public void onGetSleepModeTime(String arg0) {
            // TODO Auto-generated method stub

            if (arg0 != null) {
                TRACE.d("onGetSleepModeTime(String arg0):" + arg0.toString());

                int time = Integer.parseInt(arg0, 16);
                //statusEditText.setText("time is ： " + time + " seconds");
            } else {
                //statusEditText.setText("get the time is failed");
                TRACE.d("onGetSleepModeTime get the time is failed" + arg0.toString());

            }
        }

        @Override
        public void onGetShutDownTime(String arg0) {

            if (arg0 != null) {
                TRACE.d("onGetShutDownTime(String arg0):" + arg0.toString());

                //statusEditText.setText("shut down time is : " + Integer.parseInt(arg0, 16) + "s");
            } else {
                //statusEditText.setText("get the shut down time is fail!");

                TRACE.d("onGetShutDownTime get the shut down time is fail" );

            }
        }

        @Override
        public void onQposDoSetRsaPublicKey(boolean arg0) {
            // TODO Auto-generated method stub
            TRACE.d("onQposDoSetRsaPublicKey(boolean arg0):" + arg0);
            /*
            if (arg0) {
                statusEditText.setText("set rsa is successed!");

            } else {
                statusEditText.setText("set rsa is failed!");
            }*/
        }

        @Override
        public void onQposGenerateSessionKeysResult(Hashtable<String, String> arg0) {

            if (arg0 != null) {
                TRACE.d("onQposGenerateSessionKeysResult(Hashtable<String, String> arg0):" + arg0.toString());
                String rsaFileName = arg0.get("rsaReginString");
                String enPinKeyData = arg0.get("enPinKey");
                String enKcvPinKeyData = arg0.get("enPinKcvKey");
                String enCardKeyData = arg0.get("enDataCardKey");
                String enKcvCardKeyData = arg0.get("enKcvDataCardKey");
                //statusEditText.setText("rsaFileName:" + rsaFileName + "\nenPinKeyData:" + enPinKeyData + "\nenKcvPinKeyData:" +
                //       enKcvPinKeyData + "\nenCardKeyData:" + enCardKeyData + "\nenKcvCardKeyData:" + enKcvCardKeyData);
            } else {
                TRACE.d("onQposGenerateSessionKeysResult  get key failed,pls try again!");

                //statusEditText.setText("get key failed,pls try again!");
            }
        }

        @Override
        public void transferMifareData(String arg0) {
            TRACE.d("transferMifareData(String arg0):" + arg0.toString());

           /* // TODO Auto-generated method stub
            if (arg0 != null) {
                statusEditText.setText("response data:" + arg0);
            } else {
                statusEditText.setText("transfer data failed!");
            }*/
        }

        @Override
        public void onReturnRSAResult(String arg0) {
            TRACE.d("onReturnRSAResult(String arg0):" + arg0.toString());
            /*
            if (arg0 != null) {
                statusEditText.setText("rsa data:\n" + arg0);
            } else {
                statusEditText.setText("get the rsa failed");
            }*/
        }

        @Override
        public void onRequestNoQposDetectedUnbond() {
            // TODO Auto-generated method stub
            TRACE.d("onRequestNoQposDetectedUnbond()");

        }

        @Override
        public void onRequestGenerateTransportKey(Hashtable result){
            TRACE.d("onRequestGenerateTransportKey(Hashtable<String, String> arg0):" + result.toString());
            DoTrade();
        }

    }

}
