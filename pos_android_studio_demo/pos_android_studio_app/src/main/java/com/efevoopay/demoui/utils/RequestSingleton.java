package com.efevoopay.demoui.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestSingleton {
    private static RequestSingleton instance;
    private RequestQueue requestQueue;
    private Context ctx;

    public RequestSingleton(Context _ctx) {
        this.ctx = _ctx;
        this.requestQueue = getRequestQueue();
    }

    public static synchronized RequestSingleton getInstance(Context _ctx) {
        if(instance == null) {
            instance = new RequestSingleton(_ctx);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if(this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(this.ctx.getApplicationContext());
        }
        return this.requestQueue;
    }

}
