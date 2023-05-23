package com.efevoopay.demoui.utils;

import android.database.Cursor;
import android.os.Build;

import com.blumonpay.capx.functions.CypherFunctions;
import com.blumonpay.capx.model.DUKPTData;
import com.blumonpay.capx.model.TransactionData;
import com.efevoopay.demoui.activities.WMX_Ajustes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class GNTBackEnd {
    public String _redtarj="";
    public String _tiptarj="";
    public String _card="";
    public DUKPTData EncryptBlumon(String _track2, Integer _counter, Cursor cursor) {
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
        Integer tr_counter = _counter;

        tr.setKey(tr_key);
        tr.setKsn(tr_ksn);
        tr.setTk(tr_tk);
        tr.setTrack1("");
        tr.setTrack2(track2);
        tr.setCounter(tr_counter);


        TRACE.d(TRACE.NEW_LINE + TRACE.NEW_LINE + "TransactionData" + TRACE.NEW_LINE + tr.toString()+TRACE.NEW_LINE+TRACE.NEW_LINE);
        try {
            dukpt = cy.encryptDUKPT(tr);

            TRACE.d(TRACE.NEW_LINE + TRACE.NEW_LINE + "dukpt" + TRACE.NEW_LINE + dukpt.toString()+TRACE.NEW_LINE+TRACE.NEW_LINE);
        }catch (Throwable t){
            TRACE.d("Throwable" + t.toString());
        }
        return  dukpt;
    }
    public String transaccion(String _entrada,String _entrymode,String _pinpan,String _Track2,String _crc32,String _ksn,String _Counter,String _d4,String _emv,Integer _msi,String _pan,String _deviceid,String _redtarjeta,String _tipotarjeta,String _propina,String _type_trans,String _time_txn,String _p11,String _AID,String _ARQC,String _tamtrack2, Cursor cursor){
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("tpv", Build.MODEL+"Android smart POS");
            jsonBody.put("deviceid",_deviceid);
            jsonBody.put("entrada", _entrada);
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
            jsonBody.put("arqc", _ARQC);
            jsonBody.put("drafcapture", drafcapture(_type_trans));
            jsonBody.put("p43", cursor.getString(5));
            jsonBody.put("p48", cursor.getString(6));
            jsonBody.put("p120", cursor.getString(7));
            _redtarj=jsonBody.getString("redtarj").toString();
            _tiptarj=jsonBody.getString("tipotarj").toString();
            _card=jsonBody.getString("pinPan").toString();
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
    public  String tipo(String type_trans){
        if(type_trans.equals("Cancelacion")){
            return "CAN";
        }else if(type_trans.equals("devolucion")){
            return "DEV";
        }else if(type_trans.equals("ajuste")){
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
            return "VEN";
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
    public String tipotxn(String type_trans){
        if (type_trans.equals("venta")){
            return "VN";
        }else if(type_trans.equals("msi")) {
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
        if(type_trans.equals("msi")||type_trans.equals("Cancelacion")||type_trans.equals("devolucion")||type_trans.equals("ajuste")||type_trans.equals("reverso")||type_trans.equals("destino")||type_trans.equals("checkin")||type_trans.equals("reautorizacion")||type_trans.equals("checkout")||type_trans.equals("preventa")||type_trans.equals("cierrepreventa")) {
            return "0.00";
        }else{
            return propina;
        }
    }
    public String tipotarjeta(String _tipo){
        if (_tipo.equals("credit")){
            return "Crédito";
        }else if (_tipo.equals("debit")) {
            return "Debito";
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
            return "Debito";
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
    public String MascaraTrack2(String track2){
        //TRACE.d("track2 original : " + track2.toString());
        track2= String.format("%"+-48+"s",track2.toUpperCase(Locale.ROOT)).replace(" ","F");
        //TRACE.d("track2 final : " + track2.toString());
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
}
