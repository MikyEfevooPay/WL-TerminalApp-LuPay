package com.efevoopay.demoui.activities;

import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WMX_llamada_dukpt {
    ArrayList<Transaction> transactions = new ArrayList<>();
    public void readJsonnew(String _json){
        try {
            JSONArray object = new JSONArray(_json);
            for (int i = 0; i < object.length(); i++) {
                JSONObject object1 = object.getJSONObject(i);
                JSONObject data =new  JSONObject(object1.getString("txn").toString());
                TRACE.d("data" +  TRACE.NEW_LINE + data.toString());
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
}
