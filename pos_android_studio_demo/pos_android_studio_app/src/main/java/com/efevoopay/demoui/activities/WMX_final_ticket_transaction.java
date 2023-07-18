package com.efevoopay.demoui.activities;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.efevoopay.demoui.R;
import com.efevoopay.demoui.interfaces.TicketLayoutType;
import com.efevoopay.demoui.utils.DBManager;
import com.efevoopay.demoui.utils.GNTBackEnd;
import com.efevoopay.demoui.utils.PRINT_TYPE;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Utils;
import com.efevoopay.demoui.utils.Ticket;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class WMX_final_ticket_transaction extends BaseActivity implements View.OnClickListener {

    AppCompatButton btn_ticket_final;
    private LinearLayout ll_btn_open_modal_email;
    Context mContext;
    private String  type_transaction;
    private boolean isTicketPrinted;
    String v_total, v_time, v_card, v_type_transaction, v_redtarjeta, v_tipotarjeta, v_AID, v_ARQC, v_tip, v_subtotal, v_months, v_months_total, card_provider;
    ProgressDialog loader;
    private String ksn_posId;
    private DBManager dbManager;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().hide();
        setTitle(getString(R.string.wmx_title_welcome));

        mContext=this;

        loader = Utils.getLoaderSpinner(this, "Enviando...");

        btn_ticket_final =  (AppCompatButton) findViewById(R.id.btn_ticket_final);
        btn_ticket_final.setOnClickListener(this);

        ll_btn_open_modal_email = findViewById(R.id.ll_btn_open_modal_email);
        ll_btn_open_modal_email.setOnClickListener(this);

        Intent intent = getIntent();
        ksn_posId = intent.getStringExtra("ksn_posId");
        type_transaction = intent.getStringExtra("type_transaction");

        dbManager = new DBManager(mContext);
        dbManager.open();
        cursor = dbManager.fetch(ksn_posId);

        initInfo();

    }

    private void initInfo(){

        TextView ticket_tv_title = findViewById(R.id.ticket_tv_title),
                ticket_tv_tip_label = findViewById(R.id.ticket_tv_tip_label),
                ticket_tv_tip_value = findViewById(R.id.ticket_tv_tip_value),
                ticket_tv_subtotal_value = findViewById(R.id.ticket_tv_subtotal_value),
                ticket_tv_total_value = findViewById(R.id.ticket_tv_total_value),
                ticket_tv_time_value = findViewById(R.id.ticket_tv_time_value),
                ticket_tv_card_value = findViewById(R.id.ticket_tv_card_value),
                ticket_tv_method_value = findViewById(R.id.ticket_tv_method_value),
                txt_AID = findViewById(R.id.txt_AID),
                txt_ARQC = findViewById(R.id.txt_ARQC),
                textView16=findViewById(R.id.textView16);

        LinearLayout ticket_ll_subtotal = findViewById(R.id.ticket_ll_subtotal);

        Intent intent = getIntent();

        v_total = intent.getStringExtra("v_total");
        v_time = intent.getStringExtra("v_time");
        v_card = intent.getStringExtra("v_card");
        v_type_transaction = intent.getStringExtra("type_transaction");
        v_redtarjeta = intent.getStringExtra("v_redtarjeta");
        v_tipotarjeta = intent.getStringExtra("v_tipotarjeta");
        v_AID = intent.getStringExtra("v_AID");
        v_ARQC = intent.getStringExtra("v_ARQC");


        ticket_tv_total_value.setText(v_total);
        ticket_tv_time_value.setText(v_time);
        ticket_tv_card_value.setText(v_card);
        ticket_tv_method_value.setText(v_tipotarjeta);
        txt_AID.setText(v_AID);
        txt_ARQC.setText(v_ARQC);

        if (type_transaction.equals("msi")){
            v_months = intent.getStringExtra("v_months");
            v_months_total = intent.getStringExtra("v_months_total");

            ticket_tv_title.setText("Resumen de pago a MSI");
            ticket_ll_subtotal.setVisibility(View.GONE);

            ticket_tv_tip_label.setText( v_months+" MSI");
            ticket_tv_tip_value.setText(v_months_total);

        }else if(type_transaction.equals(GNTBackEnd.TRANS_VEN_TYPE)){
            v_tip = intent.getStringExtra("v_tip");
            v_subtotal = intent.getStringExtra("v_subtotal");

            ticket_tv_tip_value.setText(v_tip);
            ticket_tv_subtotal_value.setText(v_subtotal);
        }else if(type_transaction.equals(GNTBackEnd.TRANS_CAN_TYPE)){
            ticket_tv_title.setText("Resumen de cancelación");
            textView16.setText("Cancelación aprobada");
            v_tip = intent.getStringExtra("v_tip");
            v_subtotal = intent.getStringExtra("v_subtotal");

            ticket_tv_tip_value.setText(v_tip);
            ticket_tv_subtotal_value.setText(v_subtotal);
        }

        if (v_redtarjeta.equals("MC")){
            card_provider = "MASTERCARD";
        }else if(v_redtarjeta.equals("Visa")){
            card_provider = "VISA";
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
                .setApprove(v_tipotarjeta)
                .setCard(v_card)
                .setCardType(card_provider)
                .setDate_Time(v_time)
                .setAmount(v_subtotal)
                .setTip(v_tip)
                .setTotal(v_total)
                .setARQC(v_ARQC)
                .setAID(v_AID)
                .setKsn_posId(ksn_posId)
                .setCursor(cursor);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onPrintFinished(boolean isSuccess, PRINT_TYPE print_type, TicketLayoutType layoutType) {
       super.onPrintFinished(isSuccess, print_type, layoutType);
       if(print_type == PRINT_TYPE.STORE) {
           showConfirmClientTicketDialog();
       } else if(print_type == PRINT_TYPE.CLIENT) {
           startActivity(new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
       }
    }

    @Override
    public void onPrintError(boolean isSuccess, String status, PRINT_TYPE print_type, TicketLayoutType layoutType) {
        super.onPrintError(isSuccess, status, print_type, layoutType);
        TRACE.d("print_type " + print_type.toString());
        if(print_type == PRINT_TYPE.STORE) {
            showConfirmClientTicketDialog();
        } else if(print_type == PRINT_TYPE.CLIENT) {
            runOnUiThread(() -> startActivity(new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        }
    }

    private void onFinish() {
        if(!isTicketPrinted) {
            isTicketPrinted = true;
            PrintTicket(PRINT_TYPE.STORE);
            return;
        }
        showConfirmClientTicketDialog();
    }

    private void showConfirmClientTicketDialog() {
        MaterialAlertDialogBuilder confirm =   new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog_secondary)
                .setTitle("¿Imprimir copia del ticket al cliente?")
                .setIcon(R.drawable.printer)
                .setPositiveButton("Sí",(dialog, lis) -> {
                    PrintTicket(PRINT_TYPE.CLIENT);
                })
                .setNeutralButton("No",(dialog, lis) -> {
                    startActivity(new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                });

        runOnUiThread(() -> confirm.show());
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()){
            case R.id.btn_ticket_final:
                if(Build.MODEL.equals("D30")){
                    onFinish();
                }else{
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

    private void openModalSendEmail(){
        LayoutInflater inflater=getLayoutInflater();
        View dialogContentView =inflater.inflate(R.layout.wmx_modal_email_input, null);

        MaterialAlertDialogBuilder modalEmail = new MaterialAlertDialogBuilder(mContext, R.style.ThemeOverlay_App_MaterialAlertDialog);
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
                if(isValidEmail != currEnableState) {
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

                try {
                    setCorreo(txt_email.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    modalEmailCreate.dismiss();
                }
            }
        });
    }


    private String getHTMLEmailTicketTemplate() {
        StringBuilder strBulider = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(getAssets().open("email_ticket_template.html")));
            String html;
            while((html = in.readLine()) != null) {
                strBulider.append(html);
            }
            in.close();
            return strBulider.toString();
        } catch (IOException e) {
            return "";
        }
    }


    private void setCorreo(String _correo)throws IOException {
        loader.show();
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = Utils.TERMINAL_API + "/matriz/certificacion/correoticket";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("correo", _correo);
            if(type_transaction.equals(GNTBackEnd.TRANS_VEN_TYPE)){
                jsonBody.put("subject","Ticket de compra");
                jsonBody.put("tipo", "V");
            }else{
                jsonBody.put("subject","Ticket de Cancelación");
                jsonBody.put("tipo", "C");
            }
            jsonBody.put("comercio", Utils.isNull(cursor.getString(9), "N/A"));
            jsonBody.put("amount", Utils.isNull(v_subtotal, "N/A"));
            jsonBody.put("tip", Utils.isNull( v_tip, "N/A"));
            jsonBody.put("total", Utils.isNull(v_total, "N/A"));
            jsonBody.put("pay_method", Utils.isNull(card_provider, "N/A"));
            jsonBody.put("card", Utils.isNull(v_card, "N/A"));
            jsonBody.put("payment_date", Utils.isNull(v_time, "N/A"));
            jsonBody.put("address", Utils.isNull(cursor.getString(8), "N/A"));
            jsonBody.put("kpos_id", Utils.isNull(ksn_posId, "N/A"));
            jsonBody.put("arqc", Utils.isNull(v_ARQC, "N/A"));
            jsonBody.put("aid", Utils.isNull(v_AID, "N/A"));
            final String requestBody = jsonBody.toString();
            TRACE.d("requestBody " +  TRACE.NEW_LINE + requestBody );
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    loader.dismiss();
                    TRACE.d("** ResponseResult " +  TRACE.NEW_LINE + response.toString() );
                    showAlert("success", "¡Ticket enviado con éxito!");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loader.dismiss();
                    TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + error.toString() );
                    showAlert("ERROR",  "¡Ticket no enviado!");
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    String parsed;
                    try {
                        parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    } catch (UnsupportedEncodingException var4) {
                        parsed = new String(response.data);
                    }

                    if (response != null) {
                        responseString = String.valueOf(parsed);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }

            };
            requestQueue.add(stringRequest);
        } catch (JSONException e) {

            TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + e.toString() );

        }


    }

}

