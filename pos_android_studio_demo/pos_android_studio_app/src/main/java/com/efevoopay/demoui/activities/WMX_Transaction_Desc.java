package com.efevoopay.demoui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import androidx.core.graphics.drawable.DrawableCompat;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;

import com.dspread.xpos.Util;
import com.efevoopay.demoui.R;
import com.efevoopay.demoui.interfaces.FetchEntity;
import com.efevoopay.demoui.interfaces.FetchOptions;
import com.efevoopay.demoui.interfaces.TicketLayoutType;
import com.efevoopay.demoui.utils.DBManager;
import com.efevoopay.demoui.utils.Fetch;
import com.efevoopay.demoui.utils.FetchUIManager;
import com.efevoopay.demoui.utils.GNTBackEnd;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Utils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.efevoopay.demoui.utils.PRINT_TYPE;
import com.efevoopay.demoui.utils.Ticket;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WMX_Transaction_Desc extends BaseActivity implements View.OnClickListener {

    TextView tp_tv_trans_type, tp_tv_auth, tp_tv_amount, tp_tv_tip, tp_tv_total, tp_tv_card, tp_tv_date_time,
            tp_tv_approve, tp_tv_tip_label, tp_tv_total_label, tp_tv_tipotarjeta, tp_tv_AID, tp_tv_ARQC;
    ImageView tp_iv_trans_type, tp_iv_process;
    LinearLayout tp_ll_content_card, lyt_transaction_tip, lyt_historial_details_email;
    private int transaction_type, trans_id;
    private String card_provider, type_transaction, v_months, tipotarjeta, currEmail, card_emisor, nip, entrada;
    Context mContext;
    private String ksn_posId;
    ProgressDialog loader;
    private DBManager dbManager;
    Cursor cursor;

    private final String TRANSACTION_SEND_EMAIL = "transaction_send_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Button print_button = (Button) findViewById(R.id.btn_print);
        print_button.setOnClickListener(this);
        lyt_historial_details_email = findViewById(R.id.lyt_historial_details_email);
        lyt_historial_details_email.setOnClickListener(this);
        mContext = this;
        super.switch_title_logo("Detalle Transacción");
        Intent intent = getIntent();
        loader = Utils.getLoaderSpinner(this, "Enviando...");
        setFetchProgressTitle("Enviando...");
        initData(intent);
    }

    @Override
    public void addFetchs(FetchUIManager manager) throws Exception {
        Fetch history = manager.addFetch(TRANSACTION_SEND_EMAIL,
                new FetchOptions(Utils.TERMINAL_API + "/matriz/certificacion/correoticket", Request.Method.POST));
        history.setSetBodyListenner(this::getBody);
    }

    private void getBody(JSONObject body) throws JSONException {
        body.put("correo", currEmail);

        if (type_transaction.equals("venta")) {
            body.put("subject", "Ticket de compra");
            body.put("tipo", "sale");
        } else if (type_transaction.equals("Cancelacion")) {
            body.put("subject", "Ticket de Cancelación");
            body.put("tipo", "cancel");
        } else if (type_transaction.equals("MSI")) {
            body.put("subject", "Ticket MSI");
            body.put("tipo", "msi");
        }
        DateFormat formatemail = new SimpleDateFormat("ddMMyyHHmmss");
        Date datemail = new Date();
        body.put("idemail", "Ticket" + formatemail.format(datemail).toString());
        body.put("comercio", Utils.isNull(cursor.getString(9), "N/A"));
        body.put("msi", Utils.isNull(v_months, "N/A"));
        body.put("amount", Utils.isNull(tp_tv_amount.getText().toString(), "N/A"));
        body.put("tip", Utils.isNull(tp_tv_tip.getText().toString(), "N/A"));
        body.put("total", Utils.isNull(tp_tv_total.getText().toString(), "N/A"));
        body.put("pay_method", Utils.isNull(tipotarjeta + "/" + card_emisor + "/" + card_provider, "N/A"));
        body.put("idrecibo", Utils.isNull(String.valueOf(trans_id), "N/A"));
        body.put("afiliacion", Utils.isNull(cursor.getString(26), "N/A"));
        body.put("autorizacion", Utils.isNull(tp_tv_approve.getText().toString(), "N/A"));
        body.put("card", Utils.isNull(tp_tv_card.getText().toString(), "N/A"));
        body.put("payment_date", Utils.isNull(tp_tv_date_time.getText().toString(), "N/A"));
        body.put("address", Utils.isNull(cursor.getString(8), "N/A"));
        body.put("kpos_id", Utils.isNull(ksn_posId, "N/A"));
        body.put("arqc", Utils.isNull(Utils.maskText(tp_tv_ARQC.getText().toString(), 4), "N/A"));
        body.put("aid", Utils.isNull(Utils.maskText(tp_tv_AID.getText().toString(), 4), "N/A"));
        body.put("singtype", Utils.isNull(tipofirma(nip, entrada), ""));

    }

    @Override
    public void onFetchCurrentResult(FetchEntity entity, @Nullable FetchEntity error) {
        super.onFetchCurrentResult(entity, error);
        if (entity.result == null)
            return;
        switch (entity.key) {
            case TRANSACTION_SEND_EMAIL:
                showAlert("success", "¡Ticket enviado con éxito!");
                break;
            default:
                break;
        }
    }

    @Override
    public void onToolbarLinstener() {
        onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_transaction_prev;
    }

    @Override
    public TicketLayoutType getPrintLayout() {
        return TicketLayoutType.TRANSACTION;
    }

    @Override
    public void setTicketData(Ticket ticket) {
        ticket.setTrans_Type(tp_tv_trans_type.getText().toString())
                .setCard(tp_tv_card.getText().toString())
                .setCardType(tipotarjeta)
                .setCard_provider(card_provider)
                .setStatus("APROBADA")
                .setApprove(tp_tv_approve.getText().toString())
                .setDate_Time(tp_tv_date_time.getText().toString())
                .setAmount(tp_tv_amount.getText().toString())
                .setTip(tp_tv_tip.getText().toString())
                .setTotal(tp_tv_total.getText().toString())
                .setARQC(tp_tv_ARQC.getText().toString())
                .setAID(tp_tv_AID.getText().toString())
                .setKsn_posId(ksn_posId)
                .setCursor(cursor)
                .setMsi(v_months)
                .setCard_emisor(card_emisor)
                .setCard_entrada(entrada)
                .setCard_nip(nip)
                .setCard_singtype(tipofirma(nip, entrada))
                .setTransId(trans_id);
    }

    private void onFinish() {
       runOnUiThread(() -> {
           new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog_secondary)
                   .setTitle("Impresión de Ticket")
                   .setIcon(R.drawable.printer)
                   .setPositiveButton("Comercio", (dialog, lis) -> {
                       dialog.dismiss();
                       PrintTicket(PRINT_TYPE.STORE);
                   })
                   .setNeutralButton("Cliente", (dialog, lis) -> {
                       dialog.dismiss();
                       PrintTicket(PRINT_TYPE.CLIENT);
                   })
                   .show();
       });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_print:
                if (Build.MODEL.equals("D30")||Build.MODEL.equals("D60")) {
                    onFinish();
                } else {
                    TRACE.d("click email button");
                    openModalSendEmail();
                }

                break;
            case R.id.lyt_historial_details_email:
                openModalSendEmail();
                break;
            default:
                break;

        }
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
        txt_email.requestFocus();

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

        btn_modal_sendEmail.setOnClickListener(view -> {
            currEmail = txt_email.getText().toString();
            getFetchManager().CallById(TRANSACTION_SEND_EMAIL);
            modalEmailCreate.dismiss();
        });
    }

    private void initData(Intent intent) {
        String auth, date, time, subtotal, card, redtarj, status, propina, total, msi, aid, arqc, approve;
        auth = intent.getStringExtra("auth");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        subtotal = intent.getStringExtra("subtotal");
        card = intent.getStringExtra("card");
        redtarj = intent.getStringExtra("redtarj");
        tipotarjeta = intent.getStringExtra("tipotarj");
        status = intent.getStringExtra("status");
        propina = intent.getStringExtra("propina");
        total = intent.getStringExtra("total");
        msi = intent.getStringExtra("msi");
        aid = intent.getStringExtra("aid");
        arqc = intent.getStringExtra("arqc");
        card_emisor = intent.getStringExtra("emisor");
        trans_id = intent.getIntExtra("id", 0);
        nip = intent.getStringExtra("nip");
        entrada = intent.getStringExtra("entrada");

        approve = intent.getStringExtra("approve");
        ksn_posId = intent.getStringExtra("ksn_posId");

        tp_tv_trans_type = findViewById(R.id.tp_tv_trans_type);
        tp_tv_auth = findViewById(R.id.tp_tv_auth);
        tp_tv_amount = findViewById(R.id.tp_tv_amount);
        tp_tv_tip = findViewById(R.id.tp_tv_tip);
        tp_tv_total = findViewById(R.id.tp_tv_total);
        tp_tv_card = findViewById(R.id.tp_tv_card);
        tp_tv_date_time = findViewById(R.id.tp_tv_date_time);
        tp_tv_approve = findViewById(R.id.tp_tv_approve);
        tp_iv_trans_type = findViewById(R.id.tp_iv_trans_type);
        tp_iv_process = findViewById(R.id.tp_iv_process);
        tp_ll_content_card = findViewById(R.id.tp_ll_content_card);
        tp_tv_tip_label = findViewById(R.id.tp_tv_tip_label);
        tp_tv_total_label = findViewById(R.id.tp_tv_total_label);
        tp_tv_tipotarjeta = findViewById(R.id.tp_tv_tipotarjeta);
        tp_tv_AID = findViewById(R.id.txt_AID);
        tp_tv_ARQC = findViewById(R.id.txt_ARQC);
        lyt_transaction_tip = findViewById(R.id.lyt_transaction_tip);

        tp_tv_amount.setText(subtotal + " MXN");
        tp_tv_tip.setText(propina + " MXN");
        v_months = msi;
        if (status.equals("CAN") || (status.equals("REV") && redtarj.toUpperCase(Locale.ROOT).equals("AMEX"))) {
            if (Integer.parseInt(msi) > 0) {
                tp_tv_total_label.setText(v_months + " MSI");
                tp_tv_amount.setText(GNTBackEnd.Amount_msi(total, v_months) + " MXN");
                tp_tv_trans_type.setText(GNTBackEnd.getTitle(GNTBackEnd.TRANS_CANMSI_TYPE));
            } else {
                tp_tv_trans_type.setText(GNTBackEnd.getTitle(GNTBackEnd.TRANS_CAN_TYPE));
            }
            type_transaction = "Cancelacion";
            tp_iv_trans_type.setImageResource(R.drawable.efevoo_i_grupo_41699);

            tp_tv_trans_type.setTextColor(0xFFCC1818);
            tp_tv_date_time.setTextColor(0xFF121212);
            tp_tv_total_label.setTextColor(0xFF5A5A5A);
            tp_tv_total.setTextColor(0xFF5A5A5A);
            transaction_type = 1;

            Drawable layoutDrawable = tp_ll_content_card.getBackground();
            layoutDrawable = DrawableCompat.wrap(layoutDrawable);
            // the color is a direct color int and not a color resource
            DrawableCompat.setTint(layoutDrawable, 0xFFFDC0C0);
            tp_ll_content_card.setBackground(layoutDrawable);

        } else if (status.equals("VN")) {
            type_transaction = "venta";
            tp_iv_trans_type.setImageResource(R.drawable.efevoo_i_check_exito);
            tp_tv_trans_type.setText(GNTBackEnd.getTitle(GNTBackEnd.TRANS_VEN_TYPE));
            transaction_type = 1;
        } else {
            // v_months=msi;
            type_transaction = GNTBackEnd.TRANS_MSI_TYPE;
            tp_tv_trans_type.setText(GNTBackEnd.getTitle(GNTBackEnd.TRANS_MSI_TYPE));
            tp_tv_total_label.setText(v_months + " MSI");
            tp_tv_amount.setText(GNTBackEnd.Amount_msi(total, v_months) + " MXN");
            transaction_type = 0;
            lyt_transaction_tip.setVisibility(View.GONE);
        }

        if (redtarj.equals("MC")) {
            card_provider = "MASTERCARD";
            tp_iv_process.setImageResource(R.drawable.masterdcard);
        } else if (redtarj.equals("Visa")) {
            card_provider = "VISA";
            tp_iv_process.setImageResource(R.drawable.visa);
        }else if (redtarj.toUpperCase(Locale.ROOT).equals("AMEX")) {
            card_provider = "AMEX";
            tp_iv_process.setImageResource(R.drawable.amex);
        }
        tp_tv_AID.setText(aid);
        tp_tv_ARQC.setText(arqc);
        tp_tv_tipotarjeta.setText("Tarjeta " + tipotarjeta);
        tp_tv_auth.setText(auth);

        tp_tv_total.setText(total + " MXN");
        tp_tv_card.setText("**** " + card);
        tp_tv_date_time.setText(date + " " + time);
        tp_tv_approve.setText(approve);

        dbManager = new DBManager(mContext);
        dbManager.open();
        cursor = dbManager.fetch(ksn_posId);

    }
}
