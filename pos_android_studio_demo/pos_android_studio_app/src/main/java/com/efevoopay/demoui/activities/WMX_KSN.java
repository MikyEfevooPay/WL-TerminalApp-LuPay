package com.efevoopay.demoui.activities;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.dspread.xpos.CQPOSService;
import com.dspread.xpos.QPOSService;
import com.efevoopay.demoui.utils.TRACE;

import java.util.Hashtable;

public class WMX_KSN {
    private QPOSService pos;
    String posId;
    Context eContext;
    protected void onCreate() {
        initUart(QPOSService.CommunicationMode.UART);
        pos.getQposId();
    }
    private void initUart(QPOSService.CommunicationMode mode){
        TRACE.d("open");
        pos=QPOSService.getInstance(mode);
        if (pos==null){
            return;
        }
        if (mode==QPOSService.CommunicationMode.USB_OTG_CDC_ACM){
            pos.setUsbSerialDriver(QPOSService.UsbOTGDriver.CDCACM);
        }
        pos.setD20Trade(true);
        pos.setConext(eContext);
        MyPosListener listener= new MyPosListener();
        Handler handler=new Handler(Looper.myLooper());
        pos.initListener(handler,listener);
    }
    class MyPosListener extends CQPOSService {

        @Override
        public void onQposIdResult(Hashtable<String, String> posIdTable) {
            //TRACE.w("onQposIdResult():" + posIdTable.toString());
            posId = posIdTable.get("posId").toString();
            //ksn.setText(posId);
        }
    }
}
