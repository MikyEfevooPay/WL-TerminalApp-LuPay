package com.efevoopay.demoui.activities;

import android.app.ProgressDialog;
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
import com.efevoopay.demoui.utils.Utils;
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
    ProgressDialog spinner;

    private String ksn_posId;
    private WMX_llamada_dukpt jsondukpt=new WMX_llamada_dukpt();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.switch_title_logo("Cancelaciones");
        recyclerView = findViewById(R.id.historial_cancelaciones_List);
        cancellation_empty_layout = findViewById((R.id.layout_cancellation_empty));
        intent = getIntent();
        ksn_posId = intent.getStringExtra("ksn_posId");
        spinner = Utils.getLoaderSpinner(this);

        readJsontxn();
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
        intent.putExtra("aid", transactions.get(position).get_aid());
        intent.putExtra("arqc", transactions.get(position).get_arqc());
        intent.putExtra("approve", transactions.get(position).get_approve());
        intent.putExtra("ksn_posId",ksn_posId);

        startActivity(intent);
    }

    public void setItems() {
        if(transactions.size() > 0) {
            cancellation_empty_layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            CancelacionesItemAdapter transactionItemAdapter = new CancelacionesItemAdapter(this,transactions, this);
            recyclerView.setAdapter(transactionItemAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            cancellation_empty_layout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }


    private void getHistorial(String _devicesid)throws IOException{
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = Utils.TERMINAL_API + "/matriz/certificacion/Dukptnumtxn";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("deviceid", _devicesid);
            jsonBody.put("pantalla", "Cancelacion");
            jsonBody.put("fechainicio", "");
            jsonBody.put("fechafinal", "");
            final String requestBody = jsonBody.toString();
            TRACE.d("requestBody " +  TRACE.NEW_LINE + requestBody );
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    jsondukpt.readJsonnew(response.toString());
                    if(spinner.isShowing()) spinner.dismiss();
                    TRACE.d("** ResponseResult " +  TRACE.NEW_LINE + response.toString() );
                    setItems();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + error.toString() );
                    if(spinner.isShowing()) spinner.dismiss();
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

    public void readJsontxn(){
        try {
            spinner.show();
            getHistorial(ksn_posId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
