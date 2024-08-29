package com.cohetepay.demoui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.cohetepay.demoui.R;
import com.cohetepay.demoui.interfaces.FetchEntity;
import com.cohetepay.demoui.interfaces.FetchOptions;
import com.cohetepay.demoui.interfaces.HistorialCorteCajaViewInterface;
import com.cohetepay.demoui.utils.CorteCaja;
import com.cohetepay.demoui.utils.Fetch;
import com.cohetepay.demoui.utils.FetchUIManager;;
import com.cohetepay.demoui.utils.TRACE;
import com.cohetepay.demoui.utils.Utils;
import com.cohetepay.demoui.widget.FinalCorteCajaItemAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WMX_Final_CorteCaja extends BaseActivity implements View.OnClickListener, HistorialCorteCajaViewInterface {
    RecyclerView recyclerView;
    LinearLayout finalcortecaja_empty_layout;
    private Context mContext;
    Intent intent;
    ArrayList<CorteCaja> cortecaja = new ArrayList<>();
    Button btn_finalcortecaja;
    private TextView txt_totalamount, txt_datetime;
    private WMX_llamada_dukpt jsondukpt = new WMX_llamada_dukpt();
    ProgressDialog loader;
    private String ksn_posId;

    private final String CORTE_CAJA = "getCorteCaja";
    private final String CONFIRM_CORTE_CAJA = "confirmCorteCaja";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setStatusBarColor(Color.WHITE);
        super.setToolbarBgColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.switch_title_logo("Corte de caja");
        recyclerView = findViewById(R.id.final_cortecaja_List);
        finalcortecaja_empty_layout = findViewById((R.id.layout_finalcortecaja_empty));
        intent = getIntent();
        ksn_posId = intent.getStringExtra("ksn_posId");
        loader = Utils.getLoaderSpinner(this, "Enviando...");
        btn_finalcortecaja = (Button) findViewById(R.id.btn_finalcortecaja);
        btn_finalcortecaja.setOnClickListener(this);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh:mm");
        Date date = new Date();

        txt_totalamount = (TextView) findViewById(R.id.txt_totalamount);
        txt_datetime = (TextView) findViewById(R.id.txt_datetime);
        txt_datetime.setText(dateFormat.format(date).toString());

        mContext = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        getFetchManager().CallById(CORTE_CAJA);
    }

    @Override
    public void addFetchs(FetchUIManager manager) throws Exception {
        Fetch corte = manager.addFetch(CORTE_CAJA, new FetchOptions(Utils.TPVCONFIG + "/apiv0/agrs/corte/crud", Request.Method.POST));
        corte.setSetBodyListenner(this::setCorteBody);
        Fetch confirm = manager.addFetch(CONFIRM_CORTE_CAJA, new FetchOptions(Utils.TPVCONFIG + "/apiv0/agrs/corte/crud", Request.Method.POST));
        confirm.setSetBodyListenner(this::setConfirmBody);
    }

    private void setCorteBody(JSONObject body) throws JSONException {
        body.put("snTerminal", ksn_posId);
        body.put("operacion", "D");
        body.put("idCorte", "0");
        body.put("bd", "wl_cohetepay");
    }

    private void setConfirmBody(JSONObject body) throws JSONException {
        body.put("snTerminal", ksn_posId);
        body.put("operacion", "C");
        body.put("idCorte", "0");
        body.put("bd", "wl_cohetepay");
    }

    @Override
    public void onFetchCurrentResult(FetchEntity entity, @Nullable FetchEntity error) {
        super.onFetchCurrentResult(entity, error);
        switch(entity.key) {
            case CORTE_CAJA:
                if(entity.result == null) return;
                jsondukpt.finalcortecaja(entity.result.toString());
                cortecaja = jsondukpt.cortecaja;
                setItems();
                break;
            case CONFIRM_CORTE_CAJA:
                try {
                    JSONObject object = new JSONObject(entity.result.toString());
                    if (object.getString("code").toString().equals("00")) {
                        WMX_Final_CorteCaja.super.showAlert("success", "¡Corte de caja realizado con éxito!");
                        btn_enable(false);
                        ViewTicket();
                    } else {
                        WMX_Final_CorteCaja.super.showAlert("error", "¡Corte de caja no exitoso!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finalcortecaja:
                getFetchManager().CallById(CONFIRM_CORTE_CAJA);
                break;
            default:

        }
    }

    @Override
    public void onToolbarLinstener() {
        onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_final_cortecaja;
    }

    @Override
    public void onItemClick(int position) {

    }

    public void setItems() {
        if (cortecaja.size() > 0) {
            txt_totalamount.setText(jsondukpt.total);
            btn_enable(true);
            finalcortecaja_empty_layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            FinalCorteCajaItemAdapter finalcortecajaItemAdapter = new FinalCorteCajaItemAdapter(this, cortecaja, this);
            recyclerView.setAdapter(finalcortecajaItemAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            txt_totalamount.setText("$0.00 mxn");
            btn_enable(false);
            finalcortecaja_empty_layout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void btn_enable(Boolean bnd) {
        if (bnd) {
            btn_finalcortecaja.setEnabled(true);
        } else {
            btn_finalcortecaja.setEnabled(false);
            btn_finalcortecaja.setAlpha(Float.parseFloat("0.5"));
        }

    }

    private void ViewTicket() throws JSONException {
        intent = new Intent(this, WMX_Final_CorteCaja_Ticket.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("ksn_posId", ksn_posId);
        intent.putExtra("totalamount", jsondukpt.total);
        intent.putExtra("tip", jsondukpt.tip);
        intent.putExtra("corte", jsondukpt.subtotal);
        intent.putExtra("fechaCorte", txt_datetime.getText().toString());
        intent.putExtra("tablerows", jsondukpt.objectcorte.getString("corte"));
        startActivityMiddleware(intent);
    }

}
