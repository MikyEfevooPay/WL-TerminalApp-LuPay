package com.cohetepay.demoui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.dspread.xpos.CQPOSService;
import com.dspread.xpos.QPOSService;
import com.dspread.xpos.TradeSoundType;
import com.cohetepay.demoui.utils.TRACE;

import java.util.Hashtable;
import java.util.concurrent.CompletableFuture;

public class WMX_KSN {
    private static QPOSService pos;
    private static String posId;
    private static Handler RequestAttempt;
    private static CompletableFuture<String> PosIdResult;
    private static Context mContext;

    public static void init() {
        _init();
    }

    public static void init(Context ctx) {
        mContext = ctx;
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
        pos = mContext != null ? QPOSService.getInstance(mContext, QPOSService.CommunicationMode.UART) : QPOSService.getInstance(QPOSService.CommunicationMode.UART);
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
            RequestAttempt.removeCallbacksAndMessages(null);
            String ksnId = posIdTable.get("posId");
            posId = ksnId;
            PosIdResult.complete(ksnId);
            closePos();
            TRACE.d("INTERNAL KSN RESULT: " + posId);
        }
    }
}
