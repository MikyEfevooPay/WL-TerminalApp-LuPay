package com.lupay.demoui.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import com.lupay.demoui.R;
import com.lupay.demoui.interfaces.FetchEntity;
import com.lupay.demoui.interfaces.FetchOptions;
import com.lupay.demoui.interfaces.TicketLayoutType;
import com.lupay.demoui.utils.DBManager;
import com.lupay.demoui.utils.Fetch;
import com.lupay.demoui.utils.FetchUIManager;
import com.lupay.demoui.utils.TRACE;
import com.lupay.demoui.utils.Ticket;
import com.lupay.demoui.utils.Utils;
import com.android.volley.Request;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WMX_Final_Historial_Ticket extends BaseActivity implements View.OnClickListener {
    private Intent intent;
    private String ksn_posId, totalamount, date, date2, corte, tip, TableRowsString, currEmail, totalmovimientos,totaltransacciones, totalcancelaciones, totalmc, totalvisa, totalamex, totalotro, totaldebito, totalcredito;
    private Context mContext;
    private Button btn_historial_final;
    private TextView txt_totalamount, txt_datetime, txt_subtotal, txt_tip;
    private LinearLayout lyt_historial_email, lyt_historial_print;
    private final WMX_llamada_dukpt jsondukpt = new WMX_llamada_dukpt();
    private DBManager dbManager;
    Cursor cursor;

    private final String SEND_EMAIL = "send_Email";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.hideToolbar();
        intent = getIntent();
        ksn_posId = intent.getStringExtra("ksn_posId");
        totalamount = intent.getStringExtra("totalamount");
        tip = intent.getStringExtra("tip");
        corte = intent.getStringExtra("corte");
        date = intent.getStringExtra("date1");
        date2 = intent.getStringExtra("date2");
        TableRowsString = intent.getStringExtra("tablerows");
        TRACE.d("TableRowsString: " + TableRowsString);
        procesaInformacion();
        mContext = this;

        btn_historial_final = findViewById(R.id.btn_historial_final);
        btn_historial_final.setOnClickListener(this);
        lyt_historial_email = findViewById(R.id.lyt_historial_email);
        lyt_historial_email.setOnClickListener(this);
        lyt_historial_print = findViewById(R.id.lyt_historial_print);
        lyt_historial_print.setOnClickListener(this);

        txt_totalamount = findViewById(R.id.lbl_historial_total_value);
        txt_datetime = findViewById(R.id.lbl_historial_fechahora_value);
        txt_subtotal = findViewById(R.id.lbl_historial_subtotal_value);
        txt_tip = findViewById(R.id.lbl_historial_propina_value);

        txt_subtotal.setText(corte);
        txt_tip.setText(tip);
        txt_totalamount.setText(totalamount);
        txt_datetime.setText(date);

        dbManager = new DBManager(mContext);
        dbManager.open();
        cursor = dbManager.fetch(ksn_posId);
        setFetchProgressTitle("Enviando...");
    }

    @Override
    public void addFetchs(FetchUIManager manager) throws Exception {
        Fetch history = manager.addFetch(SEND_EMAIL, new FetchOptions(Utils.TERMINAL_BATCH + "/api/correo/cortecaja", Request.Method.POST));
        history.setSetBodyListenner(this::getBody);
    }

    private void getBody(JSONObject body) throws JSONException {
        DateFormat formatemail = new SimpleDateFormat("ddMMyyHHmm");
        Date datemail = new Date();
        body.put("correo", currEmail);
        body.put("subject", "Corte de caja");
        body.put("idemail", "Corte"+formatemail.format(datemail).toString()+ksn_posId.substring(ksn_posId.length()-8).toString());
        body.put("comercio", Utils.isNull(cursor.getString(9), "N/A"));
        body.put("subtotal", Utils.isNull(corte, "N/A"));
        body.put("propina", Utils.isNull( tip, "N/A"));
        body.put("montototal", Utils.isNull(totalamount, "N/A"));
        body.put("fechacorte", Utils.isNull(date, "N/A"));
        body.put("tablerows",Utils.isNull(TableRowsString, "[]"));
        body.put("wl_name", Utils.TERMINAL_WL_Bucket);
    }

    @Override
    public void onFetchCurrentResult(FetchEntity entity, @Nullable FetchEntity error) {
        super.onFetchCurrentResult(entity, error);
        if (entity.result == null)
            return;
        switch (entity.key) {
            case SEND_EMAIL:
                TRACE.d("** ResponseResult " + TRACE.NEW_LINE + entity.result.toString());
                showAlert("success", "¡Corte caja enviado con éxito!");
                break;
            default:
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.lyt_historial_email:
                openModalSendEmail();
                break;
            case R.id.lyt_historial_print:
                PrintTicket();
                break;
            case R.id.btn_historial_final:
                onBackPressed();
                break;
            default:
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onToolbarLinstener() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_final_historial_ticket;
    }

    @Override
    public TicketLayoutType getPrintLayout() {
        return TicketLayoutType.HISTORIAL;
    }

    @Override
    public void setTicketData(Ticket ticket) {
        ticket.setTrans_Type("REPORTE DE TRANSACCIONES")
                .setDate_Time(date)
                .setDate_Time2(date2)
                .setAmount(corte)
                .setTip(tip)
                .setTotal(totalamount)
                .setKsn_posId(ksn_posId)
                .setTotalMovimientos(totalmovimientos)
                .setTotalTransacciones(totaltransacciones)
                .setTotalCancelaciones(totalcancelaciones)
                .setTotal_MC(totalmc)
                .setTotal_Visa(totalvisa)
                .setTotal_Amex(totalamex)
                .setTotal_Otro(totalotro)
                .setTotal_Credito(totalcredito)
                .setTotal_Debito(totaldebito)
                .setTable_Rows(TableRowsString)
                .setCursor(cursor);
    }

    private void openModalSendEmail() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogContentView = inflater.inflate(R.layout.wmx_modal_email_input, null);

        MaterialAlertDialogBuilder modalEmail = new MaterialAlertDialogBuilder(mContext,
                R.style.ThemeOverlay_App_MaterialAlertDialog);
        modalEmail.setView(dialogContentView);

        AppCompatButton btn_modal_sendEmail = dialogContentView.findViewById(R.id.btn_modal_sendEmail);
        AppCompatImageButton btn_close = dialogContentView.findViewById(R.id.btn_correo_modal_close);
        btn_modal_sendEmail.setEnabled(false);
        btn_modal_sendEmail.getBackground().setAlpha(128);
        EditText txt_email = dialogContentView.findViewById(R.id.editTextTextPersonName2);
        TextView titulo = dialogContentView.findViewById(R.id.textView31);
        titulo.setText("Recibe tu historial de transacciones por mail");
        txt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = s.toString();
                boolean isValidEmail = Utils.isValidEmail(email);
                boolean currEnableState = btn_modal_sendEmail.isEnabled();
                if (isValidEmail != currEnableState) {
                    btn_modal_sendEmail.setEnabled(isValidEmail);
                    btn_modal_sendEmail.getBackground().setAlpha(isValidEmail ? 255 : 128);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        AlertDialog modalEmailCreate = modalEmail.create();

        modalEmailCreate.show();

        btn_close.setOnClickListener((view) -> {
            modalEmailCreate.dismiss();
        });

        btn_modal_sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_modal_sendEmail.setClickable(false);
                currEmail = txt_email.getText().toString();
                modalEmailCreate.dismiss();
                getFetchManager().CallById(SEND_EMAIL);
            }
        });
    }

    private void procesaInformacion(){
        try {
            JSONArray array = new JSONArray(TableRowsString);
            totalmovimientos = String.valueOf(array.length());
            Integer count = 0;
            Integer cancelacionescount = 0;
            Integer mccount = 0;
            Integer visacount = 0;
            Integer amexcount = 0;
            Integer otrocount = 0;
            Integer debitocount = 0;
            Integer creditocount = 0;
            for (int i = 0; i < array.length(); i++) {
                JSONObject object1 = array.getJSONObject(i);
                if(object1.getString("tipotxn").equals("CAN") || object1.getString("tipotxn").equals("CANMSI")){
                    cancelacionescount++;
                } else {
                    count++;
                }
                if(object1.getString("redtarj").toUpperCase().equals("VISA")){
                    visacount++;
                } else if(object1.getString("redtarj").toUpperCase().equals("MC")){
                    mccount++;

                } else if(object1.getString("redtarj").toUpperCase().equals("AMEX")){
                    amexcount++;
                } else {
                    otrocount++;
                }
                if(object1.getString("tipotarj").toUpperCase().contains("BITO")){
                    debitocount++;
                } else {
                    creditocount++;
                }
            }
            totaltransacciones = String.valueOf(count);
            totalcancelaciones = String.valueOf(cancelacionescount);
            totalmc = String.valueOf(mccount);
            totalvisa = String.valueOf(visacount);
            totalamex = String.valueOf(amexcount);
            totalotro = String.valueOf(otrocount);
            totalcredito = String.valueOf(creditocount);
            totaldebito = String.valueOf(debitocount);
        } catch (JSONException e){
            TRACE.d(e.getMessage());
        }
    }
}
