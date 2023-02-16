package com.efevoopay.demoui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.action.printerservice.IPrinterCallback;
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
import com.dspread.xpos.r;
import com.dspread.helper.printer.PrinterClass;
import com.action.printerservice.ActionPrinter;
import com.action.printerservice.PrintStyle;

import com.efevoopay.demoui.R;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Utils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.efevoopay.demoui.utils.PRINT_TYPE;
import com.efevoopay.demoui.utils.Ticket;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.Contract;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;


public class WMX_Transaction_Desc extends BaseActivity implements View.OnClickListener{

    TextView tp_tv_trans_type,tp_tv_auth,tp_tv_amount,tp_tv_tip,tp_tv_total,tp_tv_card,tp_tv_date_time,tp_tv_approve,tp_tv_tip_label,tp_tv_total_label,tp_tv_tipotarjeta,tp_tv_AID,tp_tv_ARQC;
    ImageView tp_iv_trans_type,tp_iv_process;
    LinearLayout tp_ll_content_card;
    private int transaction_type;
    private String blueTootchAddress = "", card_provider;
    private Ticket ticket;
    Context mContext;
    private Intent intent;
    private String ksn_posId;
    AppCompatButton btn_ticket_print;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Button print_button = (Button) findViewById(R.id.btn_print);
        print_button.setOnClickListener(this);
        mContext = this;
        super.switch_title_logo("Detalle Transacción");
        Intent intent = getIntent();
        ticket = new Ticket(getApplicationContext());
        initData(intent);
    }

    @Override
    public void onToolbarLinstener() {
        onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_transaction_prev;
    }

    private void onFinish() {
        new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog_secondary)
                .setTitle("Impresion de Ticket")
                .setIcon(R.drawable.printer)
                .setPositiveButton("Comercio",(dialog, lis) -> {
                    ticket.GenerateTicket(PRINT_TYPE.STORE, transaction_type);
                })
                .setNeutralButton("Cliente",(dialog, lis) -> {
                    ticket.GenerateTicket(PRINT_TYPE.CLIENT, transaction_type);
                })
                .show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_print:
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

        AppCompatButton btn_modal_sendEmail = (AppCompatButton) dialogContentView.findViewById(R.id.btn_modal_sendEmail);

        AlertDialog modalEmailCreate = modalEmail.create();

        EditText txtcorreo = (EditText) dialogContentView.findViewById(R.id.editTextTextPersonName2);

        modalEmailCreate.show();

        btn_modal_sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setCorreo(txtcorreo.getText().toString());
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


    private void initData(Intent intent){
        String auth,date,time,amount,card,redtarj,tipotarjeta,status,propina,total,msi,aid,arqc, approve;
        auth = intent.getStringExtra("auth");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        amount = intent.getStringExtra("amount");
        card = intent.getStringExtra("card");
        redtarj = intent.getStringExtra("redtarj");
        tipotarjeta = intent.getStringExtra("tipotarj");
        status = intent.getStringExtra("status");
        propina=intent.getStringExtra("propina");
        total=intent.getStringExtra("total");
        msi=intent.getStringExtra("msi");
        aid=intent.getStringExtra("aid");
        arqc=intent.getStringExtra("arqc");

        approve = intent.getStringExtra("approve");
        ksn_posId=intent.getStringExtra("ksn_posId");

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
        tp_tv_tip_label =findViewById(R.id.tp_tv_tip_label);
        tp_tv_total_label = findViewById(R.id.tp_tv_total_label);
        tp_tv_tipotarjeta = findViewById(R.id.tp_tv_tipotarjeta);
        tp_tv_AID=findViewById(R.id.txt_AID);
        tp_tv_ARQC=findViewById(R.id.txt_ARQC);
        aid=intent.getStringExtra("aid");
        arqc=intent.getStringExtra("arqc");

        if(status.equals("CAN")){
            tp_tv_tip.setText(propina);
            if (Integer.parseInt(msi)>0){
                tp_tv_tip.setText(msi+" MSI");
            }
            tp_iv_trans_type.setImageResource(R.drawable.efevoo_i_grupo_41699);
            tp_tv_trans_type.setText("Cancelada Venta Normal");
            tp_tv_trans_type.setTextColor(0xFFCC1818);
            tp_tv_date_time.setTextColor(0xFF121212);
            tp_tv_total_label.setTextColor(0xFF5A5A5A);
            tp_tv_total.setTextColor(0xFF5A5A5A);
            transaction_type = 1;

            Drawable layoutDrawable = tp_ll_content_card.getBackground();
            layoutDrawable = DrawableCompat.wrap(layoutDrawable);
            //the color is a direct color int and not a color resource
            DrawableCompat.setTint(layoutDrawable, 0xFFFDC0C0);
            tp_ll_content_card.setBackground(layoutDrawable);

        }else if (status.equals("VN")){
            tp_iv_trans_type.setImageResource(R.drawable.efevoo_i_check_exito);
            tp_tv_trans_type.setText("Aprobada Venta Normal");
            tp_tv_tip.setText(propina);
            transaction_type = 1;

        }else{
            tp_tv_trans_type.setText("Aprobada Venta a Meses");
            tp_tv_tip_label.setText("Meses:");
            tp_tv_tip.setText(msi+" MSI");
            transaction_type = 0;
        }

        if (redtarj.equals("MC")){
            card_provider = "MASTERCARD";
            tp_iv_process.setImageResource(R.drawable.masterdcard);
        }else if(redtarj.equals("Visa")){
            card_provider = "VISA";
            tp_iv_process.setImageResource(R.drawable.visa);
        }
        tp_tv_AID.setText(aid);
        tp_tv_ARQC.setText(arqc);
        tp_tv_tipotarjeta.setText("Tarjeta "+tipotarjeta);
        tp_tv_auth.setText(auth);
        tp_tv_AID.setText(aid);
        tp_tv_ARQC.setText(arqc);
        tp_tv_amount.setText(amount);
        tp_tv_total.setText(total);
        tp_tv_card.setText("**** "+card);
        tp_tv_date_time.setText(date+" "+time);
        tp_tv_approve.setText(approve);

        ticket.setData(tp_tv_trans_type.getText().toString(),
                tp_tv_approve.getText().toString(), tp_tv_card.getText().toString(),
                card_provider, tp_tv_date_time.getText().toString(),
                tp_tv_amount.getText().toString(),
                tp_tv_tip.getText().toString(),
                tp_tv_total.getText().toString(),
                tp_tv_ARQC.getText().toString(),
                tp_tv_AID.getText().toString()
        );
    }
}
