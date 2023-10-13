package com.efevoopay.demoui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.efevoopay.demoui.R;
import com.efevoopay.demoui.interfaces.FetchEntity;
import com.efevoopay.demoui.interfaces.FetchOptions;
import com.efevoopay.demoui.interfaces.TransactionsViewInterface;
import com.efevoopay.demoui.utils.Fetch;
import com.efevoopay.demoui.utils.FetchUIManager;
import com.efevoopay.demoui.utils.Transaction;
import com.efevoopay.demoui.utils.Utils;
import com.efevoopay.demoui.widget.CancelacionesItemAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WMX_Historial_Cancelaciones extends BaseActivity implements View.OnClickListener, TransactionsViewInterface {
    RecyclerView recyclerView;
    LinearLayout cancellation_empty_layout;
    ArrayList<Transaction> transactions = new ArrayList<>();
    Intent intent;
    private final String CANCELATION_HISTORY_KEY = "getCancelacionHistory";

    private String ksn_posId;
    private WMX_llamada_dukpt jsondukpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.switch_title_logo("Cancelaciones");
        recyclerView = findViewById(R.id.historial_cancelaciones_List);
        cancellation_empty_layout = findViewById((R.id.layout_cancellation_empty));
        intent = getIntent();
        ksn_posId = intent.getStringExtra("ksn_posId");
        jsondukpt=new WMX_llamada_dukpt();
        setFetchProgressTitle("Cargando historial...");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FetchUIManager manager = getFetchManager();
        manager.CallAll();
    }

    @Override
    public void addFetchs(FetchUIManager manager) throws Exception {
        Fetch cancelacionHistory = manager.addFetch(CANCELATION_HISTORY_KEY, new FetchOptions(Utils.TERMINAL_API + "/matriz/certificacion/Dukptnumtxn", Request.Method.POST));
        cancelacionHistory.setSetBodyListenner(this::setBody);
    }


    private void setBody(JSONObject body) throws JSONException {
        body.put("deviceid", ksn_posId);
        body.put("pantalla", "Cancelacion");
        body.put("fechainicio", "");
        body.put("fechafinal", "");
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onToolbarLinstener() {
        onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_historial_cancelaciones;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(WMX_Historial_Cancelaciones.this, WMX_Cancelacion_Desc.class);
        intent.putExtra("id", transactions.get(position).get_id());
        intent.putExtra("auth", transactions.get(position).get_auth());
        intent.putExtra("date", transactions.get(position).get_date2());
        intent.putExtra("time", transactions.get(position).get_time());
        intent.putExtra("subtotal", transactions.get(position).get_subtotal());
        intent.putExtra("card", transactions.get(position).get_card());
        intent.putExtra("redtarj", transactions.get(position).get_redtarj());
        intent.putExtra("tipotarj", transactions.get(position).get_tipotarj());
        intent.putExtra("status", transactions.get(position).get_tipotxn());
        intent.putExtra("propina", transactions.get(position).get_propina());
        intent.putExtra("total", transactions.get(position).get_total());
        intent.putExtra("msi", transactions.get(position).get_msi());
        intent.putExtra("aid", transactions.get(position).get_aid());
        intent.putExtra("arqc", transactions.get(position).get_arqc());
        intent.putExtra("approve", transactions.get(position).get_approve());
        intent.putExtra("ksn_posId",ksn_posId);

        startActivity(intent);
    }

    @Override
    public void onFetchCurrentResult(FetchEntity entity, @Nullable FetchEntity error) {
        super.onFetchCurrentResult(entity, error);
        if(entity.key.equals(CANCELATION_HISTORY_KEY)) {
            if(entity.result == null) return;
            jsondukpt.readJsonnew(entity.result.toString());
            transactions = jsondukpt.transactions;
            Collections.reverse(transactions);
            setItems();
        }
    }
    @Override
    public void onFetchResults(List<FetchEntity> entities, List<FetchEntity> errors) {

    }

    public void setItems() {
        if(transactions.size() > 0) {
            cancellation_empty_layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            CancelacionesItemAdapter transactionItemAdapter = new CancelacionesItemAdapter(this,transactions, this);
            recyclerView.setAdapter(transactionItemAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            cancellation_empty_layout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

}
