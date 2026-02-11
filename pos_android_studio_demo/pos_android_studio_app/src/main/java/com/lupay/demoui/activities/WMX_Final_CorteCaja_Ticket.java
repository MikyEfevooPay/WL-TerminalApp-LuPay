package com.lupay.demoui.activities;

import android.app.ProgressDialog;
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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

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
import com.lupay.demoui.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WMX_Final_CorteCaja_Ticket extends BaseActivity implements View.OnClickListener {


    public enum CORTE_CAJA_TYPE {
        DETAILS(0),
        FINAL(1);

        private int id;
        CORTE_CAJA_TYPE(int id) {
            this.id = id;
        }

        public static CORTE_CAJA_TYPE getByNumber(int _id) {
            for(CORTE_CAJA_TYPE type : values()) {
                if(type.id == _id) {
                    return type;
                }
            }
            return CORTE_CAJA_TYPE.FINAL;
        }
    }

    private Intent intent;
    private String ksn_posId, totalamount, date, corte, tip, TableRowsString, currEmail, idcorte;
    private Context mContext;
    private Button btn_cortecaja_final, btn_cortecaja_reprocesar;
    private TextView txt_totalamount, txt_datetime, txt_subtotal, txt_tip;
    private LinearLayout lyt_cortecaja_email, lyt_cortecaja_print;
    private final WMX_llamada_dukpt jsondukpt = new WMX_llamada_dukpt();
    private DBManager dbManager;
    Cursor cursor;
    private CORTE_CAJA_TYPE type;

    private final String SEND_EMAIL = "send_Email";
    private final String CORTE_CAJA_REPROCESS_DETAILS = "getCorteCajaReprocessDetails";
    private final String CORTE_CAJA_REPROCESS = "getCorteCajaReprocess";
    public static ProgressDialog spinner;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.hideToolbar();
        intent = getIntent();
        ksn_posId = intent.getStringExtra("ksn_posId");
        totalamount = intent.getStringExtra("totalamount");
        tip = intent.getStringExtra("tip");
        corte = intent.getStringExtra("corte");
        date = intent.getStringExtra("fechaCorte");
        TableRowsString = intent.getStringExtra("tablerows");
        idcorte = intent.getStringExtra("idcorte");
        TRACE.d("idcorte: " + idcorte);
        TRACE.d("TableRowsString: " + TableRowsString);
        type = CORTE_CAJA_TYPE.getByNumber(intent.getIntExtra("type", 1));
        mContext = this;

        btn_cortecaja_final = findViewById(R.id.btn_cortecaja_final);
        btn_cortecaja_final.setOnClickListener(this);
        lyt_cortecaja_email = findViewById(R.id.lyt_cortecaja_email);
        lyt_cortecaja_email.setOnClickListener(this);
        lyt_cortecaja_print = findViewById(R.id.lyt_cortecaja_print);
        lyt_cortecaja_print.setOnClickListener(this);
        btn_cortecaja_reprocesar = findViewById(R.id.btn_cortecaja_reprocesar);
        btn_cortecaja_reprocesar.setOnClickListener(this);

        spinner = Utils.getLoaderSpinner(this);

        if(type == CORTE_CAJA_TYPE.DETAILS) {
            btn_cortecaja_final.setText("Cerrar");
        }

        txt_totalamount = findViewById(R.id.lbl_cortecaja_total_value);
        txt_datetime = findViewById(R.id.lbl_cortecaja_fechahora_value);
        txt_subtotal = findViewById(R.id.lbl_cortecaja_subtotal_value);
        txt_tip = findViewById(R.id.lbl_cortecaja_propina_value);

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
        Fetch details = manager.addFetch(CORTE_CAJA_REPROCESS_DETAILS, new FetchOptions(Utils.TPVCONFIG + "/api/apiv0/agrs/corte/crud", Request.Method.POST));
        details.setSetBodyListenner(this::getBodyReprocessDetails);
        Fetch reprocess = manager.addFetch(CORTE_CAJA_REPROCESS, new FetchOptions(Utils.TPVCONFIG + "/api/apiv0/agrs/corte/crud", Request.Method.POST));
        reprocess.setSetBodyListenner(this::getBodyReprocess);
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
    private void getBodyReprocessDetails(JSONObject body) throws JSONException {
        body.put("snTerminal", ksn_posId);
        body.put("idCorte", idcorte);
        body.put("fechaInicio", "2026-01-29 00:00:00");
        body.put("fechaFin", "2026-02-02 23:59:59");
        body.put("operacion", "DRE");
        body.put("bd", Utils.TERMINAL_WL_Name);
    }
    private void getBodyReprocess(JSONObject body) throws JSONException {
        body.put("snTerminal", ksn_posId);
        body.put("idCorte", idcorte);
        body.put("fechaInicio", "2026-01-29 00:00:00");
        body.put("fechaFin", "2026-02-02 23:59:59");
        body.put("operacion", "RE");
        body.put("bd", Utils.TERMINAL_WL_Name);
    }
    @Override
    public void onFetchCurrentResult(FetchEntity entity, @Nullable FetchEntity error) {
        super.onFetchCurrentResult(entity, error);
        if (entity.result == null)
            return;
        switch (entity.key) {
            case SEND_EMAIL:
                showAlert("success", "¡Corte caja enviado con éxito!");
                break;
            case CORTE_CAJA_REPROCESS_DETAILS:
                try {
                    if (spinner.isShowing())
                        spinner.dismiss();
                    JSONObject object=new JSONObject(entity.result.toString());
                    if(!object.has("corte")) {
                        showAlert("informative", "No se necesita reprocesar.");
                        return;
                    }
                    openModalAlertCorteCaja();
                } catch (JSONException e){
                    TRACE.d(e.getMessage());
                }
                break;
            case CORTE_CAJA_REPROCESS:
                try {
                    if (spinner.isShowing())
                        spinner.dismiss();
                    JSONObject object=new JSONObject(entity.result.toString());
                    if(object.getString("code").equals("00")){
                        openModalAlertMensaje(object.getString("mensaje"),true);
                    } else {
                        openModalAlertMensaje(object.getString("mensaje"),false);
                    }
                } catch (JSONException e){
                    TRACE.d(e.getMessage());
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.lyt_cortecaja_email:
                openModalSendEmail();
                break;
            case R.id.lyt_cortecaja_print:
                PrintTicket();
                break;
            case R.id.btn_cortecaja_final:
                if(type == CORTE_CAJA_TYPE.DETAILS)
                    onBackPressed();
                    else
                    startActivity(new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                break;
            case R.id.btn_cortecaja_reprocesar:
                spinner.show();
                getFetchManager().CallById(CORTE_CAJA_REPROCESS_DETAILS);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(type == CORTE_CAJA_TYPE.DETAILS) super.onBackPressed();
    }

    @Override
    public void onToolbarLinstener() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_final_cortecaja_ticket;
    }

    @Override
    public TicketLayoutType getPrintLayout() {
        return TicketLayoutType.CORTE;
    }

    @Override
    public void setTicketData(Ticket ticket) {
        ticket.setTrans_Type("CORTE DE CAJA")
                .setDate_Time(date)
                .setAmount(corte)
                .setTip(tip)
                .setTotal(totalamount)
                .setKsn_posId(ksn_posId)
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
        titulo.setText("Recibe tu corte de caja por mail");
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
                currEmail = txt_email.getText().toString();
                modalEmailCreate.dismiss();
                getFetchManager().CallById(SEND_EMAIL);
            }
        });
    }
    private void openModalAlertCorteCaja() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogContentView = inflater.inflate(R.layout.wmx_modal_alert_cortecaja, null);

        MaterialAlertDialogBuilder modalAlert = new MaterialAlertDialogBuilder(mContext,
                R.style.ThemeOverlay_App_MaterialAlertDialog);
        modalAlert.setView(dialogContentView);

        AppCompatButton btn_alert_cortecaja_reprocesar = dialogContentView.findViewById(R.id.btn_alert_cortecaja_reprocesar);
        AppCompatButton btn_alert_cortecaja_cancel = dialogContentView.findViewById(R.id.btn_alert_cortecaja_cancel);

        AlertDialog modalAlterCorteCajaCreate = modalAlert.create();

        modalAlterCorteCajaCreate.show();
        btn_alert_cortecaja_reprocesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.show();
                modalAlterCorteCajaCreate.dismiss();
                getFetchManager().CallById(CORTE_CAJA_REPROCESS);
            }
        });
        btn_alert_cortecaja_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modalAlterCorteCajaCreate.dismiss();
            }
        });
    }

    private void openModalAlertMensaje(String msj, Boolean success) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogContentView = inflater.inflate(R.layout.wmx_modal_alert_menu, null);

        MaterialAlertDialogBuilder modalAlert = new MaterialAlertDialogBuilder(mContext,
                R.style.ThemeOverlay_App_MaterialAlertDialog);
        modalAlert.setView(dialogContentView);

        if (success) {
            AppCompatImageView imageView = dialogContentView.findViewById(R.id.tp_warning);
            imageView.setImageResource(R.drawable.check_cancelar);
        }

        AppCompatTextView modal_text_mensaje = dialogContentView.findViewById(R.id.modal_text_mensaje);
        modal_text_mensaje.setText(msj);

        AppCompatButton btn_alert_mensaje = dialogContentView.findViewById(R.id.btn_alert_try);
        btn_alert_mensaje.setText("Cerrar");

        AlertDialog modalAlterMensajeCreate = modalAlert.create();

        modalAlterMensajeCreate.show();
        btn_alert_mensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modalAlterMensajeCreate.dismiss();
                onBackPressed();
            }
        });
    }
}
