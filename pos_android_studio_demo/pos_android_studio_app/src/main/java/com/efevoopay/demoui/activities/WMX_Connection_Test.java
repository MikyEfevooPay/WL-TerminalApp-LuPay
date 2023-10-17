package com.efevoopay.demoui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import com.android.volley.Request;
import com.efevoopay.demoui.R;
import com.efevoopay.demoui.interfaces.FetchEntity;
import com.efevoopay.demoui.interfaces.FetchOptions;
import com.efevoopay.demoui.utils.FetchUIManager;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;


public class WMX_Connection_Test extends BaseActivity implements View.OnClickListener {
    Intent intent;
    private String ksn_posId;

    private final String TEST_CONNECTION_ECO = "test_connection_eco";
    private final String TEST_CONNECTION_LOGON = "test_connection_logon";

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
    }

    @Override
    public void onStart() {
        super.onStart();
        Conectividad();
    }

    @Override
    public void addFetchs(FetchUIManager manager) throws Exception {
        manager.addFetch(TEST_CONNECTION_ECO, new FetchOptions(Utils.TERMINAL_API + "/matriz/certificacion/com/v2/eco", Request.Method.POST));
        manager.addFetch(TEST_CONNECTION_LOGON, new FetchOptions(Utils.TERMINAL_API + "/matriz/certificacion/com/v2/logon", Request.Method.POST));
    }

    @Override
    public void onFetchCurrentResult(FetchEntity entity, @Nullable FetchEntity error) {
        if(error != null) {
            onResultActivity(1);
            return;
        }
        if(entity.result == null) return;
        switch (entity.key) {
            case TEST_CONNECTION_ECO:
                processConnectionEco(entity.result.toString());
                break;
            case TEST_CONNECTION_LOGON:
                processConnectionLogon(entity.result.toString());
                break;
            default:
                break;
        }
    }

    private void processConnectionEco(String response) {
        try {
            JSONObject object = new JSONObject(response);
            if(object.has("P39")){
                onResultActivity(0);
                return;
            }
            getFetchManager().CallById(TEST_CONNECTION_LOGON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processConnectionLogon(String response) {
        try {
            JSONObject object = new JSONObject(response);
            onResultActivity(object.has("P39") ? 0 : 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onResultActivity(int type) {
        Intent intent = new Intent(WMX_Connection_Test.this, WMX_Connection_Test_Final.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("type", type);
        intent.putExtra("ksn_posId", ksn_posId);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestsFetching(boolean isFetching) {
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

    public void Conectividad() {
        new Thread(() -> {
            if(isNetworkAvailable()){
                getFetchManager().CallById(TEST_CONNECTION_ECO);
            }else{
                onResultActivity(1);
            }
        }).start();
    }

}
