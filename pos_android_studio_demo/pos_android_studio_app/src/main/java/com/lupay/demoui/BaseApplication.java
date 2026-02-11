package com.lupay.demoui;

import android.app.Application;
import android.content.Context;
import android.webkit.WebStorage;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lupay.demoui.utils.TRACE;

import java.io.File;

import xcrash.XCrash;

public class BaseApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //  Default init
        XCrash.init(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        TRACE.d("Cache antes: "  + cachevacio(this));
        BorrarCache(this);
        TRACE.d("Cache despues: "  + cachevacio(this));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        WebStorage.getInstance().deleteAllData();
    }
    public static void BorrarCache(Context context) {
        try{
            TRACE.d("BarrarCache");
            File cacheDir = context.getCacheDir();
            borrarDirectorio(cacheDir);
        } catch (Exception e) {
            TRACE.d(e.getMessage());
        }
    }
    public static boolean borrarDirectorio(File dir){
        if(dir != null && dir.isDirectory()){
            for(File file : dir.listFiles()){
                borrarDirectorio(file);
            }
        }
        return dir != null && dir.delete();
    }
    public static long cachevacio(Context context){
        long size = 0;
        File cacheDir = context.getCacheDir();

        if(cacheDir != null && cacheDir.listFiles() != null){
            for(File file : cacheDir.listFiles()){
                size += file.length();
            }
        }
        return size;
    }
}
