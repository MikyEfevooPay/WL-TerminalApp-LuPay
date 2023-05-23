package com.efevoopay.demoui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;
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
import com.efevoopay.demoui.interfaces.HistorialCorteCajaViewInterface;
import com.efevoopay.demoui.interfaces.TransactionsViewInterface;
import com.efevoopay.demoui.utils.CorteCaja;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Utils;
import com.efevoopay.demoui.widget.CancelacionesItemAdapter;
import com.efevoopay.demoui.widget.CorteCajaItemAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class WMX_Historial_CorteCaja extends BaseActivity implements View.OnClickListener, HistorialCorteCajaViewInterface {
    ProgressDialog spinner;
    RecyclerView recyclerView;
    LinearLayout cortecaja_empty_layout;
    Intent intent;
    ArrayList<CorteCaja> cortecaja = new ArrayList<>();
    Button btn_hacercorte;
    private WMX_llamada_dukpt jsondukpt=new WMX_llamada_dukpt();
    private String ksn_posId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.switch_title_logo("Corte de caja");
        recyclerView = findViewById(R.id.historial_cortecaja_List);
        cortecaja_empty_layout = findViewById((R.id.layout_cortecaja_empty));
        intent = getIntent();
        ksn_posId = intent.getStringExtra("ksn_posId");
        btn_hacercorte =  (Button) findViewById(R.id.btn_hacercortecaja);
        btn_hacercorte.setOnClickListener(this);

        spinner = Utils.getLoaderSpinner(this);

        readjsonhistorialcortecaja();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_hacercortecaja:
                Intent intent = new Intent(WMX_Historial_CorteCaja.this, WMX_Final_CorteCaja.class);
                intent.putExtra("ksn_posId", ksn_posId);
                startActivity(intent);
                break;
            default:

        }
    }

    @Override
    public void onToolbarLinstener() {
        onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_historial_cortecaja;
    }

    @Override
    public void onItemClick(int position) {

    }

    public void setItems() {
        if(cortecaja.size() > 0) {
            cortecaja_empty_layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            CorteCajaItemAdapter cortecajaItemAdapter = new CorteCajaItemAdapter(this,cortecaja, this);
            recyclerView.setAdapter(cortecajaItemAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            cortecaja_empty_layout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void readjsonhistorialcortecaja(){
        try {
            spinner.show();
            getHistorial(ksn_posId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void getHistorial(String _devicesid)throws IOException{
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = Utils.TPVCONFIG + "/apiv0/agrs/corte/crud";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("snTerminal", _devicesid);
            jsonBody.put("operacion", "H");
            final String requestBody = jsonBody.toString();
            TRACE.d("requestBody " +  TRACE.NEW_LINE + requestBody );
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    jsondukpt.historialcortecaja(response.toString());
                    if(spinner.isShowing()) spinner.dismiss();
                    TRACE.d("** ResponseResult " +  TRACE.NEW_LINE + response.toString() );
                    setItems();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + error.toString() );
                    if(spinner.isShowing()) spinner.dismiss();
                    WMX_Historial_CorteCaja.super.showAlert("error", error.toString());
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
            cortecaja=jsondukpt.cortecaja;
            requestQueue.add(stringRequest);
        } catch (JSONException e) {

            TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + e.toString() );

        }
    }
}
