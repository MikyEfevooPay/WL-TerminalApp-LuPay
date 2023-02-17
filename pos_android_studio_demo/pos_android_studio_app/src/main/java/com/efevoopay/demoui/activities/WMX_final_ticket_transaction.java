package com.efevoopay.demoui.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;

import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.action.printerservice.ActionPrinter;
import com.action.printerservice.IPrinterCallback;
import com.action.printerservice.PrintStyle;
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
import com.efevoopay.demoui.utils.PRINT_TYPE;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Utils;
import com.efevoopay.demoui.utils.Ticket;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class WMX_final_ticket_transaction extends BaseActivity implements View.OnClickListener {

    AppCompatButton btn_ticket_final;
    private LinearLayout ll_btn_open_modal_email;
    Context mContext;
    private String  type_transaction;
    private int transaction_type;
    String v_total, v_time, v_card, v_type_transaction, v_redtarjeta, v_tipotarjeta, v_AID, v_ARQC, v_tip, v_subtotal, v_months, v_months_total, card_provider;
    private Ticket ticket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().hide();
        setTitle(getString(R.string.wmx_title_welcome));

        mContext=this;

        ticket = new Ticket(this);


        btn_ticket_final =  (AppCompatButton) findViewById(R.id.btn_ticket_final);
        btn_ticket_final.setOnClickListener(this);

        ll_btn_open_modal_email = findViewById(R.id.ll_btn_open_modal_email);
        ll_btn_open_modal_email.setOnClickListener(this);

        Intent intent = getIntent();
        type_transaction = intent.getStringExtra("type_transaction");
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

        transaction_type = v_type_transaction.equals("msi") ? 0 : 1;

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

        }else if(type_transaction.equals("venta")){
            v_tip = intent.getStringExtra("v_tip");
            v_subtotal = intent.getStringExtra("v_subtotal");

            ticket_tv_tip_value.setText(v_tip);
            ticket_tv_subtotal_value.setText(v_subtotal);
        }else if(type_transaction.equals("Cancelacion")){
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
    public void onBackPressed() {

    }



    private void onFinish() {
        ticket.GenerateTicket(PRINT_TYPE.STORE, transaction_type);
        new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog_secondary)
                .setTitle("¿Imprimir copia del ticket al cliente?")
                .setIcon(R.drawable.printer)
                .setPositiveButton("Si",(dialog, lis) -> {
                    ticket.GenerateTicket(PRINT_TYPE.CLIENT, transaction_type);
                    startActivity(new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                })
                .setNeutralButton("No",(dialog, lis) -> {
                    startActivity(new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                })
                .show();
    }

    @Override
    public void onClick(View view) {
        ticket.setData(v_type_transaction,
                v_tipotarjeta, v_card,
                card_provider, v_time,
                v_subtotal,
                v_tip,
                v_total,
                v_ARQC,
                v_AID);

        switch (view.getId()){
            case R.id.btn_ticket_final:
                onFinish();
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
        EditText txt_email = dialogContentView.findViewById(R.id.editTextTextPersonName2);

        AlertDialog modalEmailCreate = modalEmail.create();

        modalEmailCreate.show();

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

                startActivity(new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }


    private void setCorreo(String _correo)throws IOException {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "https://efevoopayloadbalancer-ecommerce.com/matriz/certificacion/correo";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("correo", _correo);
            jsonBody.put("body", ticket.getTicketString(1));
            final String requestBody = jsonBody.toString();
            TRACE.d("requestBody " +  TRACE.NEW_LINE + requestBody );
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    TRACE.d("** ResponseResult " +  TRACE.NEW_LINE + response.toString() );
                    showAlert("success", "¡Ticket enviado con éxito!");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + error.toString() );
                    showAlert("ERROR",  error.toString());
                    //startActivity(new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

