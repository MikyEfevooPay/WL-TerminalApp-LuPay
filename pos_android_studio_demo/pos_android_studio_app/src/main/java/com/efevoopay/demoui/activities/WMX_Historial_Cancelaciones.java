package com.efevoopay.demoui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.efevoopay.demoui.R;
import com.efevoopay.demoui.interfaces.TransactionsViewInterface;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Transaction;
import com.efevoopay.demoui.widget.CancelacionesItemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WMX_Historial_Cancelaciones extends BaseActivity implements View.OnClickListener, TransactionsViewInterface {
    RecyclerView recyclerView;
    LinearLayout cancellation_empty_layout;
    ArrayList<Transaction> transactions = new ArrayList<>();
    Intent intent;
    private String ksn_posId;
    private WMX_llamada_dukpt jsondukpt=new WMX_llamada_dukpt();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.switch_title_logo("Cancelaciones");

        intent = getIntent();
        ksn_posId = intent.getStringExtra("ksn_posId");


//        cancellation_empty_layout = findViewById((R.id.layout_cancellation_empty));

        try {
            readJsontxn();
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setItems();

    }
    @Override
    public void onClick(View view) {

    }

    @Override
    public void onToolbarLinstener() {
        onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_historial_cancelaciones;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(WMX_Historial_Cancelaciones.this, WMX_Cancelacion_Desc.class);
        intent.putExtra("auth", transactions.get(position).get_auth());
        intent.putExtra("date", transactions.get(position).get_date2());
        intent.putExtra("time", transactions.get(position).get_time());
        intent.putExtra("amount", transactions.get(position).get_amount2());
        intent.putExtra("card", transactions.get(position).get_card());
        intent.putExtra("redtarj", transactions.get(position).get_redtarj());
        intent.putExtra("tipotarj", transactions.get(position).get_tipotarj());
        intent.putExtra("status", transactions.get(position).get_tipotxn());
        intent.putExtra("propina", transactions.get(position).get_propina());
        intent.putExtra("total", transactions.get(position).get_total());
        intent.putExtra("msi", transactions.get(position).get_msi());
        intent.putExtra("approve", transactions.get(position).get_approve());
        intent.putExtra("ksn_posId",ksn_posId);

        startActivity(intent);
    }

    public void setItems() {
//        int length = readJson();
//        if(length > 0) {
//            cancellation_empty_layout.setVisibility(View.GONE);
            recyclerView = findViewById(R.id.historial_cancelaciones_List);
            recyclerView.setVisibility(View.VISIBLE);
            CancelacionesItemAdapter transactionItemAdapter = new CancelacionesItemAdapter(this,transactions, this);
            recyclerView.setAdapter(transactionItemAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        } else {
//            recyclerView.setVisibility(View.GONE);
//        }
    }

    /*public int readJson(){
        try {
            JSONArray jsonArray = new JSONArray(JsonDataFromAsset());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = jsonArray.getJSONObject(i);
                if(!data.getString("tipo").equals("A")){
                    Transaction _data = new Transaction(
                            data.getString("noAuth"),
                            data.getString("date"),
                            data.getString("hour"),
                            data.getString("amount"),
                            data.getString("pan"),
                            data.getString("procesador"),
                            data.getString("tipo"),
                            data.getString("approve"));
                    transactions.add(_data);
                }

            }
            return jsonArray.length();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }*/

    /*private String JsonDataFromAsset() throws IOException{
        String json =null;
        try{
            InputStream inputStream = getAssets().open("dataDummy2.json");
            int sizeOfFile = inputStream.available();
            byte[] bufferData =  new byte[sizeOfFile];
            inputStream.read(bufferData);
            inputStream.close();
            json = new String(bufferData, "UTF-8");
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }*/
    private void getHistorial(String _devicesid)throws IOException{
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://wmx-iso-apps1.eba-9vhqtwgu.us-west-2.elasticbeanstalk.com/matriz/certificacion/Dukptnumtxn";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("deviceid", _devicesid);
            jsonBody.put("pantalla", "Cancelacion");
            final String requestBody = jsonBody.toString();
            TRACE.d("requestBody " +  TRACE.NEW_LINE + requestBody );
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    jsondukpt.readJsonnew(response.toString());
                    //TRACE.d("** ResponseResult " +  TRACE.NEW_LINE + response.toString() );
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + error.toString() );
                    WMX_Historial_Cancelaciones.super.showAlert("ERROR", error.toString());
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
            transactions=jsondukpt.transactions;
            requestQueue.add(stringRequest);
        } catch (JSONException e) {

            TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + e.toString() );

        }


    }
    /*private void readJsonnew(String _json){
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
                            data.getString("numref"));
                    this.transactions.add(_data);
                }
            }
            //TRACE.d("transaccion" +  TRACE.NEW_LINE + transactions.toArray().length);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
    public void readJsontxn(){
        try {
            getHistorial(ksn_posId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
