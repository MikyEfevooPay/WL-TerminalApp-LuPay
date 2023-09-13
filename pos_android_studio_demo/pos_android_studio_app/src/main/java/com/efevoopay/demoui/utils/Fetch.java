package com.efevoopay.demoui.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.efevoopay.demoui.interfaces.FetchOptions;
import com.efevoopay.demoui.interfaces.IFetching;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;

public class Fetch implements IFetching {
    private JSONObject jsonBody;
    private FetchOptions options;
    private String contentType;
    private CompletableFuture<String> ResponseAsync;
    private FetchSetBody setBodyListenner;
    private Context mContext;
    public String key;

    @SuppressLint("NewApi")
    public Fetch(String _key, FetchOptions _options, Context ctx) {
        this.key = _key;
        this.options = _options;
        this.jsonBody = new JSONObject();
        this.ResponseAsync = new CompletableFuture();
        this.mContext = ctx;
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
                } finally {
                    jsonBody = new JSONObject();
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
        RequestSingleton.getInstance(mContext).getRequestQueue().add(stringRequest);
    }
}
