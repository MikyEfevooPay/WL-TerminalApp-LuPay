package com.lupay.demoui.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.lupay.demoui.R;
import com.lupay.demoui.interfaces.FetchEntity;
import com.lupay.demoui.interfaces.FetchOptions;
import com.lupay.demoui.interfaces.HistorialCorteCajaViewInterface;
import com.lupay.demoui.utils.CorteCaja;
import com.lupay.demoui.utils.Fetch;
import com.lupay.demoui.utils.FetchUIManager;
import com.lupay.demoui.utils.Utils;
import com.lupay.demoui.widget.CorteCajaItemAdapter;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WMX_Historial_CorteCaja extends BaseActivity implements View.OnClickListener, HistorialCorteCajaViewInterface {
    RecyclerView recyclerView;
    LinearLayout cortecaja_empty_layout;
    Intent intent;
    ArrayList<CorteCaja> cortecaja = new ArrayList<>();
    Button btn_hacercorte;
    private Date date1, date2;

    private boolean searchByDates;
    private MaterialDatePicker dpDate;
    private WMX_llamada_dukpt jsondukpt=new WMX_llamada_dukpt();
    private WMX_llamada_dukpt jsondukpt_details = new WMX_llamada_dukpt();
    private String ksn_posId, currIdCorte;
    private int currPosition;

    private final String CORTE_CAJA_HISTORIAL = "getCorteCajaHistorial";
    private final String CORTE_CAJA_HISTORIAL_DETAILS = "getCorteCajaHistorialDetails";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.switch_title_logo("Corte de caja");
        super.show_calendar();
        configCalendar();
        recyclerView = findViewById(R.id.historial_cortecaja_List);
        cortecaja_empty_layout = findViewById((R.id.layout_cortecaja_empty));
        intent = getIntent();
        ksn_posId = intent.getStringExtra("ksn_posId");
        btn_hacercorte =  (Button) findViewById(R.id.btn_hacercortecaja);
        btn_hacercorte.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getFetchManager().CallById(CORTE_CAJA_HISTORIAL);
    }

    @Override
    public void onCalendarLinstener(){
        toolbar_btn_calendar.setEnabled(false);
        dpDate.show(getSupportFragmentManager(), "date");
    };

    private void configCalendar() {
        date1 = new Date();
        date2 = new Date();
        searchByDates = false;
        DatePickerListener();

        Locale locale = new Locale("es", "ES");
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void DatePickerListener() {
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
            searchByDates = true;
            getFetchManager().CallById(CORTE_CAJA_HISTORIAL);
        });
    }

    @Override
    public void addFetchs(FetchUIManager manager) throws Exception {
        Fetch history = manager.addFetch(CORTE_CAJA_HISTORIAL, new FetchOptions(Utils.TPVCONFIG + "/api/apiv0/agrs/corte/crud", Request.Method.POST));
        history.setSetBodyListenner(this::historyBody);
        Fetch details = manager.addFetch(CORTE_CAJA_HISTORIAL_DETAILS, new FetchOptions(Utils.TPVCONFIG + "/api/apiv0/agrs/corte/crud", Request.Method.POST));
        details.setSetBodyListenner(this::detailsBody);
    }


    private void historyBody(JSONObject body) throws JSONException {
        DateFormat obj = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        body.put("snTerminal", ksn_posId);
        body.put("operacion", searchByDates ? "f" : "H");
        body.put("idCorte", "0");
        if(searchByDates) {
            body.put("fechaInicio", obj.format(date1) + " 00:00:00");
            body.put("fechaFin", obj.format(date2) + " 23:59:59");
        }
        body.put("bd", Utils.TERMINAL_WL_Name);
    }

    private void detailsBody(JSONObject body) throws JSONException {
        body.put("snTerminal", ksn_posId);
        body.put("idCorte", currIdCorte);
        body.put("operacion", "HD");
        body.put("bd", Utils.TERMINAL_WL_Name);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_hacercortecaja:
                Intent intent = new Intent(WMX_Historial_CorteCaja.this, WMX_Final_CorteCaja.class);
                intent.putExtra("ksn_posId", ksn_posId);
                startActivity(intent);
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
        return R.layout.wmx_historial_cortecaja;
    }

    @Override
    public void onItemClick(int position) {
        currIdCorte = cortecaja.get(position).get_idCorte();
        currPosition = position;
        getFetchManager().CallById(CORTE_CAJA_HISTORIAL_DETAILS);
    }

    @Override
    public void onFetchCurrentResult(FetchEntity entity, @Nullable FetchEntity error) {
        super.onFetchCurrentResult(entity, error);
        if(entity.result == null) return;
        switch (entity.key) {
            case CORTE_CAJA_HISTORIAL:
                jsondukpt.historialcortecaja(entity.result.toString());
                cortecaja=jsondukpt.cortecaja;
                setItems();
                break;
            case CORTE_CAJA_HISTORIAL_DETAILS:
                jsondukpt_details.finalcortecaja(entity.result.toString());
                try {
                    onDetailsScreen(currPosition);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
    @Override
    public void onFetchResults(List<FetchEntity> entities, List<FetchEntity> errors) {

    }

    public void setItems() {
        if(cortecaja.size() > 0) {
            cortecaja_empty_layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            CorteCajaItemAdapter cortecajaItemAdapter = new CorteCajaItemAdapter(this,cortecaja, this);
            recyclerView.setAdapter(cortecajaItemAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            cortecaja_empty_layout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void onDetailsScreen(int position) throws JSONException {
        Intent intent = new Intent(WMX_Historial_CorteCaja.this, WMX_Final_CorteCaja_Ticket.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("ksn_posId", cortecaja.get(position).get_Identificador());
        intent.putExtra("totalamount", cortecaja.get(position).get_Total());
        intent.putExtra("tip", cortecaja.get(position).get_Propina());
        intent.putExtra("corte", cortecaja.get(position).get_Subtotal());
        intent.putExtra("fechaCorte", cortecaja.get(position).get_FechaHora());
        intent.putExtra("type", 0);
        intent.putExtra("tablerows", jsondukpt_details.objectcorte.getString("corte"));
        startActivity(intent);
    }

}
