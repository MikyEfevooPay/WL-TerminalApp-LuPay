package com.efevoopay.demoui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.blumonpay.capx.functions.RSA;
import com.blumonpay.capx.model.RSAData;
import com.efevoopay.demoui.BuildConfig;
import com.efevoopay.demoui.R;
import com.efevoopay.demoui.interfaces.FetchEntity;
import com.efevoopay.demoui.interfaces.FetchOptions;
import com.efevoopay.demoui.utils.DBManager;
import com.efevoopay.demoui.utils.Fetch;
import com.efevoopay.demoui.utils.FetchUIManager;
import com.efevoopay.demoui.utils.ResponseCode;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class WMX_Ajustes extends BaseActivity implements View.OnClickListener{
    private Button initialize;
    private String _rsa = "";
    private String _tk = "";
    private String _pk = "";
    private TextView txt_ksn,txt_version,txtmodelo;
    private Intent intent;
    private String ksn_posId;
    public String name="";
    private DBManager dbManager;
    private String p43, p48, p120, address, comercio, msi, msi3, msi6, msi9, msi12, msi18, minimo3, minimo6, minimo9, minimo12, minimo18;

    private final String INITIALIZE_TPV = "initializeTPV";
    private final String TPV_CONFIG = "configTPV";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(getString(R.string.wmx_title_welcome));
        txt_ksn=(TextView)findViewById(R.id.txtksn);
        txt_version=(TextView)findViewById(R.id.txtversion);
        txtmodelo=(TextView)findViewById(R.id.txtmodelo);
        initialize = (Button) findViewById(R.id.WMX_btn_initialize_keys);
        initialize.setOnClickListener(this);
        txt_version.setText(BuildConfig.VERSION_NAME);
        txtmodelo.setText(Build.MODEL);
        intent = getIntent();
        ksn_posId = intent.getStringExtra("ksn_posId");
        txt_ksn.setText(ksn_posId);
        dbManager = new DBManager(this);
        dbManager.open();
        getFetchManager().setForceFetchDone(true);
    }

    @Override
    public void addFetchs(FetchUIManager manager) throws Exception {
        Fetch config = manager.addFetch(TPV_CONFIG, new FetchOptions(Utils.TPVCONFIG + "/apiv0/agrs/terminales/tpv", Request.Method.POST));
        config.setSetBodyListenner(this::getConfigBody);
        Fetch initialize = manager.addFetch(INITIALIZE_TPV, new FetchOptions( Utils.TERMINAL_API + "/efevoo/tpv/initllave", Request.Method.POST));
        initialize.setSetBodyListenner(this::getInitializeBody);
    }

    private void getConfigBody(JSONObject body) throws JSONException {
        body.put("snTerminal", ksn_posId);
    }

    private void getInitializeBody(JSONObject body) throws JSONException {
        body.put("tpv", Build.MODEL+"Android smart POS");
        body.put("device_id", ksn_posId);
        body.put("device_tk", _tk);
        body.put("device_rsa", _rsa);
        body.put("device_p43", p43);
        body.put("device_p48", p48);
        body.put("device_p120", p120);
        body.put("device_address", address);
    }

    @Override
    public void onFetchCurrentResult(FetchEntity entity, @Nullable FetchEntity error) {
        if(error != null) {
            TRACE.d("ERROR: " + error.result.toString());
            WMX_Ajustes.super.showAlert("informative", "¡INTENTA DE NUEVO!");
            return;
        }
        if(entity.result == null) return;
        switch (entity.key) {
            case TPV_CONFIG:
                processConfig((String)entity.result);
                break;
            case INITIALIZE_TPV:
                processInitialization((String)entity.result);
                break;
            default:
                break;
        }
    }

    private void processConfig(String response) {
        try {
            JSONObject objtpv = new JSONObject(response);
            if(objtpv.has("mensaje")) {
                showAlert("error", objtpv.getString("mensaje"));
                getFetchManager().ForceClose();
                return;
            }
            p43=objtpv.getString("p43");
            p48=objtpv.getString("p48");
            p120=objtpv.getString("p120");
            address=objtpv.getString("address");
            comercio=objtpv.getString("comercio");
            msi=objtpv.getString("msi");
            msi3=objtpv.getString("msi3");
            msi6=objtpv.getString("msi6");
            msi9=objtpv.getString("msi9");
            msi12=objtpv.getString("msi12");
            msi18=objtpv.getString("msi18");
            minimo3=objtpv.getString("minimo3");
            minimo6=objtpv.getString("minimo6");
            minimo9=objtpv.getString("minimo9");
            minimo12=objtpv.getString("minimo12");
            minimo18=objtpv.getString("minimo18");
            getFetchManager().CallById(INITIALIZE_TPV);
        } catch (JSONException e) {
            getFetchManager().ForceClose();
            e.printStackTrace();
        }

    }

    private void processInitialization(String response) {
        DatosInicializacion(response,p43,p48,p120,address,comercio,msi,msi3,msi6,msi9,msi12,msi18,minimo3,minimo6,minimo9,minimo12,minimo18);
    }


    @Override
    public void onToolbarLinstener() {
        onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_ajustes;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.WMX_btn_initialize_keys:
                try {
                    RSA rsa = new RSA();

                    RSAData rsaD = new RSAData();
                    rsaD = rsa.generateKeys("3082010902820100CF57041EC2E7399C2BBD6CB0E8EDFC126B7837442541BCE86CC2804F9D90FE06EAE65B07014D789ED17300540D665213054E3E3A2A16D7FE1CFCC1382AF1485C542469D2AB327522444BF1A1EF1D8B79D9E9317B87D3531B364A8FCD24C0C6476E534D0D89070EEE2CBC999F00C5BEF3B935719AB459BBEE4EA86FEBEAC0F02A4F25D4007BA948E7B1E4A0456EB77107C4FCDAC79125EEE5A9D039995B6111F339DB1296A21D9F2048A8213BE29CE36DF0338D1BC04C3D42C0F6965E9694AFB05203D0BC05E6113AA6DA20DF0AB23DEA631144A8891352D866CBA9423B71890A4FD2B2112CE7BB57081581816232CD831932834EF05AA050C6FEBD434E9512ED0203010001");

                    _rsa=rsaD.getRsa();
                    _pk=rsaD.getPublicKey();
                    _tk=rsaD.getTk();

                }catch (Throwable t){
                    TRACE.d("error rsa: " + t);
                }
                getFetchManager().CallById(TPV_CONFIG);
                break;
        }
    }

    public void DatosInicializacion(String _json,String _p43,String _p48,String _p120,String _address,String _comercio,String _msi,String msi3,String msi6,String msi9,String msi12,String msi18,String minimo3,String minimo6,String minimo9,String minimo12,String minimo18){
        try {
            JSONObject object = new JSONObject(_json);

            if(object.getString("codigo").equals("00")){
                dbManager.onUpgrade();
                dbManager.insert(ksn_posId,object.getString("ksn").toString(),object.getString("tk").toString(),object.getString("ipek").toString(),_p43,_p48,_p120,_address,_comercio,_msi,Integer.parseInt(object.getString("count")),msi3,msi6,msi9,msi12,msi18,minimo3,minimo6,minimo9,minimo12,minimo18);

                WMX_Ajustes.super.showAlert("success", "¡Inicialización con éxito!");
            }else{
                ResponseCode.CodeDetails details = ResponseCode.getCodeDetails(object.getString("codigo"));
                WMX_Ajustes.super.showAlert("informative", details.description);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
