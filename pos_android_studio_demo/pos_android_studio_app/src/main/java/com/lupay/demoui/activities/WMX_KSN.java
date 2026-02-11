package com.lupay.demoui.activities;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.dspread.xpos.CQPOSService;
import com.dspread.xpos.QPOSService;
import com.dspread.xpos.TradeSoundType;
import com.lupay.demoui.utils.FileUtils;
import com.lupay.demoui.utils.TRACE;

import java.util.Hashtable;
import java.util.concurrent.CompletableFuture;

public class WMX_KSN {
    private static QPOSService pos;
    private static String posId;
    private static Handler RequestAttempt;
    private static CompletableFuture<String> PosIdResult;
    private static Context mContext;
    private static Integer version = 1; //Subir en 1 si quieres ejecutar el emvxml
    static SharedPreferences prefs;
    public static void init() {
        _init();
    }

    public static void init(Context ctx) {
        mContext = ctx;
        prefs = mContext.getSharedPreferences("mi_prefs", mContext.MODE_PRIVATE);
        _init();
    }

    @SuppressLint("NewApi")
    private static void _init(){
        RequestAttempt = new Handler();
        PosIdResult = new CompletableFuture();
        String currPosId = getPosId();
        if(currPosId != null) {
            RequestAttempt.postDelayed(() -> {
                PosIdResult.complete(currPosId);
            }, 500);
            return;
        }
        tryInit();
        RequestAttempt.postDelayed(() -> {
            TRACE.d("PósIdRequestAgain");
            tryInit();
        }, 5000);
    }


    private static void requestPosId() {
        if(pos != null) pos.getQposId();
    }

    public static String getPosId() { return posId; }

    public static CompletableFuture<String> getPosIdResult() { return PosIdResult; }

    private static void tryInit() {
        closePos();
        pos = null;
        initUart();
    }

    private static void closePos() {
        if(pos != null ) {
            pos.closeUart();
        }
    }

    private static void initUart(){
        TRACE.d("open");
        pos = mContext != null ? QPOSService.getInstance(mContext, QPOSService.CommunicationMode.UART) : QPOSService.getInstance(mContext, QPOSService.CommunicationMode.UART);
        if (pos==null) return;
        pos.setCustomTradeSound(TradeSoundType.Type.TONE_CDMA_SIGNAL_OFF);
        MyPosListener listener= new MyPosListener();
        Handler handler=new Handler(Looper.myLooper());
        pos.initListener(handler,listener);
        pos.openUart();
    }
    private static class MyPosListener extends CQPOSService {
        @Override
        public void onRequestQposConnected() {
            TRACE.d("onRequestQposConnected()");
            requestPosId();
        }

        @Override
        public void onRequestQposDisconnected() {
            TRACE.d("onRequestQposDisconnected()");
        }

        @SuppressLint("NewApi")
        @Override
        public void onQposIdResult(Hashtable<String, String> posIdTable) {
            int versionEjecutada = prefs.getInt("comando_version", -1);
            RequestAttempt.removeCallbacksAndMessages(null);
            if (versionEjecutada != version){
                //Toast.makeText(mContext,"Entre el porque son diferentes",Toast.LENGTH_SHORT).show();
                pos.updateEMVConfigByXml(new String(FileUtils.readAssetsLine("wirebit_emv_profile_tlv_D30-20250321.xml",mContext)));
            }
            String ksnId = posIdTable.get("posId");
            posId = ksnId;
            PosIdResult.complete(ksnId);
            if (versionEjecutada == version) {
                //Toast.makeText(mContext,"entre porque la version es la misma",Toast.LENGTH_SHORT).show();
                closePos();
            }
            TRACE.d("INTERNAL KSN RESULT: " + posId);
        }
        @Override
        public void onReturnCustomConfigResult(boolean isSuccess, String result) {
            //Toast.makeText(mContext,"Finalizo el xml",Toast.LENGTH_SHORT).show();
            prefs.edit()
                    .putInt("comando_version", version)
                    .apply();
            closePos();
        }
    }
}
