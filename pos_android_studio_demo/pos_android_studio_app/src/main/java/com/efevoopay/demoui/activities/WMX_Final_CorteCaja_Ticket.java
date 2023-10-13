package com.efevoopay.demoui.activities;

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

import com.efevoopay.demoui.interfaces.FetchEntity;
import com.efevoopay.demoui.interfaces.FetchOptions;
import com.efevoopay.demoui.interfaces.TicketLayoutType;
import com.efevoopay.demoui.utils.DBManager;
import com.efevoopay.demoui.utils.Fetch;
import com.efevoopay.demoui.utils.FetchUIManager;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Ticket;
import com.efevoopay.demoui.utils.Utils;

import com.android.volley.Request;
import com.efevoopay.demoui.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
    private String ksn_posId, totalamount, date, corte, tip, TableRowsString, currEmail;
    private Context mContext;
    private Button btn_cortecaja_final;
    private TextView txt_totalamount, txt_datetime, txt_subtotal, txt_tip;
    private LinearLayout lyt_cortecaja_email, lyt_cortecaja_print;
    private final WMX_llamada_dukpt jsondukpt = new WMX_llamada_dukpt();
    private DBManager dbManager;
    Cursor cursor;
    private CORTE_CAJA_TYPE type;

    private final String SEND_EMAIL = "sendEmail";

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
        TRACE.d("TableRowsString: " + TableRowsString);
        type = CORTE_CAJA_TYPE.getByNumber(intent.getIntExtra("type", 1));
        mContext = this;

        btn_cortecaja_final = findViewById(R.id.btn_cortecaja_final);
        btn_cortecaja_final.setOnClickListener(this);
        lyt_cortecaja_email = findViewById(R.id.lyt_cortecaja_email);
        lyt_cortecaja_email.setOnClickListener(this);
        lyt_cortecaja_print = findViewById(R.id.lyt_cortecaja_print);
        lyt_cortecaja_print.setOnClickListener(this);

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
        Fetch history = manager.addFetch(SEND_EMAIL, new FetchOptions(Utils.TERMINAL_API + "/matriz/certificacion/correocortecaja", Request.Method.POST));
        history.setSetBodyListenner(this::getBody);
    }

    private void getBody(JSONObject body) throws JSONException {
        DateFormat formatemail = new SimpleDateFormat("ddMMyyHHmmss");
        Date datemail = new Date();
        body.put("correo", currEmail);
        body.put("subject", "Corte de caja");
        body.put("idemail", "Corte"+formatemail.format(datemail).toString());
        body.put("comercio", Utils.isNull(cursor.getString(9), "N/A"));
        body.put("subtotal", Utils.isNull(corte, "N/A"));
        body.put("propina", Utils.isNull( tip, "N/A"));
        body.put("montototal", Utils.isNull(totalamount, "N/A"));
        body.put("fechacorte", Utils.isNull(date, "N/A"));
        body.put("tablerows",Utils.isNull(TableRowsString, "[]"));
    }

    @Override
    public void onFetchCurrentResult(FetchEntity entity, @Nullable FetchEntity error) {
        if(error != null) {
            TRACE.d("** ResponseResult ERROR " + TRACE.NEW_LINE + error.result);
            showAlert("error", "ERROR", "¡Correo no enviado!");
            return;
        }
        if(entity.result == null) return;
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
                getFetchManager().CallById(SEND_EMAIL);
            }
        });
    }


}
