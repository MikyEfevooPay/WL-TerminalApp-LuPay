package com.efevoopay.demoui.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

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
import com.efevoopay.demoui.activities.WMX_Historial_Cancelaciones;
import com.efevoopay.demoui.interfaces.FetchOptions;
import com.efevoopay.demoui.interfaces.IFetching;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;

public class Fetch implements IFetching {
    private JSONObject jsonBody;
    private FetchOptions options;
    private String FetchResponseString;
    private String contentType;
    private CompletableFuture<String> ResponseAsync;
    private RequestQueue requestQueue;
    private FetchSetBody setBodyListenner;
    public String key;

    @SuppressLint("NewApi")
    public Fetch(String _key, FetchOptions _options, Context ctx) {
        this.key = _key;
        this.options = _options;
        this.jsonBody = new JSONObject();
        this.ResponseAsync = new CompletableFuture();
        requestQueue = Volley.newRequestQueue(ctx);
    }

    public String getFetchResponseString() {
        return this.FetchResponseString;
    }

    public void setSetBodyListenner(FetchSetBody listenner) {
        this.setBodyListenner = listenner;
    }

    @Override
    public void onFetchResult(Object result, String error) {

    }

    @Override
    public void onRequestFetching(boolean isFetching) {
    }

    @Override
    public void setBodyContentType(String _contentType) {
        this.contentType = _contentType;
    }

    public CompletableFuture<String> getResponseAsync() {
        return this.ResponseAsync;
    }

    @SuppressLint("NewApi")
    public void clearResponse() {
        ResponseAsync = new CompletableFuture();
    }


    /**
     * Llamada asincrona que manda a hacer la peticion http al backend
     * */
    @SuppressLint("NewApi")
    public void Call() {
        onRequestFetching(false);
        StringRequest stringRequest = new StringRequest(options.method,options.URL, response -> {
            TRACE.d("CURR_INTERNAL_RESPONSE: " + response + TRACE.NEW_LINE + "BODY: " + jsonBody.toString());
            onRequestFetching(true);
            ResponseAsync.complete(response);
        }, error -> {
            onRequestFetching(true);
            ResponseAsync.completeExceptionally(error);
        }) {

            @Override
            public String getBodyContentType() {
                return Utils.isNull(contentType, "application/json; charset=utf-8");
            }

            @Override
            public byte[] getBody() {
                try {
                    if(setBodyListenner != null) {
                        setBodyListenner.setBodyElement(jsonBody);
                    }
                    return jsonBody == null ? null : jsonBody.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonBody, "utf-8");
                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();
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
    }
}
