package com.lupay.demoui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lupay.demoui.R;
import com.lupay.demoui.interfaces.FetchEntity;
import com.lupay.demoui.interfaces.FetchOptions;
import com.lupay.demoui.interfaces.TransactionsViewInterface;
import com.lupay.demoui.utils.Fetch;
import com.lupay.demoui.utils.FetchUIManager;
import com.lupay.demoui.utils.TRACE;
import com.lupay.demoui.utils.Transaction;
import com.lupay.demoui.utils.Utils;
import com.lupay.demoui.widget.CancelacionesItemAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WMX_Historial_Cancelaciones extends BaseActivity implements View.OnClickListener, TransactionsViewInterface {
    RecyclerView recyclerView;
    LinearLayout cancellation_empty_layout;
    ArrayList<Transaction> transactions = new ArrayList<>();
    Intent intent;
    private final String CANCELATION_HISTORY_KEY = "getCancelacionHistory";
    private final String HISTORY_KEY_AMEX = "getCancelacionHistoryAmex";

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
        if(jsondukpt.transactions.size() > 0) jsondukpt.transactions.clear();
        manager.CallAll();
    }

    @Override
    public void addFetchs(FetchUIManager manager) throws Exception {
        Fetch cancelacionHistory = manager.addFetch(CANCELATION_HISTORY_KEY, new FetchOptions(Utils.TERMINAL_BATCH + "/api/consulta/dukptdeviceid", Request.Method.POST));
        cancelacionHistory.setSetBodyListenner(this::setBody);

        Fetch HistoryAmex = manager.addFetch(HISTORY_KEY_AMEX, new FetchOptions(Utils.TERMINAL_AMEX + "/amex/tpv/txndevice", Request.Method.POST));
        HistoryAmex.setSetBodyListenner(this::setBodyAmex);
    }


    private void setBody(JSONObject body) throws JSONException {
        body.put("deviceid", ksn_posId);
        body.put("pantalla", "Cancelacion");
        body.put("fechainicio", "");
        body.put("fechafinal", "");
    }
    private void setBodyAmex(JSONObject body) throws JSONException {
        body.put("numserie", ksn_posId);
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
        if (puedeCancelar(transactions.get(position).get_datehour())) {
            Intent intent = new Intent(WMX_Historial_Cancelaciones.this, WMX_Cancelacion_Desc.class);
            intent.putExtra("id", transactions.get(position).get_id());
            intent.putExtra("auth", transactions.get(position).get_auth());
            intent.putExtra("date", transactions.get(position).get_date());
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
            intent.putExtra("tarjeta", transactions.get(position).get_tarjeta());
            intent.putExtra("datetime", transactions.get(position).get_datehour());
            intent.putExtra("emisor", transactions.get(position).get_emisor());
            intent.putExtra("nip", transactions.get(position).get_nip());
            intent.putExtra("entrada", transactions.get(position).get_entrada());
            intent.putExtra("ksn_posId",ksn_posId);

            startActivity(intent);
        } else {
            openModalmensaje();
        }
    }

    @Override
    public void onFetchCurrentResult(FetchEntity entity, @Nullable FetchEntity error) {
        super.onFetchCurrentResult(entity, error);
        if(entity.key.equals(CANCELATION_HISTORY_KEY)) {
            if(entity.result == null) return;
            //TRACE.d("PROSA: " +entity.result.toString());
            jsondukpt.readJsonnew(entity.result.toString());
            //transactions = jsondukpt.transactions;
            //Collections.reverse(transactions);
            //setItems();
        }
        if(entity.key.equals(HISTORY_KEY_AMEX)) {
            if(entity.result == null) return;
            //TRACE.d("AMEX: " +entity.result.toString());
            jsondukpt.readJsonnew(entity.result.toString());

        }
    }
    @Override
    public void onFetchResults(List<FetchEntity> entities, List<FetchEntity> errors) {
        TRACE.d("ultimo: ");
        transactions = jsondukpt.transactions;
        Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction lhs, Transaction rhs) {
                return lhs.get_time().compareTo(rhs.get_time());
            }
        });
        Collections.reverse(transactions);
        setItems();
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
    public boolean puedeCancelar(String fechaRegistro) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date fechaSeleccionada = sdf.parse(fechaRegistro);
            Calendar ahora = Calendar.getInstance();
            Calendar seleccion = Calendar.getInstance();
            seleccion.setTime(fechaSeleccionada);
            // validar mismo día
            boolean mismoDia =
                    ahora.get(Calendar.YEAR) == seleccion.get(Calendar.YEAR) &&
                            ahora.get(Calendar.DAY_OF_YEAR) == seleccion.get(Calendar.DAY_OF_YEAR);
            // límite 22:59
            Calendar limite = Calendar.getInstance();
            limite.set(Calendar.HOUR_OF_DAY, 22);
            limite.set(Calendar.MINUTE, 59);
            limite.set(Calendar.SECOND, 0);
            boolean antesDeLimite = ahora.before(limite);
            return mismoDia && antesDeLimite;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private void openModalmensaje() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogContentView = inflater.inflate(R.layout.wmx_modal_alert_cancelaciones, null);

        MaterialAlertDialogBuilder modalAlert = new MaterialAlertDialogBuilder(this,
                R.style.ThemeOverlay_App_MaterialAlertDialog);
        modalAlert.setView(dialogContentView);

        AppCompatButton btn_alert_card_close = dialogContentView.findViewById(R.id.btn_alert_try);

        AlertDialog modalAlterMenuCreate = modalAlert.create();

        modalAlterMenuCreate.show();
        btn_alert_card_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modalAlterMenuCreate.dismiss();
            }
        });
    }
}
