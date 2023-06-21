package com.efevoopay.demoui.activities;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
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


import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.efevoopay.demoui.keyboard.KeyBoardNumInterface;
import com.efevoopay.demoui.keyboard.KeyboardUtil;
import com.efevoopay.demoui.keyboard.MyKeyboardView;
import com.efevoopay.demoui.utils.DBManager;
import com.efevoopay.demoui.utils.GNTBackEnd;
import com.efevoopay.demoui.utils.ResponseCode;
import com.efevoopay.demoui.utils.TLV;
import com.efevoopay.demoui.utils.TLVParser;
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
import java.util.Locale;

import libdukpt.DUKPK2009_CBC;

import com.blumonpay.capx.model.DUKPTData;
import com.efevoopay.demoui.utils.Utils;


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
    private MediaPlayer Beep;
    private LottieAnimationView LottieTerminalView, LottiePointsView;

    private String FinalPin = "";
    private LinearLayout lin;

    private String FinalTradeType= "";

    private Hashtable<String, String> ICCTag ;

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1001;

    private Button WMX_btn_trade;
    private TextView tv_card_label_1,tv_card_label_2;

    private String cardNofinal ="";
    private String type_transaction;
    public GNTBackEnd gntBackEnd = new GNTBackEnd();
    private DUKPTData _encryptblumon ;
    //RequestQueue requestQueue;
    private String _Propina="";
    private String TransExit= "";
    private String content="";
    private String d4="";
    private Integer msi=0;
    private String ksn_posId;
    private String maskedPAN="";
    private String emvicc="";
    private String pinKsn="";
    private String mascara="000000000000";
    private String _track2MN="";
    private String _noAuth="";
    private String _card="";
    private String _tiptar="";
    private String _redtar="";
    private String _AID="N/A";
    private String _ARQC="N/A";
    private String _9F41="";
    Cursor cursor;
    ProgressDialog spinner;
    private DBManager dbManager;
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
        ksn_posId = intent.getStringExtra("ksn_posId");
        _Propina=intent.getStringExtra("propina");
        if(type_transaction.equals("msi")){
            msi=Integer.parseInt(intent.getStringExtra("months"));
        }else if(type_transaction.equals("Cancelacion")||type_transaction.equals("devolucion")||type_transaction.equals("ajuste")||type_transaction.equals("reverso")||type_transaction.equals("destino")||type_transaction.equals("reautorizacion")||type_transaction.equals("checkout")||type_transaction.equals("cierrepreventa")){
            _noAuth=intent.getStringExtra("cp_tv_auth");
        }

        Status_lector = (TextView) findViewById(R.id.wmx_status_lector);
        Total_Amount = (TextView) findViewById(R.id.wmx_text_total_Amount);
        Pruebaedittext = (EditText) findViewById(R.id.pruebaedittext);
        lin = findViewById(R.id.lyt_card);
        Total_Amount.setText(AmountToShow);
        trading = (Button) findViewById(R.id.WMX_btn_trade);
        trading.setOnClickListener(this);

        pruebas = (Button) findViewById(R.id.WMX_btn_pruebas_tmp);
        pruebas.setOnClickListener(this);

        mContext = this;

        Beep = MediaPlayer.create(mContext, R.raw.beep);
        Beep.setVolume(0.05f, 0.05f);

        LottieTerminalView = findViewById(R.id.terminal_animation);
        LottiePointsView = findViewById(R.id.points_animation);
        tv_card_label_1 = findViewById(R.id.tv_card_label_1);
        tv_card_label_2 = findViewById(R.id.tv_card_label_2);

        dbManager = new DBManager(mContext);
        dbManager.open();
        cursor = dbManager.fetch(ksn_posId);
        initSDK();
        /** open(QPOSService.CommunicationMode.UART);
         posType = POS_TYPE.UART;
         blueTootchAddress = "/dev/ttyS1";
         pos.setDeviceAddress(blueTootchAddress);
         pos.openUart();**/

       /*Handler handler = new Handler();
        handler.postDelayed(() -> {

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
            pos.setCardTradeMode(QPOSService.CardTradeMode.SWIPE_TAP_INSERT_CARD_NOTUP_UNALLOWED_LOW_TRADE);
            pos.doTrade(60);
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
        //pos.setD20Trade(true);
        pos.setConext(this);
        Handler handler = new Handler(Looper.myLooper());
        pos.initListener(handler, listener);
//        pos.getQposId();

    }

    private KeyboardUtil keyboardUtil;

    private List<String> keyBoardList = new ArrayList<>();

    private void ChangeViewtoProccess (){
        LottiePointsView.setVisibility(View.VISIBLE);
        LottieTerminalView.setVisibility(View.GONE);
        tv_card_label_1.setText("Procesando Transacción");
        tv_card_label_2.setText("");
        trading.setVisibility(View.GONE);
    }

    private void ChangeViewToTicket (){
        Intent thisIntent = getIntent();
        String v_total = thisIntent.getStringExtra("total");

        intent = new Intent(this, WMX_final_ticket_transaction.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //TRACE.d("date   "+getTime());

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh:mm");
        Date date = new Date();

        intent.putExtra("type_transaction",type_transaction);
        intent.putExtra("v_total",Total_Amount.getText().toString());
        intent.putExtra("v_time",dateFormat.format(date).toString());
        intent.putExtra("v_card","**** "+_card);
        intent.putExtra("v_redtarjeta",_redtar);
        intent.putExtra("v_tipotarjeta",_tiptar);
        intent.putExtra("v_AID",_AID);
        intent.putExtra("v_ARQC",_ARQC);
        intent.putExtra("ksn_posId", ksn_posId);

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

        startActivityMiddleware(intent);
        pos.closeUart();
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
            String trackipek = "C5BFFC5E6551D64F62E3D80F6A3126F8";
            String trackipekKCV = "B34512";
            String pinKsn = "00000081958255400001";
            String pinipek = "C5BFFC5E6551D64F62E3D80F6A3126F8";
            String pinipekKCV = "B34512";
            String emvKsn = "00000081958255400001";
            String emvIPEK = "C5BFFC5E6551D64F62E3D80F6A3126F8";
            String emipekKCV = "B34512";
            TRACE.d("INYECCION()");

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

            String amount = Amount;
            int cents = (int) Math.round(100*Float.parseFloat(amount));

            pos.setAmount(String.valueOf(cents), "0", "484", TransactionType.GOODS);

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
            maskedPAN="";
            pinKsn="";
            FinalTradeType=result.toString();
            d4=Amount.toString().replace(".","");
            mascara=mascara.substring(0,12-d4.length());
            d4=mascara+d4;
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
                Status_lector.setText(result.toString());
                content = getString(R.string.card_swiped);
                _track2MN="";
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
                    String realPan = "";
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
                    pinKsn = decodeData.get("pinKsn");
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
                    //String realPan = null;
                    _track2MN = DUKPK2009_CBC.getDUKPT(trackksn, encTrack2, DUKPK2009_CBC.Enum_key.DATA, DUKPK2009_CBC.Enum_mode.ECB, null);
                    if (!TextUtils.isEmpty(trackksn) && !TextUtils.isEmpty(encTrack2)) {
                        String clearPan = DUKPK2009_CBC.getDUKPT(trackksn, encTrack2, DUKPK2009_CBC.Enum_key.DATA, DUKPK2009_CBC.Enum_mode.CBC, null);
                        content += "encTrack2:" + " " + clearPan + "\n";
                        realPan = clearPan.substring(0, maskedPAN.length());
                        content += "realPan:" + " " + realPan + "\n";
                    }
                    if (!TextUtils.isEmpty(pinKsn) && !TextUtils.isEmpty(pinBlock) && !TextUtils.isEmpty(realPan)) {
                        String date = DUKPK2009_CBC.getDUKPT(pinKsn, pinBlock, DUKPK2009_CBC.Enum_key.PIN, DUKPK2009_CBC.Enum_mode.CBC, null);
                        String parsCarN = "0000" + realPan.substring(realPan.length() - 13, realPan.length() - 1);
                        String s = DUKPK2009_CBC.xor(parsCarN, date);
                        content += "PIN:" + " " + s + "\n";
                    }
                }
                String terminalTime = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
                //TRACE.d("_track2MN: "+_track2MN);
                maskedPAN=_track2MN.substring(0,8)+"XXXX"+_track2MN.substring(12,16);
                Integer _9f=Integer.parseInt(pinKsn.substring(15,20),16);
                ValidacionRequest(_track2MN.substring(0,8),"MCR","90","",maskedPAN,_track2MN,_9f.toString() ,terminalTime);
                //call(content);
                //Status_lector.setText(content);
//                autoDoTrade(0);

            }
            else if (result == QPOSService.DoTradeResult.NFC_ONLINE) {
                //nfcLog = decodeData.get("nfcLog");
                Status_lector.setText(result.toString());
                TRACE.d("EMV NFC Start");
                Beep.start();
                List<TLV> parse = TLVParser.parse(pos.getNFCBatchData().get("tlv"));
                //C0
                String onLineksn = TLVParser.searchTLV(parse, "C0").value;
                //C2
                String onLineblockData = TLVParser.searchTLV(parse, "C2").value;

                String tlvNFC = DUKPK2009_CBC.getDUKPT(onLineksn, onLineblockData, DUKPK2009_CBC.Enum_key.DATA, DUKPK2009_CBC.Enum_mode.ECB, null);
                List<TLV> NFCparse = TLVParser.parse(tlvNFC);
                String _track2 = TLVParser.searchTLV(NFCparse, "57").value;
                String _entrymode=TLVParser.searchTLV(NFCparse, "9F39").value;
                String _tag50=TLVParser.searchTLV(NFCparse, "50").value;
                String _tag9F12=TLVParser.searchTLV(NFCparse, "9F12").value;
                String _tag9F21=TLVParser.searchTLV(NFCparse, "9F21").value;

                _AID=TLVParser.searchTLV(NFCparse, "4F").value.toUpperCase(Locale.ROOT);
                _ARQC=TLVParser.searchTLV(NFCparse, "9F26").value.toUpperCase(Locale.ROOT);
                //_9F41=TLVParser.searchTLV(NFCparse, "9F41").value.toUpperCase(Locale.ROOT);

                content = getString(R.string.tap_card);
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
                    pinKsn = decodeData.get("trackksn");
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
                Integer _9f=Integer.parseInt(onLineksn.substring(15,20),16);
                maskedPAN=_track2.substring(0,8)+"XXXX"+_track2.substring(12,16);
                ValidacionDatos(maskedPAN.substring(0,8),"NFC",_entrymode,tlvNFC,maskedPAN,_track2,_9f.toString(),_tag50,_tag9F12,_tag9F21);

                //TRACE.d(TRACE.NEW_LINE + "content in NNFC request(?)" +content);
                //call(content);

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
            //TRACE.d("onRequestTime");

            String terminalTime = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
            pos.sendTime(terminalTime);
            TRACE.d("onRequestTime"+terminalTime);
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
            //TRACE.d("\nonRequestOnlineProcess \n" + tlv);
            Status_lector.setText(R.string.request_data_to_server);
            List<TLV> parse = TLVParser.parse(tlv);
            //C0
            String onLineksn = TLVParser.searchTLV(parse, "C0").value;
            //C2
            String onLineblockData = TLVParser.searchTLV(parse, "C2").value;

            emvicc = DUKPK2009_CBC.getDUKPT(onLineksn, onLineblockData, DUKPK2009_CBC.Enum_key.DATA, DUKPK2009_CBC.Enum_mode.ECB, null);
            //TRACE.d("\nemvicc(tlv):\n" + emvicc);
            emvicc=emvicc.substring(8);

            List<TLV> ICCparse = TLVParser.parse(emvicc);

            _AID=TLVParser.searchTLV(ICCparse, "4F").value.toUpperCase(Locale.ROOT);
            _ARQC=TLVParser.searchTLV(ICCparse, "9F26").value.toUpperCase(Locale.ROOT);
            _9F41=onLineksn;

            //pos.getIccCardNo(getTime());

            //dialog = new Dialog(mContext);
            //dialog.setContentView(R.layout.alert_dialog);
            //dialog.setTitle(R.string.request_data_to_server);
            Hashtable<String, String> decodeData = pos.anlysEmvIccData(tlv);
            //TRACE.d("\nanlysEmvIccData(tlv):\n" + decodeData.toString());
            String decodeData2 = pos.anlysEmvTLVData(tlv);
            //TRACE.d("\nanlysEmvTLVData(tlv):\n" + decodeData2);

            if (isPinCanceled) {
                Status_lector.setText(R.string.replied_failed);

            } else {
                Status_lector.setText(R.string.replied_success);
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
            //TRACE.d("\n\"onRequestBatchData(String tlv):\":\n" + tlv);
            content += tlv;
            Status_lector.setText(content);
            //call(tlv);
//            autoDoTrade(0);
        }

        @Override
        public void onRequestTransactionResult(QPOSService.TransactionResult transactionResult) {
            //TRACE.d("onRequestTransactionResult()" + transactionResult.toString());
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
                ICCTag=pos.getICCTag(QPOSService.EncryptType.PLAINTEXT,1,1,"5A");
                String _pinpan=ICCTag.get("tlv").toString();
                ICCTag=pos.getICCTag(QPOSService.EncryptType.PLAINTEXT,1,1,"57");
                String _track2=ICCTag.get("tlv").toString();
                ICCTag=pos.getICCTag(QPOSService.EncryptType.PLAINTEXT,1,1,"9F39");
                String _entrymode=ICCTag.get("tlv").toString();
                ICCTag=pos.getICCTag(QPOSService.EncryptType.PLAINTEXT,1,1,"9F36");
                String _counter=ICCTag.get("tlv").toString();
                ICCTag=pos.getICCTag(QPOSService.EncryptType.PLAINTEXT,1,1,"50");
                String _tag50=ICCTag.get("tlv").toString();
                ICCTag=pos.getICCTag(QPOSService.EncryptType.PLAINTEXT,1,1,"9F12");
                String _tag9F12=ICCTag.get("tlv").toString();
                ICCTag=pos.getICCTag(QPOSService.EncryptType.PLAINTEXT,1,1,"9F21");
                String _tag9F21=ICCTag.get("tlv").toString();

                //TRACE.d("_9f: " + _9F41);
                String pan=_pinpan.substring(4,12)+"XXXX"+_pinpan.substring(16,_pinpan.length());

                Integer F41=Integer.parseInt(_9F41.substring(15,20),16);
                //Integer F41=Integer.parseInt(_9F41);

                ValidacionDatos(_pinpan.substring(4,12),"ICC",_entrymode.substring(6,_entrymode.length()),emvicc,pan,_track2.substring(4,_track2.length()),F41.toString(),_tag50,_tag9F12,_tag9F21.substring(6,_tag9F21.length()));

                //pos.updateEMVConfigByXml(new String(FileUtils.readAssetsLine("emv_profile_tlv_D30.xml",WMX_Card.this)));

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
                TRACE.d("TransactionResult.NFC_TERMINATED");
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
            //TRACE.d("onQposRequestPinResult()");
            super.onQposRequestPinResult(dataList, offlineTime);
            keyBoardList = dataList;
            MyKeyboardView.setKeyBoardListener(new KeyBoardNumInterface() {
                @Override
                public void getNumberValue(String value) {
                    TRACE.d("init change handle event: " + value);
                    pos.pinMapSync(value, 20);
                }
            });
            keyboardUtil = new KeyboardUtil(WMX_Card.this, lin, dataList);
            keyboardUtil.initKeyboard(MyKeyboardView.KEYBOARDTYPE_Only_Num_Pwd, Pruebaedittext);
        }

        @Override
        public void onReturnGetPinInputResult(int num) {
            //TRACE.d("onReturnGetPinInputResult() " + num);

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
            TRACE.d("onError:" + errorState);
            if (errorState.toString().equals("UNKNOWN")){
                WMX_Card.super.showAlert("informative", "Tarjeta no leída, intente de nuevo.");
            }else{
                WMX_Card.super.showAlert("ERROR", errorState.toString());
            }

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
    private void ValidacionDatos(String _bin,String entrada,String entrymode,String emv,String pan,String track2,String counter,String tag50,String tag9f12,String tag9F21){
        String _redtarj,_tiptarj;
        _tiptarj=gntBackEnd.tagtipotarjeta(gntBackEnd.hexToString(tag50),gntBackEnd.hexToString(tag9f12));
        _redtarj=gntBackEnd.tagredtarjeta(gntBackEnd.hexToString(tag50),gntBackEnd.hexToString(tag9f12));
        if(_tiptarj.equals("Desconocido")||_redtarj.equals(("Desconocido"))){
            ValidacionRequest(_bin,entrada,entrymode,emv,pan,track2,counter,tag9F21);
        }else{
            //TRACE.d("_tiptarj : " + _tiptarj.toString());
            //TRACE.d("_redtarj : " + _redtarj.toString());
            procesofinal(entrada,entrymode,emv,_redtarj,_tiptarj,pan,track2,counter,tag9F21);

        }
    }
    private void ValidacionRequest(String _bin,String entrada,String entrymode,String emv,String pan,String track2,String counter,String time_txn){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String UrlBin=Utils.TERMINAL_BIN+_bin;
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET,UrlBin,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //TRACE.d("UrlBin : " + response.toString());
                            procesofinal(entrada,entrymode,emv,response.getString("scheme").toString(),response.getString("type").toString(),pan,track2,counter,time_txn);
                        } catch (JSONException  e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TRACE.d("UrlBin : VolleyError");
                        procesofinal(entrada,entrymode,emv,"","",pan,track2,counter,time_txn);
                    }
                }
        );
        //requestQueue=Volley.newRequestQueue(mContext);
        requestQueue.add(request);
    }
    public void procesofinal(String entrada,String entrymode,String emv,String redtarjeta,String tipotarjeta,String pan,String track2,String counter,String time_txn){
        _encryptblumon=gntBackEnd.EncryptBlumon(gntBackEnd.MascaraTrack2(track2),Integer.parseInt(counter),cursor);
        TransExit=gntBackEnd.transaccion(entrada,entrymode,pan.substring(12,pan.length()),_encryptblumon.getTrack2(),_encryptblumon.getCrc32Track2(),_encryptblumon.getKsn(),String.valueOf(_encryptblumon.getCounter()),d4,emv,msi,pan,ksn_posId,redtarjeta,tipotarjeta,_Propina,type_transaction,time_txn,_noAuth,_AID,_ARQC,gntBackEnd.CountTrack2(track2),cursor);
        _redtar=gntBackEnd._redtarj;
        _tiptar=gntBackEnd._tiptarj;
        _card=gntBackEnd._card;
        call(TransExit, Utils.TERMINAL_API + "/matriz/certificacion/iso/gral");

    }
    private void call(String contenido,String url) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            final String requestBody = contenido;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    TRACE.d("** ResponseResult " +  TRACE.NEW_LINE + response.toString() );
                    if(response.equals("00")){
                        ChangeViewToTicket();
                        Status_lector.setText(content);
                    }else if(response.equals("")){
                        esperarYCerrar(ksn_posId,_ARQC);
                    }
                    else{
                        ResponseCode.CodeDetails details = ResponseCode.getCodeDetails(response);
                        WMX_Card.super.showAlert("ERROR", details.description);
                        Intent intent = new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityMiddleware(intent);
                        pos.closeUart();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    //TRACE.d("** ResponseVolleyError  " +  TRACE.NEW_LINE + error.toString() );
                    WMX_Card.super.showAlert("ERROR", "TRANSACCION NO PROCESADA : "+ error.toString());
                    Intent intent = new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityMiddleware(intent);
                    pos.closeUart();
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
                    String parsed;
                    try {
                        parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    } catch (UnsupportedEncodingException var4) {
                        parsed = new String(response.data);
                    }

                    if (response != null) {
                        responseString = String.valueOf(parsed);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            TRACE.d("** Exception ERROR " +  TRACE.NEW_LINE + e.toString() );
        }
    }
    public void esperarYCerrar(String deviceid,String arqc) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                ValidacionTxn(ksn_posId,_ARQC);
            }
        }, 4000);
    }
    private void ValidacionTxn(String deviceid,String arqc){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String Urltxn=Utils.TERMINAL_API+"/efevoo/tpv/transaccion";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("deviceid", deviceid);
            jsonBody.put("arqc", arqc);
            final String requestBody = jsonBody.toString();
            //TRACE.d("requestBody " +  TRACE.NEW_LINE + requestBody );
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Urltxn, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //TRACE.d("** ResponseResult " +  TRACE.NEW_LINE + response.toString() );
                    if(response.equals("00")){
                        ChangeViewToTicket();
                        Status_lector.setText(content);
                    }else{
                        ResponseCode.CodeDetails details = ResponseCode.getCodeDetails(response);
                        WMX_Card.super.showAlert("ERROR", details.description);
                        Intent intent = new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityMiddleware(intent);
                        pos.closeUart();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            //TRACE.d("** ResponseVolleyValidacionTxn  " +  TRACE.NEW_LINE + error.toString() );
                            WMX_Card.super.showAlert("ERROR", "VALIDAR TRANSACCION EN HISTORIAL: "+ error.toString());
                            Intent intent = new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivityMiddleware(intent);
                            pos.closeUart();
                        }
                    })
            {
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
                    String parsed;
                    try {
                        parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    } catch (UnsupportedEncodingException var4) {
                        parsed = new String(response.data);
                    }

                    if (response != null) {
                        responseString = String.valueOf(parsed);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void startActivityMiddleware(Intent intent) {
        String CurrPackageName = getPackageName();
        ComponentName name = intent.resolveActivity(getPackageManager());
        String intentPackageName = name.getPackageName();
        String intentClassName = name.getClassName();
        if(intentPackageName.equals(CurrPackageName) && intentClassName.contains(CurrPackageName)) {
            startActivity(intent);
        }
    }

}
