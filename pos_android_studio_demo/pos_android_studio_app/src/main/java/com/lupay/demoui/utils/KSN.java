package com.lupay.demoui.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.dspread.xpos.CQPOSService;
import com.dspread.xpos.QPOSService;

import java.util.Hashtable;

public class KSN extends CQPOSService {
    Context eContext;
    private QPOSService pos;
public String posId;

public KSN() {
    onCreate();
}

    private void onCreate() {
        initUart(QPOSService.CommunicationMode.UART);
        pos.getQposId();
    }
    private void initUart(QPOSService.CommunicationMode mode){
        TRACE.d("open");
        pos=QPOSService.getInstance(eContext, mode);
        if (pos==null){
            return;
        }
        if (mode==QPOSService.CommunicationMode.USB_OTG_CDC_ACM){
            pos.setUsbSerialDriver(QPOSService.UsbOTGDriver.CDCACM);
        }
        pos.setD20Trade(true);
        MyPosListener listener= new MyPosListener();
        Handler handler=new Handler(Looper.myLooper());
        pos.initListener(handler,listener);
    }
    private class MyPosListener extends CQPOSService {

        @Override
        public void onQposIdResult(Hashtable<String, String> posIdTable) {
            //TRACE.w("onQposIdResult():" + posIdTable.toString());
            posId = posIdTable.get("posId").toString();
            //ksn.setText(posId);
        }
    }

}



