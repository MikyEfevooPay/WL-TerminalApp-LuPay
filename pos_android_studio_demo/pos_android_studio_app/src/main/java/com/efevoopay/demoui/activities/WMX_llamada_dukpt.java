package com.efevoopay.demoui.activities;

import com.efevoopay.demoui.utils.CorteCaja;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WMX_llamada_dukpt {
    ArrayList<Transaction> transactions = new ArrayList<>();
    ArrayList<CorteCaja> cortecaja = new ArrayList<>();
    JSONObject objectcorte;
    public String total;
    public void readJsonnew(String _json){
       if(this.transactions.size() > 0) this.transactions.clear();
        try {
            JSONArray object = new JSONArray(_json);
            for (int i = 0; i < object.length(); i++) {
                JSONObject object1 = object.getJSONObject(i);
                JSONObject data =new  JSONObject(object1.getString("txn").toString());
                //TRACE.d("data" +  TRACE.NEW_LINE + data.toString());
                if(!data.getString("tipotxn").equals("A")){
                    Transaction _data = new Transaction(
                            data.getString("noAuth"),
                            data.getString("date"),
                            data.getString("hour"),
                            data.getString("amount"),
                            data.getString("pan"),
                            data.getString("redtarj"),
                            data.getString("tipotarj"),
                            data.getString("tipotxn"),
                            data.getString("propina"),
                            data.getString("total"),
                            data.getString("msi"),
                            data.getString("aid"),
                            data.getString("arqc"),
                            data.getString("numref"));
                    this.transactions.add(_data);
                }
            }
            //TRACE.d("transaccion" +  TRACE.NEW_LINE + transactions.toArray().length);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void historialcortecaja(String _json){
        if(this.cortecaja.size() > 0) this.cortecaja.clear();
        try {
            JSONObject object = new JSONObject(_json);
            //TRACE.d("object:" +  TRACE.NEW_LINE + object.toString());
            if(object.has("corte")){
                JSONArray array = new JSONArray(object.getString("corte").toString());
                //TRACE.d("array:" +  TRACE.NEW_LINE + array.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    //JSONObject data =new  JSONObject(object1.getString("corte").toString());
                    //TRACE.d("data" +  TRACE.NEW_LINE + object1.toString());
                    CorteCaja _data = new CorteCaja(
                            object1.getString("idCorte"),
                            object1.getString("Identificador"),
                            object1.getString("Cantidad"),
                            object1.getString("FechaHora"));
                    this.cortecaja.add(_data);
                }
            }
            //TRACE.d("transaccion" +  TRACE.NEW_LINE + transactions.toArray().length);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void finalcortecaja(String _json){
        if(this.cortecaja.size() > 0) this.cortecaja.clear();
        try {
            objectcorte=new JSONObject(_json);
            //JSONObject objectcorte = new JSONObject(_json);
            //TRACE.d("object:" +  TRACE.NEW_LINE + object.toString());
            if(objectcorte.has("corte")){
                JSONArray array = new JSONArray(objectcorte.getString("corte").toString());
                //TRACE.d("array:" +  TRACE.NEW_LINE + array.toString());
                total=objectcorte.getString("total").toString();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    //JSONObject data =new  JSONObject(object1.getString("corte").toString());
                    //TRACE.d("data" +  TRACE.NEW_LINE + object1.toString());
                    CorteCaja _data = new CorteCaja(
                            object1.getString("reqdukpt_id"),
                            object1.getString("device"),
                            object1.getString("numtxn"),
                            object1.getString("monto"),
                            object1.getString("date"),
                            object1.getString("hora"),
                            object1.getString("pan"));
                    this.cortecaja.add(_data);
                }
            }
            //TRACE.d("transaccion" +  TRACE.NEW_LINE + transactions.toArray().length);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
