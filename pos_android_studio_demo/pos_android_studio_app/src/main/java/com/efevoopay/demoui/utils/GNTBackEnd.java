package com.efevoopay.demoui.utils;

import com.blumonpay.capx.functions.CypherFunctions;
import com.blumonpay.capx.model.DUKPTData;
import com.blumonpay.capx.model.TransactionData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class GNTBackEnd {
    public String _redtarj="";
    public String _tiptarj="";
    public String _card="";
    public DUKPTData EncryptBlumon(String _track2, Integer _counter) {
        TransactionData tr = new TransactionData();
        CypherFunctions cy = new CypherFunctions();
        DUKPTData dukpt = new DUKPTData();
        /*String tr_key = "C5BFFC5E6551D64F62E3D80F6A3126F8";
        String tr_ksn = "00000111855052200001";
        String tr_tk = "FE2B1B3A367E54A7E21E6E24E13E3849";*/
        String tr_ksn = "00000141549406200001";
        String tr_key = "F0EA461D2876C8476F8A9AC245ED6FDE";
        String tr_tk = "3CE9F2A75EF203993CCFA854A508F590";

        String track2 = _track2.toUpperCase(Locale.ROOT);
        TRACE.d(TRACE.NEW_LINE + "track2" + TRACE.NEW_LINE + track2+TRACE.NEW_LINE);
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
    public String transaccion(String _entrada,String _entrymode,String _pinpan,String _Track2,String _crc32,String _ksn,String _Counter,String _d4,String _emv,Integer _msi,String _pan,String _deviceid,String _redtarjeta,String _tipotarjeta,String _propina,String _type_trans,String _time_txn,String _p11,String _AID,String _ARQC){
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("deviceid",_deviceid);
            jsonBody.put("entrada", _entrada);
            jsonBody.put("tipo",tipo(_type_trans));
            jsonBody.put("track2", _Track2);
            jsonBody.put("crc32", _crc32);
            jsonBody.put("count", _Counter);
            jsonBody.put("ksn", _ksn);
            jsonBody.put("tamtrack2", "37");
            jsonBody.put("pinPan", _pinpan);
            jsonBody.put("emv", _emv);
            jsonBody.put("d4", _d4);
            jsonBody.put("d18", "");
            jsonBody.put("d22", _entrymode+"1");
            jsonBody.put("d95", "");
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
            _redtarj=jsonBody.getString("redtarj").toString();
            _tiptarj=jsonBody.getString("tipotarj").toString();
            _card=jsonBody.getString("pinPan").toString();
            TRACE.d(TRACE.NEW_LINE +  jsonBody.toString()+TRACE.NEW_LINE+TRACE.NEW_LINE);
            return jsonBody.toString();
        } catch (JSONException e) {
            TRACE.d("** ERROR JSON " +  TRACE.NEW_LINE + e.toString() );
            return e.toString();
        }
    }
    public  String tipo(String type_trans){
        if(type_trans.equals("Cancelacion")){
            return "CAN";
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
        }
        else if(type_trans.equals("msi")) {
            return "MSI";
        }else{
            return "CAN";
        }
    }
    public String propina(String type_trans,String propina){
        if(type_trans.equals("msi")||type_trans.equals("Cancelacion")) {
            return "0.00";
        }else{
            return propina;
        }
    }
    public String tipotarjeta(String _tipo){
        if (_tipo.equals("credit")){
            return "Credito";
        }else if (_tipo.equals("debit")) {
            return "Debito";
        }else  {
            return _tipo;
        }
    }
    public String redtarjeta(String _red,String _pan){
        TRACE.d("original: "+_red+" original: "+_pan);
        if (_red==""){
            if (Integer.parseInt(_pan)>=4 && Integer.parseInt(_pan)<5){
                return "Visa";
            }else{
                return "MC";
            }
        }else{
            TRACE.d("redtarjeta original: "+_red);
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
        TRACE.d("tag50 : " + tag50.toString());
        TRACE.d("tag9f12 : " + tag9f12.toString());
        if (tag50.contains("DEBIT")||tag9f12.contains("DEBIT")){
            return "Debito";
        }else if (tag50.contains("CREDIT")||tag9f12.contains("CREDIT")){
            return "Credito";
        }else{
            return "Desconocido";
        }
    }
    public String tagredtarjeta(String tag50,String tag9f12){
        TRACE.d("tag50 : " + tag50.toString());
        TRACE.d("tag9f12 : " + tag9f12.toString());
        if (tag50.contains("VISA")||tag9f12.contains("VISA")){
            return "Visa";
        }else if (tag50.contains("MASTER")||tag9f12.contains("MASTER")){
            return "MC";
        }else{
            return "Desconocido";
        }
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
