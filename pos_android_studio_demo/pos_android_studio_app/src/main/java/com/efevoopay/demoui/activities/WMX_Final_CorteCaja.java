package com.efevoopay.demoui.activities;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
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
import com.efevoopay.demoui.utils.CorteCaja;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Utils;
import com.efevoopay.demoui.widget.FinalCorteCajaItemAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WMX_Final_CorteCaja extends BaseActivity implements View.OnClickListener, HistorialCorteCajaViewInterface {
    ProgressDialog spinner;
    RecyclerView recyclerView;
    LinearLayout finalcortecaja_empty_layout;
    private Context mContext;
    Intent intent;
    ArrayList<CorteCaja> cortecaja = new ArrayList<>();
    Button btn_finalcortecaja;
    private TextView txt_totalamount, txt_datetime;
    private WMX_llamada_dukpt jsondukpt = new WMX_llamada_dukpt();
    ProgressDialog loader;
    private String ksn_posId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setStatusBarColor(Color.WHITE);
        super.setToolbarBgColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.switch_title_logo("Corte de caja");
        recyclerView = findViewById(R.id.final_cortecaja_List);
        finalcortecaja_empty_layout = findViewById((R.id.layout_finalcortecaja_empty));
        intent = getIntent();
        ksn_posId = intent.getStringExtra("ksn_posId");
        loader = Utils.getLoaderSpinner(this, "Enviando...");
        btn_finalcortecaja = (Button) findViewById(R.id.btn_finalcortecaja);
        btn_finalcortecaja.setOnClickListener(this);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh:mm");
        Date date = new Date();
        spinner = Utils.getLoaderSpinner(this);

        txt_totalamount = (TextView) findViewById(R.id.txt_totalamount);
        txt_datetime = (TextView) findViewById(R.id.txt_datetime);
        txt_datetime.setText(dateFormat.format(date).toString());

        readjsonfinalcortecaja();

        mContext = this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finalcortecaja:
                ConfirmarCorte(ksn_posId);
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
        return R.layout.wmx_final_cortecaja;
    }

    @Override
    public void onItemClick(int position) {

    }

    public void setItems() {
        if (cortecaja.size() > 0) {
            txt_totalamount.setText(jsondukpt.total);
            btn_enable(true);
            finalcortecaja_empty_layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            FinalCorteCajaItemAdapter finalcortecajaItemAdapter = new FinalCorteCajaItemAdapter(this, cortecaja, this);
            recyclerView.setAdapter(finalcortecajaItemAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            txt_totalamount.setText("$0.00 mxn");
            btn_enable(false);
            finalcortecaja_empty_layout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void readjsonfinalcortecaja() {
        try {
            spinner.show();
            getFinal(ksn_posId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btn_enable(Boolean bnd) {
        if (bnd) {
            btn_finalcortecaja.setEnabled(true);
        } else {
            btn_finalcortecaja.setEnabled(false);
            btn_finalcortecaja.setAlpha(Float.parseFloat("0.5"));
        }

    }

    private void getFinal(String _devicesid) throws IOException {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = Utils.TPVCONFIG + "/apiv0/agrs/corte/crud";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("snTerminal", _devicesid);
            jsonBody.put("operacion", "D");
            jsonBody.put("idCorte", "0");
            final String requestBody = jsonBody.toString();
            TRACE.d("requestBody " + TRACE.NEW_LINE + requestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    jsondukpt.finalcortecaja(response);
                    if (spinner.isShowing())
                        spinner.dismiss();
                    TRACE.d("** ResponseResult " + TRACE.NEW_LINE + response.toString());
                    setItems();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    TRACE.d("** ResponseResult ERROR " + TRACE.NEW_LINE + error.toString());
                    if (spinner.isShowing())
                        spinner.dismiss();
                    WMX_Final_CorteCaja.super.showAlert("error", error.toString());
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
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody,
                                "utf-8");
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
            cortecaja = jsondukpt.cortecaja;
            requestQueue.add(stringRequest);
        } catch (JSONException e) {

            TRACE.d("** ResponseResult ERROR " + TRACE.NEW_LINE + e.toString());

        }
    }

    private void ViewTicket() throws JSONException {
        intent = new Intent(this, WMX_Final_CorteCaja_Ticket.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Intent intent = new Intent(mContext, WMX_Final_CorteCaja_Ticket.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("ksn_posId", ksn_posId);
        intent.putExtra("totalamount", jsondukpt.total);
        intent.putExtra("tip", jsondukpt.tip);
        intent.putExtra("corte", jsondukpt.subtotal);
        intent.putExtra("fechaCorte", txt_datetime.getText().toString());
        intent.putExtra("tablerows", jsondukpt.objectcorte.getString("corte"));
        startActivityMiddleware(intent);
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
    private void ConfirmarCorte(String _devicesid) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = Utils.TPVCONFIG + "/apiv0/agrs/corte/crud";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("snTerminal", _devicesid);
            jsonBody.put("operacion", "C");
            jsonBody.put("idCorte", "0");
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    TRACE.d("** ResponseResult " + TRACE.NEW_LINE + response.toString());
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("code").toString().equals("00")) {
                            WMX_Final_CorteCaja.super.showAlert("success", "¡Corte de caja realizado con éxito!");
                            btn_enable(false);
                            ViewTicket();
                        } else {
                            WMX_Final_CorteCaja.super.showAlert("error", "¡Corte de caja no exitoso!");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    TRACE.d("** ResponseVolleyError  " + TRACE.NEW_LINE + error.toString());
                    WMX_Final_CorteCaja.super.showAlert("error", "¡Corte de caja no exitoso!");

                    // Toast.makeText(getApplicationContext(),"CORTE DE CAJA NO
                    // PROCESADA",Toast.LENGTH_LONG).show();
                    // Intent intent = new Intent(mContext,
                    // WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody,
                                "utf-8");
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
            TRACE.d("** Exception ERROR " + TRACE.NEW_LINE + e.toString());
        }
    }



    private String getHTMLEmailTemplate() {
        StringBuilder strBulider = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(getAssets().open("email_cortecaja_template.html")));
            String html;
            while ((html = in.readLine()) != null) {
                strBulider.append(html);
            }
            in.close();
            return strBulider.toString();
        } catch (IOException e) {
            return e.toString();
        }
    }
}
