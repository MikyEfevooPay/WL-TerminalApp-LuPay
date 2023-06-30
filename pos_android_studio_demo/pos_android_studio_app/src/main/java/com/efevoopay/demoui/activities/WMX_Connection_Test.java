package com.efevoopay.demoui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

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
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class WMX_Connection_Test extends BaseActivity implements View.OnClickListener {
    Intent intent;
    private String ksn_posId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        super.setInvisiblemargin(true);
        super.setWhiteLogo();
        super.setCustomToolbarColor("#002344ED");
        super.setMarginLogo();
        intent = getIntent();
        ksn_posId = intent.getStringExtra("ksn_posId");
        Conectividad(this);

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onToolbarLinstener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_connection_test;
    }

    public void Conectividad(Context context) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                if(isNetworkAvailable(context)){
                    conectividadeco();
                }else{
                Intent intent = new Intent(WMX_Connection_Test.this, WMX_Connection_Test_Final.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("type", 1);
                intent.putExtra("ksn_posId", ksn_posId);
                startActivity(intent);
                }
                handler.removeCallbacks(this);
            }
        }, 6000);

    }
    private void conectividadeco() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = Utils.TERMINAL_API + "/matriz/certificacion/com/v2/eco";
            final String requestBody = null;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if(object.has("P39")){
                            Intent intent = new Intent(WMX_Connection_Test.this, WMX_Connection_Test_Final.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("type", 0);
                            intent.putExtra("ksn_posId", ksn_posId);
                            startActivity(intent);
                            TRACE.d("conectividadeco: " +  TRACE.NEW_LINE + response.toString() );
                        }
                        else{
                            conectividadlogon();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    TRACE.d("** ResponseResult " +  TRACE.NEW_LINE + response.toString() );
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Intent intent = new Intent(WMX_Connection_Test.this, WMX_Connection_Test_Final.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("type", 1);
                    intent.putExtra("ksn_posId", ksn_posId);
                    startActivity(intent);
                    TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + error.toString());
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
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + e.toString() );

        }
    }
    private void conectividadlogon() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = Utils.TERMINAL_API + "/matriz/certificacion/com/v2/logon";
            final String requestBody = null;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if(object.has("P39")){
                            Intent intent = new Intent(WMX_Connection_Test.this, WMX_Connection_Test_Final.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("type", 0);
                            intent.putExtra("ksn_posId", ksn_posId);
                            startActivity(intent);
                            TRACE.d("conectividadlogon: " +  TRACE.NEW_LINE + response.toString() );
                        }else{
                            Intent intent = new Intent(WMX_Connection_Test.this, WMX_Connection_Test_Final.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("type", 1);
                            intent.putExtra("ksn_posId", ksn_posId);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    TRACE.d("** ResponseResult " +  TRACE.NEW_LINE + response.toString() );
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Intent intent = new Intent(WMX_Connection_Test.this, WMX_Connection_Test_Final.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("type", 1);
                    intent.putExtra("ksn_posId", ksn_posId);
                    startActivity(intent);
                    TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + error.toString());
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
            requestQueue.add(stringRequest);
        } catch (Exception e) {

            TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + e.toString() );

        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getNetworkInfo(1);
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // Estas conectado a un Wi-Fi
                TRACE.d("MIAPP"+ " Nombre red Wi-Fi: " + networkInfo.getReason());
                return true;
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // Estas conectado a un Mobile
                TRACE.d("MIAPP"+ " Nombre red Mobile: " + networkInfo.getReason());
                return true;
            }
        }
        return false;
    }
}
