package com.lupay.demoui.utils;

import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;

import com.blumonpay.capx.functions.CypherFunctions;
import com.blumonpay.capx.model.DUKPTData;
import com.blumonpay.capx.model.TransactionData;
import com.lupay.demoui.BuildConfig;
import com.lupay.demoui.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class GNTBackEnd {
    public String _redtarj="";
    public String _tiptarj="";
    public String _card="";
    public String emisor="";
    public String nip="";
    public String entrada="";
    public String pan="";
    private static HashMap TRANS_TYPE_TITLES;
    public static final String TRANS_CAN_TYPE = "CAN";
    public static final String TRANS_DEV_TYPE = "DEV";
    public static final String TRANS_AJU_TYPE = "AJU";
    public static final String TRANS_REV_TYPE= "REV";
    public static final String TRANS_DES_TYPE = "DES";
    public static final String TRANS_CHE_TYPE = "CHE";
    public static final String TRANS_REA_TYPE = "REA";
    public static final String TRANS_OUT_TYPE = "OUT";
    public static final String TRANS_PRE_TYPE = "PRE";
    public static final String TRANS_CIE_TYPE = "CIE";
    public static final String TRANS_VEN_TYPE = "VEN";
    public static final String TRANS_MSI_TYPE = "MSI";
    public static final String TRANS_CANMSI_TYPE = "CANMSI";

    public static void initTransTypeTitles(Resources res) {
        TRANS_TYPE_TITLES = new HashMap<String, String>();
        TRANS_TYPE_TITLES.put(TRANS_CAN_TYPE, res.getString(R.string.wmx_transaction_CAN));
        TRANS_TYPE_TITLES.put(TRANS_DEV_TYPE, res.getString(R.string.wmx_transaction_DEV));
        TRANS_TYPE_TITLES.put(TRANS_AJU_TYPE, res.getString(R.string.wmx_transaction_AJU));
        TRANS_TYPE_TITLES.put(TRANS_REV_TYPE, res.getString(R.string.wmx_transaction_REV));
        TRANS_TYPE_TITLES.put(TRANS_DES_TYPE, res.getString(R.string.wmx_transaction_DES));
        TRANS_TYPE_TITLES.put(TRANS_CHE_TYPE, res.getString(R.string.wmx_transaction_CHE));
        TRANS_TYPE_TITLES.put(TRANS_REA_TYPE, res.getString(R.string.wmx_transaction_REA));
        TRANS_TYPE_TITLES.put(TRANS_OUT_TYPE, res.getString(R.string.wmx_transaction_OUT));
        TRANS_TYPE_TITLES.put(TRANS_PRE_TYPE, res.getString(R.string.wmx_transaction_PRE));
        TRANS_TYPE_TITLES.put(TRANS_CIE_TYPE, res.getString(R.string.wmx_transaction_CIE));
        TRANS_TYPE_TITLES.put(TRANS_VEN_TYPE, "Venta");
        TRANS_TYPE_TITLES.put(TRANS_MSI_TYPE, res.getString(R.string.wmx_transaction_MSI));
        TRANS_TYPE_TITLES.put(TRANS_CANMSI_TYPE, res.getString(R.string.wmx_transaction_CANMSI));
    }

    public static String getTitle(String key) {
        return TRANS_TYPE_TITLES.get(key).toString();
    }

    public DUKPTData EncryptBlumon(String _track2, Cursor cursor) {
        TransactionData tr = new TransactionData();
        CypherFunctions cy = new CypherFunctions();
        DUKPTData dukpt = new DUKPTData();
        //String tr_key = "46D09D3C810F1E70826A3F1A59DF1A59";
        //String tr_ksn = "00000160559893800001";
        //String tr_tk = "B6F0F69E1E6AF2088B80910762FD9EC9";
        String tr_key = cursor.getString(4);
        String tr_ksn = cursor.getString(2);
        String tr_tk = cursor.getString(3);
        String track2 = _track2.toUpperCase(Locale.ROOT);
        //TRACE.d(TRACE.NEW_LINE + "track2" + TRACE.NEW_LINE + track2+TRACE.NEW_LINE);
        //Integer tr_counter = _counter;

        tr.setKey(tr_key);
        tr.setKsn(tr_ksn);
        tr.setTk(tr_tk);
        tr.setTrack1("");
        tr.setTrack2(track2);
        tr.setCounter(Integer.parseInt(cursor.getString(11)));


        TRACE.d(TRACE.NEW_LINE + TRACE.NEW_LINE + "TransactionData" + TRACE.NEW_LINE + tr.toString()+TRACE.NEW_LINE+TRACE.NEW_LINE);
        try {
            dukpt = cy.encryptDUKPT(tr);

            TRACE.d(TRACE.NEW_LINE + TRACE.NEW_LINE + "dukpt" + TRACE.NEW_LINE + dukpt.toString()+TRACE.NEW_LINE+TRACE.NEW_LINE);
        }catch (Throwable t){
            TRACE.d("Throwable" + t.toString());
        }
        return  dukpt;
    }
    public String transaccion(String _entrada,String _entrymode,String _pinpan,String _Track2,String _crc32,String _ksn,String _Counter,String _d4,String _emv,Integer _msi,String _pan,String _deviceid,String _redtarjeta,String _tipotarjeta,String _propina,String _type_trans,String _time_txn,String _p11,String _AID,String _ARQC,String _tamtrack2, Cursor cursor,String _emisor,Integer _nip){
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("tpv", Build.MODEL+"Android smart POS");
            jsonBody.put("deviceid",_deviceid);
            jsonBody.put("entrada", _entrada);
            jsonBody.put("emisor", _emisor);
            jsonBody.put("nip", String.valueOf(_nip));
            jsonBody.put("tipo",tipo(_type_trans));
            jsonBody.put("track2", _Track2);
            jsonBody.put("crc32", _crc32);
            jsonBody.put("count", _Counter);
            jsonBody.put("ksn", _ksn);
            jsonBody.put("tamtrack2", _tamtrack2);
            jsonBody.put("pinPan", _pinpan);
            jsonBody.put("emv", _emv);
            jsonBody.put("d4", _d4);
            jsonBody.put("d18", d18(_type_trans));
            jsonBody.put("d22", d22(_type_trans,_entrymode+"1"));
            jsonBody.put("d95", amounts(_type_trans,_d4));
            jsonBody.put("d121", "");
            jsonBody.put("q6", msi(_msi));
            jsonBody.put("redtarj", redtarjeta(_redtarjeta,_pan.substring(0,1)));
            jsonBody.put("tipotarj", tipotarjeta( _tipotarjeta));
            jsonBody.put("iso", "");
            jsonBody.put("tipotxn",tipotxn(_type_trans));
            jsonBody.put("pan",_pan );
            jsonBody.put("amount",Integer.parseInt(_d4.substring(0,10))+"."+_d4.substring(10,12));
            jsonBody.put("init", "false");
            jsonBody.put("propina", propina(_type_trans,_propina));
            jsonBody.put("msi", _msi);
            jsonBody.put("time_txn", _time_txn);
            jsonBody.put("p11", _p11);
            jsonBody.put("aid", _AID);
            jsonBody.put("arqc", ValidaArqc(_ARQC));
            jsonBody.put("drafcapture", drafcapture(_type_trans));
            jsonBody.put("p43", cursor.getString(5));
            jsonBody.put("p48", cursor.getString(6));
            jsonBody.put("p120", cursor.getString(7));
            jsonBody.put("version", BuildConfig.VERSION_NAME);
            jsonBody.put("interfaz", cursor.getString(22));
            jsonBody.put("codigopostal", cursor.getString(23));
            jsonBody.put("giro", cursor.getString(24));
            jsonBody.put("redlogica", cursor.getString(25));
            _redtarj=jsonBody.getString("redtarj").toString();
            _tiptarj=jsonBody.getString("tipotarj").toString();
            _card=jsonBody.getString("pinPan").toString();
            emisor=jsonBody.getString("emisor").toString();
            nip=jsonBody.getString("nip").toString();
            entrada=jsonBody.getString("entrada").toString();
            pan=jsonBody.getString("pan").toString();
            //TRACE.d(TRACE.NEW_LINE +  jsonBody.toString()+TRACE.NEW_LINE+TRACE.NEW_LINE);
            return jsonBody.toString();
        } catch (JSONException e) {
            TRACE.d("** ERROR JSON " +  TRACE.NEW_LINE + e.toString() );
            return e.toString();
        }
    }
    public String TxnAmex(String _Track2,String _Counter,String _d4,String _emv,String _redtarjeta,String _tipotarjeta,Integer _nip,String _entrada,String _pan,Integer _msi,String _deviceid,String _type_trans,String _propina,String _AID,String _ARQC,String _tarjetaTrack2,String _fechaTrack2,String _pinpan,Cursor cursor){
        JSONObject jsonBody = new JSONObject();
        try {
            String p37 = new SimpleDateFormat("yyMMdd").format(Calendar.getInstance().getTime());

            jsonBody.put("mti", "1100");
            jsonBody.put("p2", _tarjetaTrack2);
            jsonBody.put("p3", "004000");
            jsonBody.put("p4", _d4);
            jsonBody.put("p11", _Counter);
            jsonBody.put("p13", "");
            jsonBody.put("p14",_fechaTrack2);
            jsonBody.put("p19", "484");
            jsonBody.put("p22", p22(_entrada));
            jsonBody.put("p24", "100");
            jsonBody.put("p25", "1900");
            //jsonBody.put("p26", "5814");
            jsonBody.put("p26", cursor.getString(24));
            jsonBody.put("p32", "45678912345");
            jsonBody.put("p33", "45678912345");
            jsonBody.put("p35",_Track2);
            jsonBody.put("p37", p37+String.format("%06d", Integer.parseInt(_Counter)));
            jsonBody.put("p41", _deviceid.substring(_deviceid.length()-8,_deviceid.length()));
            jsonBody.put("p42", p42(_msi,_entrada));
            //jsonBody.put("p43", "AA PAYMENTFACILITATOR=SOFTWARE\\MURCIA 118 COLONIA SAN AGUSTIN\\NUEVO LEON\\66260     MX 484");
            //jsonBody.put("p43", "EFEVOOPAY=EMBOCA\\PROL LOS SOLES 200 VALLE ORIEN\\NUEVO LEON\\66260     MX 484");
            jsonBody.put("p43",cursor.getString(28));
            jsonBody.put("p48", msip48(_msi));
            jsonBody.put("p49", "484");
            jsonBody.put("p53", "");
            //jsonBody.put("p60", "AXAAD70000000CROSSFITCXT1        20HOLA@CROSSFITCXT.COM8184503970          ");
            //jsonBody.put("p60", "AXAAD70000000EMBOCA1             15HOLA@EMBOCA.com8105480593          ");
            jsonBody.put("p60",cursor.getString(29));
            jsonBody.put("p63", "");
            jsonBody.put("emv", _emv);
            jsonBody.put("redtarj", redtarjeta(_redtarjeta,_pan.substring(0,1)));
            jsonBody.put("tipotarj", tipotarjeta( _tipotarjeta));
            jsonBody.put("pinPan", _pinpan);
            jsonBody.put("emisor", "AMEX");
            jsonBody.put("nip", String.valueOf(_nip));
            jsonBody.put("entrada", _entrada);
            jsonBody.put("pan",_pan );
            jsonBody.put("msi", _msi);
            jsonBody.put("deviceid",_deviceid);
            jsonBody.put("tipotxn",tipotxn(_type_trans));
            jsonBody.put("propina", propina(_type_trans,_propina));
            jsonBody.put("aid", _AID);
            jsonBody.put("arqc", _ARQC);
            _redtarj=jsonBody.getString("redtarj").toString();
            _tiptarj=jsonBody.getString("tipotarj").toString();
            _card=jsonBody.getString("pinPan").toString();
            emisor=jsonBody.getString("emisor").toString();
            nip=jsonBody.getString("nip").toString();
            entrada=jsonBody.getString("entrada").toString();
            pan=jsonBody.getString("pan").toString();
            //TRACE.d(TRACE.NEW_LINE +  jsonBody.toString()+TRACE.NEW_LINE+TRACE.NEW_LINE);
            return jsonBody.toString();
        } catch (JSONException e) {
            TRACE.d("** ERROR JSON " +  TRACE.NEW_LINE + e.toString() );
            return e.toString();
        }
    }
    public String RevAmex(String _Track2,String _Counter,String _d4,String _redtarjeta,String _tipotarjeta,Integer _nip,String _entrada,String _pan,Integer _msi,String _deviceid,String _type_trans,String _propina,String _AID,String _ARQC,String _idamex,String _tarjetaTrack2,String _fechaTrack2,String _pinpan,Cursor cursor){
        JSONObject jsonBody = new JSONObject();
        try {
            TRACE.d("_Track2:"+TRACE.NEW_LINE +  _Track2);
            String p37 = new SimpleDateFormat("yyMMdd").format(Calendar.getInstance().getTime());
            jsonBody.put("mti", "1420");
            jsonBody.put("p2", _tarjetaTrack2);
            jsonBody.put("p3", "024000");
            jsonBody.put("p4", _d4);
            jsonBody.put("p11", _Counter);
            jsonBody.put("p14",_fechaTrack2);
            jsonBody.put("p19", "484");
            jsonBody.put("p22", p22(_entrada));
            jsonBody.put("p25", "1400");
            jsonBody.put("p26", cursor.getString(24));
            jsonBody.put("p32", "45678912345");
            jsonBody.put("p33", "45678912345");
            jsonBody.put("p37", p37+String.format("%06d", Integer.parseInt(_Counter)));
            jsonBody.put("p41", _deviceid.substring(_deviceid.length()-8,_deviceid.length()));
            jsonBody.put("p42", p42(_msi,_entrada));
            jsonBody.put("p49", "484");
            jsonBody.put("emv", "");
            jsonBody.put("idamex", _idamex);
            jsonBody.put("redtarj", redtarjeta(_redtarjeta,_pan.substring(0,1)));
            jsonBody.put("tipotarj", tipotarjeta( _tipotarjeta));
            jsonBody.put("pinPan", _pinpan);
            jsonBody.put("emisor", "AMEX");
            jsonBody.put("nip", String.valueOf(_nip));
            jsonBody.put("entrada", _entrada);
            jsonBody.put("pan",_pan );
            jsonBody.put("msi", _msi);
            jsonBody.put("deviceid",_deviceid);
            jsonBody.put("tipotxn",tipotxn(_type_trans));
            jsonBody.put("propina", propina(_type_trans,_propina));
            jsonBody.put("aid", _AID);
            jsonBody.put("arqc", ValidaArqc(_ARQC));
            _redtarj=jsonBody.getString("redtarj").toString();
            _tiptarj=jsonBody.getString("tipotarj").toString();
            _card=jsonBody.getString("pinPan").toString();
            emisor=jsonBody.getString("emisor").toString();
            nip=jsonBody.getString("nip").toString();
            entrada=jsonBody.getString("entrada").toString();
            pan=jsonBody.getString("pan").toString();
            //TRACE.d(TRACE.NEW_LINE +  jsonBody.toString()+TRACE.NEW_LINE+TRACE.NEW_LINE);
            return jsonBody.toString();
        } catch (JSONException e) {
            TRACE.d("** ERROR JSON " +  TRACE.NEW_LINE + e.toString() );
            return e.toString();
        }
    }
    public  String d18(String type_trans){
        if(type_trans.equals("checkout")||type_trans.equals("reautorizacion")||type_trans.equals("checkin")){
            return "7011";
        }else if(type_trans.equals("preventa")||type_trans.equals("cierrepreventa")){
            return "5812";
        }else{
            return "";
        }
    }
    public  String d22(String type_trans,String _d22){
        if(type_trans.equals("reautorizacion")){
            return "011";
        }else{
            return _d22;
        }
    }
    public  String drafcapture(String type_trans){
        if(type_trans.equals("preventa")||type_trans.equals("reautorizacion")||type_trans.equals("checkin")){
            return "0";
        }else{
            return "1";
        }
    }
    public  String amounts(String type_trans,String d4){
        if(type_trans.equals("ajuste")){
            return d4;
        }else{
            return "";
        }
    }
    public static String tipo(String type_trans){
        if(type_trans.equals("Cancelacion")){
            return TRANS_CAN_TYPE;
        }else if(type_trans.equals("devolucion")){
            return TRANS_DEV_TYPE;
        }else if(type_trans.equals("ajuste")){
            return TRANS_AJU_TYPE;
        }else if(type_trans.equals("reverso")){
            return TRANS_REV_TYPE;
        }else if(type_trans.equals("destino")){
            return TRANS_DES_TYPE;
        }else if(type_trans.equals("checkin")){
            return TRANS_CHE_TYPE;
        }else if(type_trans.equals("reautorizacion")){
            return TRANS_REA_TYPE;
        }else if(type_trans.equals("checkout")){
            return TRANS_OUT_TYPE;
        }else if(type_trans.equals("preventa")){
            return TRANS_PRE_TYPE;
        }else if(type_trans.equals("cierrepreventa")){
            return TRANS_CIE_TYPE;
        }else if(type_trans.equals("MSI")){
            return TRANS_MSI_TYPE;
        }else{
            return TRANS_VEN_TYPE;
        }
    }
    public String msi(Integer _msi){
        if(_msi>0 && _msi <10){
            return "! Q600006 000"+_msi+"03";
        }else if(_msi>9){
            return "! Q600006 00"+_msi+"03";
        }else {
            return "";
        }
    }
    public static String tipotxn(String type_trans){
        if (type_trans.equals("venta")){
            return "VN";
        }else if(type_trans.equals("MSI")) {
            return "MSI";
        }else if(type_trans.equals("devolucion")) {
            return "DEV";
        }else if(type_trans.equals("ajuste")) {
            return "AJU";
        }else if(type_trans.equals("reverso")){
            return "REV";
        }else if(type_trans.equals("destino")){
            return "DES";
        }else if(type_trans.equals("checkin")){
            return "CHE";
        }else if(type_trans.equals("reautorizacion")){
            return "REA";
        }else if(type_trans.equals("checkout")){
            return "OUT";
        }else if(type_trans.equals("preventa")){
            return "PRE";
        }else if(type_trans.equals("cierrepreventa")){
            return "CIE";
        }else{
            return "CAN";
        }
    }
    public String propina(String type_trans,String propina){
        if(type_trans.equals("MSI")||type_trans.equals("devolucion")||type_trans.equals("ajuste")||type_trans.equals("reverso")||type_trans.equals("destino")||type_trans.equals("checkin")||type_trans.equals("reautorizacion")||type_trans.equals("checkout")||type_trans.equals("preventa")||type_trans.equals("cierrepreventa")) {
            return "0.00";
        }else{
            return propina;
        }
    }
    public String tipotarjeta(String _tipo){
        if (_tipo.equals("credit")){
            return "Crédito";
        }else if (_tipo.equals("debit")) {
            return "Débito";
        }else  {
            return _tipo;
        }
    }
    public String redtarjeta(String _red,String _pan){
        //TRACE.d("original: "+_red+" original: "+_pan);
        if (_red==""){
            if (Integer.parseInt(_pan)>=4 && Integer.parseInt(_pan)<5){
                return "Visa";
            }else{
                return "MC";
            }
        }else{
            //TRACE.d("redtarjeta original: "+_red);
            if(_red.equals("visa")){
                return _red.substring(0, 1).toUpperCase() + _red.substring(1);
            }else if(_red.equals("mastercard"))  {
                return _red.substring(0, 1).toUpperCase() + _red.substring(6,7).toUpperCase();
            }
            else{
                return _red;
            }
        }
    }
    public String tagtipotarjeta(String tag50,String tag9f12){
        //TRACE.d("tag50 : " + tag50.toString());
        //TRACE.d("tag9f12 : " + tag9f12.toString());
        if (tag50.contains("DEBIT")||tag9f12.contains("DEBIT")){
            return "Débito";
        }else if (tag50.contains("CREDIT")||tag9f12.contains("CREDIT")){
            return "Crédito";
        }else{
            return "Desconocido";
        }
    }
    public String tagredtarjeta(String tag50,String tag9f12){
        //TRACE.d("tag50 : " + tag50.toString());
        //TRACE.d("tag9f12 : " + tag9f12.toString());
        if (tag50.contains("VISA")||tag9f12.contains("VISA")){
            return "Visa";
        }else if (tag50.contains("MASTER")||tag9f12.contains("MASTER")){
            return "MC";
        }else{
            return "Desconocido";
        }
    }
    public String MascaraTrack2(String track2,String interfaz){
        TRACE.d("track2 original : " + track2.toString());
        if(interfaz.equals("Agregador"))
        {
            track2= String.format("%"+-48+"s",track2.toUpperCase(Locale.ROOT)).replace(" ","F");
        }else{
            track2= track2.toUpperCase(Locale.ROOT).replace("D","=").replace("F","");
        }
        TRACE.d("track2 final : " + track2.toString());
        return track2;
    }
    public String CountTrack2(String track2){
        track2=track2.toUpperCase(Locale.ROOT).replace("F","");
        return String.valueOf(track2.length());
    }
    public static String hexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        char[] hexData = hex.toCharArray();
        for (int count = 0; count < hexData.length - 1; count += 2) {
            int firstDigit = Character.digit(hexData[count], 16);
            int lastDigit = Character.digit(hexData[count + 1], 16);
            int decimal = firstDigit * 16 + lastDigit;
            sb.append((char) decimal);
        }
        return sb.toString().toUpperCase(Locale.ROOT);
    }
    public static String Amount_msi(String _total, String _msi){
        Float _amount = Float.parseFloat(_total.replace("$","").replace(",","").replace(" ",""));
        Float total_msi= _amount / Integer.parseInt(_msi);
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        return  format.format(total_msi).replace("$","$ ");
    }
    public String ValidaArqc(String _arqc){
        if (_arqc=="N/A")
        {
            _arqc= GenArqc(16);
        }
        return _arqc;
    }
    public String redtarjetaamex(String redtarjeta){
        if (redtarjeta.equals("NA"))
        {
            return "AMEX";
        }else  {
            return redtarjeta;
        }
    }
    public String tipotarjetaamex(String tipotarjeta){
        if (tipotarjeta.equals("NA"))
        {
            return "Crédito";
        }else  {
            return tipotarjeta;
        }
    }
    public String GenArqc(int longitud){
        String terminalTime = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        String CHARACTERS ="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"+terminalTime;
        Random random = new Random();
        String contrasenia = "";
        for (int i = 0; i < longitud; i++){
            contrasenia += CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
        }
        return contrasenia.toUpperCase(Locale.ROOT);
    }
    public String panTrack2Amex(String pan){
        int tamtrack2=pan.indexOf("D");
        TRACE.d("pan Amex:"+pan);
        return pan.substring(tamtrack2-tamtrack2,7)+"****"+pan.substring(tamtrack2-4,tamtrack2);
    }
    public String panTrack2Prosa(String pan){
        int tamtrack2=pan.indexOf("D");
        TRACE.d("pan Prosa:"+pan);
        return pan.substring(tamtrack2-tamtrack2,8)+"XXXX"+pan.substring(tamtrack2-4,tamtrack2);
    }
    public String MascaraTrack2(String track2){
        track2= track2.toUpperCase(Locale.ROOT).replace("D","=").replace("F","");
        return track2;
    }
    public String tarjetaTrack2(String pan){
        int tamtrack2=pan.indexOf("D");
        return pan.substring(0,tamtrack2);
    }
    public String fechaTrack2(String pan){
        int tamtrack2=pan.indexOf("D");
        return pan.substring(tamtrack2+1,tamtrack2+5);
    }
    public String pinpanTrack2(String pan){
        int tamtrack2=pan.indexOf("D");
        return pan.substring(tamtrack2-4,tamtrack2);
    }
    public String p22(String entrada)
    {
        if(entrada.equals("MCR"))
        {
            return "511101255124";
        }else if(entrada.equals("NFC")){
            return "51110X555124";
        }else{
            return "511101511324";
        }
    }
    public String p42(Integer _msi,String entrada)
    {
        // && entrada!="MCR"
        if(_msi==0 )
        {
            if(Build.MODEL.equals("D30")||Build.MODEL.equals("D20")||Build.MODEL.equals("D60"))
            {
                return "7163170335     ";
            }else{
                return "7163170335     ";
            }
        }else{
            return "7163170335     ";
        }
    }
    public String msip48(Integer _msi){
        if(_msi>0 && _msi <10){
            return "030"+_msi;
        }else if(_msi>9){
            return "03"+_msi;
        }else {
            return "";
        }
    }
    public String getcalltransaction(String tipo) {
        if (tipo.equals("Amex")){
            TRACE.d("getcalltransactionamex: ");
            return Utils.TERMINAL_AMEX + "/amex/tpv/txn1100";
        }else if (tipo.equals("Prosa")){
            TRACE.d("getcalltransactionprosa: ");
            return Utils.TERMINAL_API + "/matriz/certificacion/iso/gral";
        }else{
            return "";
        }
    }
    public String getcallvalida(String tipo) {
        if (tipo.equals("Amex")){
            TRACE.d("getcallvalidaamex: ");
            return Utils.TERMINAL_AMEX + "/amex/tpv/transaccion";
        }else if (tipo.equals("Prosa")){
            TRACE.d("getcallvalidaprosa: ");
            return Utils.TERMINAL_API + "/efevoo/tpv/transaccion";
        }else{
            return "";
        }
    }
}
