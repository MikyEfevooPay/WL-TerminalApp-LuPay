package com.efevoopay.demoui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.efevoopay.demoui.R;
import com.efevoopay.demoui.interfaces.FetchEntity;
import com.efevoopay.demoui.interfaces.FetchOptions;
import com.efevoopay.demoui.interfaces.TransactionsViewInterface;
import com.efevoopay.demoui.utils.Fetch;
import com.efevoopay.demoui.utils.FetchUIManager;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Transaction;
import com.efevoopay.demoui.utils.Utils;
import com.efevoopay.demoui.widget.TransactionItemAdapter2;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class WMX_Transaccion extends BaseActivity implements View.OnClickListener, TransactionsViewInterface {

    RecyclerView recyclerView;
    LinearLayout history_layout_empty, history_layout_items;
    ArrayList<Transaction> transactions = new ArrayList<>();
    ImageButton btn_date;
    TextView txt_date;
    DatePicker dpFecha;
    MaterialDatePicker dpDate;
    Date date1, date2;
    Intent intent;
    private String ksn_posId, _ARQC;
    private WMX_llamada_dukpt jsondukpt=new WMX_llamada_dukpt();

    private CompletableFuture<Boolean> hasTransactionFoundPromise;

    private final String TRANSACTION_HISTORY = "getTransactionHistory";
    private final String HISTORY_KEY_AMEX = "getCancelacionHistoryAmex";

    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.switch_title_logo("Historial");
        super.show_calendar();

        btn_date = findViewById(R.id.btn_fecha);
        txt_date = findViewById(R.id.btn_date_txt);
        dpFecha = (DatePicker) findViewById(R.id.dpFecha);
        configLocale();
        DatePickerListener();
        date1 = new Date();
        date2 = new Date();

        history_layout_empty = findViewById(R.id.history_layout_empty);
        history_layout_items = findViewById(R.id.history_layout_items);

        btn_date.setOnClickListener(this);
        txt_date.setOnClickListener(this);
        txt_date.setText(getFecha());

        intent = getIntent();
        ksn_posId = intent.getStringExtra("ksn_posId");
        _ARQC = intent.getStringExtra("ARQC");
        hasTransactionFoundPromise = new CompletableFuture();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(jsondukpt.transactions.size() > 0) jsondukpt.transactions.clear();
        getFetchManager().CallAll();
    }

    @Override
    public void onBackPressed() {
        if(!TextUtils.isEmpty(_ARQC)) {
            startActivity(new Intent(this, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
           return;
        }
        super.onBackPressed();
    }

    @Override
    public void addFetchs(FetchUIManager manager) throws Exception {
        Fetch history = manager.addFetch(TRANSACTION_HISTORY, new FetchOptions(Utils.TERMINAL_API + "/matriz/certificacion/Dukptnumtxn", Request.Method.POST));
        history.setSetBodyListenner(this::getBody);

        Fetch HistoryAmex = manager.addFetch(HISTORY_KEY_AMEX, new FetchOptions(Utils.TERMINAL_AMEX + "/amex/tpv/txndevice", Request.Method.POST));
        HistoryAmex.setSetBodyListenner(this::setBodyAmex);
    }

    private void getBody(JSONObject body) throws JSONException {
        DateFormat obj = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        TRACE.d("date1: " + obj.format(date1) + TRACE.NEW_LINE + "date2: " + obj.format(date2));
        body.put("deviceid", ksn_posId);
        body.put("pantalla", "Historial");
        body.put("fechainicio", obj.format(date1));
        body.put("fechafinal", obj.format(date2));
    }

    private void setBodyAmex(JSONObject body) throws JSONException {
        DateFormat obj = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        body.put("numserie", ksn_posId);
        body.put("pantalla", "Historial");
        body.put("fechainicio", obj.format(date1));
        body.put("fechafinal", obj.format(date2));
    }

    @Override
    public void onFetchCurrentResult(FetchEntity entity, @Nullable FetchEntity error) {
        super.onFetchCurrentResult(entity, error);
        //if(entity.result == null) return;
        /*switch (entity.key) {
            case TRANSACTION_HISTORY:
                jsondukpt.readJsonnew(entity.result.toString());
                transactions=jsondukpt.transactions;
                Collections.reverse(transactions);
                setItems();
                break;
            default:
                break;
        }*/
        if(entity.key.equals(TRANSACTION_HISTORY)) {
            if(entity.result == null) return;
            //TRACE.d("AMEX: " +entity.result.toString());
            jsondukpt.readJsonnew(entity.result.toString());

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
                return lhs.get_datehour().compareTo(rhs.get_datehour());
            }
        });
        /*Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction lhs, Transaction rhs) {
                int result = lhs.get_date2().compareTo(rhs.get_date2());
                if (result != 0)
                {
                    return result;
                }
                return lhs.get_time().compareTo(rhs.get_time());
            }
        });*/
        Collections.reverse(transactions);
        setItems();
    }

    @SuppressLint("NewApi")
    private void setItems() {
        if(transactions.size() > 0) {
            btn_date.setVisibility(View.VISIBLE);
            txt_date.setVisibility(View.VISIBLE);
            history_layout_empty.setVisibility(View.GONE);
            history_layout_items.setVisibility(View.VISIBLE);
            recyclerView = findViewById(R.id.transactionList);
            hasTransactionFoundPromise.thenApply((hasFound) -> {
                if(!TextUtils.isEmpty(_ARQC) && !hasFound) {
                    showAlert("error", getString(R.string.wmx_transaction_not_found));
                }
                return null;
            });
            TransactionItemAdapter2 transactionItemAdapter = new TransactionItemAdapter2(this,transactions, this, _ARQC, hasTransactionFoundPromise);
            recyclerView.setAdapter(transactionItemAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            btn_date.setVisibility(View.GONE);
            txt_date.setVisibility(View.GONE);
            history_layout_items.setVisibility(View.GONE);
            history_layout_empty.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onToolbarLinstener() {
        onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_transaccion;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_fecha:
                showCalendar();
                break;
            case R.id.btn_date_txt:
                showCalendar();
                break;
        }
    }

    private void configLocale() {
        Locale locale = new Locale("es", "ES");
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    public void onCalendarLinstener(){
        toolbar_btn_calendar.setEnabled(false);
        dpDate.show(getSupportFragmentManager(), "date");
    };


    public void DatePickerListener() {
        dpDate = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Seleccione fecha")
                .setTheme(R.style.MaterialCalendarThemeBackground)
                .build();

        dpDate.addOnDismissListener((selector) -> {
            toolbar_btn_calendar.setEnabled(true);
        });

        dpDate.addOnPositiveButtonClickListener((selection) -> {
            Pair<Long, Long> datesMilliseconds = (Pair<Long, Long>)selection;
            date1 = Utils.dateToUTC(datesMilliseconds.first);
            date2 = Utils.dateToUTC(datesMilliseconds.second);
            dpDate.dismiss();
            getFetchManager().CallById(TRANSACTION_HISTORY);
            getFetchManager().CallById(HISTORY_KEY_AMEX);
        });
    }


    public String getFecha()  {

        String dia = String.valueOf(dpFecha.getDayOfMonth());
        String mes = String.valueOf(dpFecha.getMonth()+1);
        String anio = String.valueOf(dpFecha.getYear());

        Date date1= null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(anio+"-"+mes+"-"+dia);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date1);

        return strDate;
    }

    public void showCalendar() {
        dpFecha.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(WMX_Transaccion.this, WMX_Transaction_Desc.class);
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
        intent.putExtra("emisor", transactions.get(position).get_emisor());
        intent.putExtra("nip", transactions.get(position).get_nip());
        intent.putExtra("entrada", transactions.get(position).get_entrada());
        intent.putExtra("ksn_posId",ksn_posId);

        startActivity(intent);
    }
}
