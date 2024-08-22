package com.cohetepay.demoui.activities;

import com.cohetepay.demoui.utils.CorteCaja;
import com.cohetepay.demoui.utils.TRACE;
import com.cohetepay.demoui.utils.Transaction;
import com.cohetepay.demoui.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WMX_llamada_dukpt {
    ArrayList<Transaction> transactions = new ArrayList<>();
    ArrayList<CorteCaja> cortecaja = new ArrayList<>();
    JSONObject objectcorte;
    public String total, subtotal, tip;
    public void readJsonnew(String _json){
       //if(this.transactions.size() > 0) this.transactions.clear();
        try {
            JSONArray object = new JSONArray(_json);
            for (int i = 0; i < object.length(); i++) {
                JSONObject object1 = object.getJSONObject(i);
                JSONObject data =new  JSONObject(object1.getString("txn").toString());
                //TRACE.d("data" +  TRACE.NEW_LINE + data.toString());
                if(!data.getString("tipotxn").equals("A")){
                    Transaction _data = new Transaction(
                            Utils.tryIntParse(data.getString("id")),
                            data.getString("noAuth"),
                            data.getString("date"),
                            data.getString("hour"),
                            data.getString("subtotal"),
                            data.getString("pan"),
                            data.getString("redtarj"),
                            data.getString("tipotarj"),
                            data.getString("tipotxn"),
                            data.getString("propina"),
                            data.getString("total"),
                            data.getString("msi"),
                            data.getString("aid"),
                            data.getString("arqc"),
                            data.getString("numref"),
                            data.getString("emisor"),
                            data.getString("nip"),
                            data.getString("entrada"),
                            data.getString("datehour"));
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
            if(object.has("corte")){
                JSONArray array = new JSONArray(object.getString("corte"));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    //JSONObject data =new  JSONObject(object1.getString("corte").toString());
                    TRACE.d("data: " +  TRACE.NEW_LINE + object1.toString() + " " + array.length() + " " + i);
                    CorteCaja _data = new CorteCaja().setHistoricDetails(object1.getString("idCorte"),
                            object1.getString("Identificador"),
                            object1.getString("total"),
                            object1.getString("FechaHora"),
                            object1.getString("subtotal"),
                            object1.getString("propina"));
                    TRACE.d("CAJA: " + _data);
                    this.cortecaja.add(_data);
                }
            }
            TRACE.d("transaccion" +  TRACE.NEW_LINE + cortecaja.toArray().length);
        } catch (JSONException e) {
            e.printStackTrace();
            TRACE.d("Error: " + e.getMessage());
        }
    }
    public void finalcortecaja(String _json){
        if(this.cortecaja.size() > 0) this.cortecaja.clear();
        try {
            objectcorte=new JSONObject(_json);
            if(objectcorte.has("corte")){
                JSONArray array = new JSONArray(objectcorte.getString("corte").toString());
                //TRACE.d("array:" +  TRACE.NEW_LINE + array.toString());
                total=objectcorte.has("total") ?  objectcorte.getString("total") : "";
                subtotal=objectcorte.has("subtotal") ?  objectcorte.getString("subtotal") : "";
                tip=objectcorte.has("propina") ? objectcorte.getString("propina") : "";
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
