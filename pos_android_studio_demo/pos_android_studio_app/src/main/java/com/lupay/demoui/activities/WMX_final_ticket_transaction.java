package com.lupay.demoui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.lupay.demoui.R;
import com.lupay.demoui.interfaces.FetchEntity;
import com.lupay.demoui.interfaces.FetchOptions;
import com.lupay.demoui.interfaces.TicketLayoutType;
import com.lupay.demoui.utils.DBManager;
import com.lupay.demoui.utils.Fetch;
import com.lupay.demoui.utils.FetchUIManager;
import com.lupay.demoui.utils.GNTBackEnd;
import com.lupay.demoui.utils.PRINT_TYPE;
import com.lupay.demoui.utils.TRACE;
import com.lupay.demoui.utils.Utils;
import com.lupay.demoui.utils.Ticket;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WMX_final_ticket_transaction extends BaseActivity implements View.OnClickListener {

    AppCompatButton btn_ticket_final;
    private LinearLayout ll_btn_open_modal_email, ticket_ll_propina;
    Context mContext;
    private String type_transaction;
    private boolean isTicketPrinted;
    String v_total, v_time, v_card, v_type_transaction, v_redtarjeta, v_tipotarjeta, v_AID, v_ARQC, v_tip, v_subtotal,
            v_months, v_months_total, card_provider, _noauth, _approve, currEmail, card_emisor, card_nip, card_entrada;
    private int trans_id;
    ProgressDialog loader;
    private String ksn_posId;
    private DBManager dbManager;
    Cursor cursor;
    TextView ticket_tv_tip_label;

    LinearLayout mainView_final_ticket_transaction;

    private final String TRANSACTION_TICKET_SEND_EMAIL = "transaction_ticket_send_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().hide();
        setTitle(getString(R.string.wmx_title_welcome));

        mContext = this;

        loader = Utils.getLoaderSpinner(this, "Enviando...");

        ticket_tv_tip_label = findViewById(R.id.ticket_tv_tip_label);

        btn_ticket_final = (AppCompatButton) findViewById(R.id.btn_ticket_final);
        btn_ticket_final.setOnClickListener(this);
        mainView_final_ticket_transaction = (LinearLayout) findViewById(R.id.mainView_final_ticket_transaction );

        ll_btn_open_modal_email = findViewById(R.id.ll_btn_open_modal_email);
        ll_btn_open_modal_email.setOnClickListener(this);

        Intent intent = getIntent();
        ksn_posId = intent.getStringExtra("ksn_posId");
        type_transaction = intent.getStringExtra("type_transaction");

        dbManager = new DBManager(mContext);
        dbManager.open();
        cursor = dbManager.fetch(ksn_posId);
        setFetchProgressTitle("Enviando...");
        initInfo();

    }

    @Override
    public void addFetchs(FetchUIManager manager) throws Exception {
        Fetch history = manager.addFetch(TRANSACTION_TICKET_SEND_EMAIL,
                new FetchOptions(Utils.TERMINAL_BATCH + "/api/correo/ticket", Request.Method.POST));
        history.setSetBodyListenner(this::getBody);
    }

    private void getBody(JSONObject body) throws JSONException {
        body.put("correo", currEmail);
        if (type_transaction.equals(GNTBackEnd.TRANS_VEN_TYPE)) {
            body.put("subject", "Ticket de compra");
            body.put("tipo", "sale");
        } else if (type_transaction.equals(GNTBackEnd.TRANS_CAN_TYPE)) {
            body.put("subject", "Ticket de Cancelación");
            body.put("tipo", "cancel");
        } else if (type_transaction.equals(GNTBackEnd.TRANS_MSI_TYPE)) {
            body.put("subject", "Ticket MSI");
            body.put("tipo", "msi");
        }
        DateFormat formatemail = new SimpleDateFormat("ddMMyyHHmmss");
        Date datemail = new Date();
        body.put("idemail", "Ticket" + formatemail.format(datemail).toString());
        body.put("comercio", Utils.isNull(cursor.getString(9), "N/A"));
        body.put("msi", Utils.isNull(v_months, "0"));
        body.put("amount", Utils.isNull(v_subtotal, "N/A"));
        body.put("tip", Utils.isNull(v_tip, "N/A"));
        body.put("total", Utils.isNull(v_total, "N/A"));
        body.put("pay_method", Utils.isNull(v_tipotarjeta + "/" + card_emisor + "/" + card_provider, "N/A"));
        body.put("card", Utils.isNull(v_card, "N/A"));
        body.put("payment_date", Utils.isNull(v_time, "N/A"));
        body.put("idrecibo", Utils.isNull(trans_id, "N/A"));
        body.put("afiliacion", Utils.isNull(cursor.getString(26), "N/A"));
        body.put("autorizacion", Utils.isNull(_approve, "N/A"));
        body.put("address", Utils.isNull(cursor.getString(8), "N/A"));
        body.put("kpos_id", Utils.isNull(ksn_posId, "N/A"));
        body.put("arqc", Utils.isNull(Utils.maskText(v_ARQC, 4), "N/A"));
        body.put("aid", Utils.isNull(Utils.maskText(v_AID, 4), "N/A"));
        body.put("singtype", Utils.isNull(tipofirma(card_nip, card_entrada), ""));
        body.put("wl_name", Utils.TERMINAL_WL_Bucket);
    }

    @Override
    public void onFetchCurrentResult(FetchEntity entity, @Nullable FetchEntity error) {
        super.onFetchCurrentResult(entity, error);
        if (entity.result == null)
            return;
        switch (entity.key) {
            case TRANSACTION_TICKET_SEND_EMAIL:
                showAlert("success", "¡Ticket enviado con éxito!");
                break;
            default:
                break;
        }
    }

    private void initInfo() {

        TextView ticket_tv_title = findViewById(R.id.ticket_tv_title),
                ticket_tv_tip_value = findViewById(R.id.ticket_tv_tip_value),
                ticket_tv_subtotal_value = findViewById(R.id.ticket_tv_subtotal_value),
                ticket_tv_total_value = findViewById(R.id.ticket_tv_total_value),
                ticket_tv_time_value = findViewById(R.id.ticket_tv_time_value),
                ticket_tv_card_value = findViewById(R.id.ticket_tv_card_value),
                ticket_tv_method_value = findViewById(R.id.ticket_tv_method_value),
                txt_AID = findViewById(R.id.txt_AID),
                txt_ARQC = findViewById(R.id.txt_ARQC),
                textView16 = findViewById(R.id.textView16),
                ticket_tv_subtotal_label = findViewById(R.id.ticket_tv_subtotal_label);

        ticket_ll_propina = findViewById(R.id.ticket_ll_propina);

        Intent thisintent = getIntent();

        v_total = thisintent.getStringExtra("v_total");
        v_time = thisintent.getStringExtra("v_time");
        v_card = thisintent.getStringExtra("v_card");
        v_type_transaction = thisintent.getStringExtra("type_transaction");
        v_redtarjeta = thisintent.getStringExtra("v_redtarjeta");
        v_tipotarjeta = thisintent.getStringExtra("v_tipotarjeta");
        v_AID = thisintent.getStringExtra("v_AID");
        v_ARQC = thisintent.getStringExtra("v_ARQC");
        _noauth = thisintent.getStringExtra("v_noauth");
        _approve = thisintent.getStringExtra("v_approve");
        card_emisor = thisintent.getStringExtra("v_emisor");
        card_nip = thisintent.getStringExtra("v_nip");
        card_entrada = thisintent.getStringExtra("v_entrada");
        trans_id = thisintent.getIntExtra("v_trans_id", 0);
        ticket_tv_total_value.setText(v_total);
        ticket_tv_time_value.setText(v_time);
        ticket_tv_card_value.setText(v_card);
        ticket_tv_method_value.setText(v_tipotarjeta);
        txt_AID.setText(v_AID);
        txt_ARQC.setText(v_ARQC);

        v_months = thisintent.getStringExtra("v_months");

        if (type_transaction.equals(GNTBackEnd.TRANS_MSI_TYPE)) {
            v_months_total = thisintent.getStringExtra("v_months_total");

            ticket_tv_title.setText("Resumen de pago a MSI");
            ticket_ll_propina.setVisibility(View.GONE);

            // ticket_tv_tip_label.setText( v_months+" MSI");
            // ticket_tv_tip_value.setText(v_months_total);
            ticket_tv_subtotal_label.setText(v_months + " MSI");
            ticket_tv_subtotal_value.setText(v_months_total);

            v_tip = "$0.00 MXN";
            v_subtotal = v_months_total;

        } else if (type_transaction.equals(GNTBackEnd.TRANS_VEN_TYPE)) {
            v_tip = thisintent.getStringExtra("v_tip");
            v_subtotal = thisintent.getStringExtra("v_subtotal");

            ticket_tv_tip_value.setText(v_tip);
            ticket_tv_subtotal_value.setText(v_subtotal);
        } else if (type_transaction.equals(GNTBackEnd.TRANS_CAN_TYPE)) {
            textView16.setText("Cancelación aprobada");
            v_subtotal = thisintent.getStringExtra("v_subtotal");
            v_tip = thisintent.getStringExtra("v_tip");

            mainView_final_ticket_transaction.setBackground(ContextCompat.getDrawable(mContext,R.drawable.ticket_cancel_background));

            if (Integer.parseInt(v_months) > 0) {
                v_type_transaction = GNTBackEnd.TRANS_CANMSI_TYPE;
                ticket_tv_title.setText("Resumen de cancelación a MSI");
                ticket_tv_subtotal_label.setText(v_months + " MSI");
                ticket_tv_subtotal_value.setText(v_subtotal);
                ticket_ll_propina.setVisibility(View.GONE);
            } else {
                ticket_tv_title.setText("Resumen de cancelación");
            }
            ticket_tv_tip_value.setText(v_tip);
            ticket_tv_subtotal_value.setText(v_subtotal);
        }

        if (v_redtarjeta.toUpperCase(Locale.ROOT).equals("MC")) {
            card_provider = "MASTERCARD";
        } else if (v_redtarjeta.toUpperCase(Locale.ROOT).equals("VISA")) {
            card_provider = "VISA";
        }else if (v_redtarjeta.toUpperCase(Locale.ROOT).equals("AMEX")) {
            card_provider = "AMEX";
        }
    }

    @Override
    public void onToolbarLinstener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_final_ticket_transaction;
    }

    @Override
    public TicketLayoutType getPrintLayout() {
        return TicketLayoutType.TRANSACTION;
    }

    @Override
    public void setTicketData(Ticket ticket) {
        ticket.setTrans_Type(GNTBackEnd.getTitle(v_type_transaction))
                .setCard(v_card)
                .setCardType(v_tipotarjeta)
                .setCard_provider(card_provider)
                .setStatus("APROBADA")
                .setApprove(Utils.isNull(_approve, ""))
                .setDate_Time(v_time)
                .setAmount(v_subtotal)
                .setTip(v_tip)
                .setTotal(v_total)
                .setARQC(v_ARQC)
                .setAID(v_AID)
                .setKsn_posId(ksn_posId)
                .setCursor(cursor)
                .setMsi(v_months)
                .setCard_emisor(card_emisor)
                .setCard_nip(card_nip)
                .setCard_entrada(card_entrada)
                .setCard_singtype(tipofirma(card_nip, card_entrada))
                .setTransId(trans_id);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onPrintFinished(boolean isSuccess, PRINT_TYPE print_type, TicketLayoutType layoutType) {
        super.onPrintFinished(isSuccess, print_type, layoutType);
        if (print_type == PRINT_TYPE.STORE) {
            showConfirmClientTicketDialog();
        } else if (print_type == PRINT_TYPE.CLIENT) {
            startActivity(new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    @Override
    public void onPrintError(boolean isSuccess, String status, PRINT_TYPE print_type, TicketLayoutType layoutType) {
        super.onPrintError(isSuccess, status, print_type, layoutType);
        TRACE.d("print_type " + print_type.toString());
        if (print_type == PRINT_TYPE.STORE) {
            showConfirmClientTicketDialog();
        } else if (print_type == PRINT_TYPE.CLIENT) {
            runOnUiThread(
                    () -> startActivity(new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        }
    }

    private void onFinish() {
        if (!isTicketPrinted) {
            isTicketPrinted = true;
            PrintTicket(PRINT_TYPE.STORE);
            return;
        }
        showConfirmClientTicketDialog();
    }

    private void showConfirmClientTicketDialog() {
        MaterialAlertDialogBuilder confirm = new MaterialAlertDialogBuilder(this,
                R.style.ThemeOverlay_App_MaterialAlertDialog_secondary)
                .setTitle("¿Imprimir copia del ticket al cliente?")
                .setIcon(R.drawable.copia_ticket)
                .setPositiveButton("Sí", (dialog, lis) -> {
                    dialog.dismiss();
                    PrintTicket(PRINT_TYPE.CLIENT);
                })
                .setNeutralButton("No", (dialog, lis) -> {
                    startActivity(new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                });

        runOnUiThread(() -> confirm.show());
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_ticket_final:
                if (Build.MODEL.equals("D30")||Build.MODEL.equals("D60")) {
                    onFinish();
                } else {
                    startActivity(new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                break;

            case R.id.ll_btn_open_modal_email:
                TRACE.d("click email button");
                openModalSendEmail();
                break;
            default:

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
            modalEmailCreate.dismiss();
            getFetchManager().CallById(TRANSACTION_TICKET_SEND_EMAIL);
        });
    }

}
