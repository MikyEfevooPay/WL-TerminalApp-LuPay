package com.efevoopay.demoui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import com.efevoopay.demoui.utils.DBManager;
import com.efevoopay.demoui.utils.PRINT_TYPE;
import com.efevoopay.demoui.utils.SQLiteTpv;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Ticket;
import com.efevoopay.demoui.utils.Utils;

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


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
    private String ksn_posId, totalamount, date, corte, tip, TableRowsString;
    private Context mContext;
    private ProgressDialog loader;
    private Button btn_cortecaja_final;
    private TextView txt_totalamount, txt_datetime, txt_subtotal, txt_tip;
    private LinearLayout lyt_cortecaja_email, lyt_cortecaja_print;
    private Ticket ticket;
    private final WMX_llamada_dukpt jsondukpt = new WMX_llamada_dukpt();
    private DBManager dbManager;
    Cursor cursor;
    private CORTE_CAJA_TYPE type;
    private boolean _final;

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
        ticket = new Ticket(mContext);

        loader = Utils.getLoaderSpinner(mContext, "Enviando...");
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
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.lyt_cortecaja_email:
                openModalSendEmail();
                break;
            case R.id.lyt_cortecaja_print:
                printTicket();
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

    private void printTicket() {
        ticket.setData("Corte de Caja","",totalamount, corte, tip, date, cursor, ksn_posId);
        ticket.GenerateTicket(PRINT_TYPE.RESUME);
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

    private void setCorreo(String _correo) throws IOException {
        loader.show();
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = Utils.TERMINAL_API + "/matriz/certificacion/correocortecaja";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("correo", _correo);
            jsonBody.put("subject", "Corte de caja");
            jsonBody.put("comercio", Utils.isNull(cursor.getString(9), "N/A"));
            jsonBody.put("subtotal", Utils.isNull(corte, "N/A"));
            jsonBody.put("propina", Utils.isNull( tip, "N/A"));
            jsonBody.put("montototal", Utils.isNull(totalamount, "N/A"));
            jsonBody.put("fechacorte", Utils.isNull(date, "N/A"));
            jsonBody.put("tablerows",Utils.isNull(TableRowsString, "[]"));
            final String requestBody = jsonBody.toString();
            TRACE.d("requestBody " + TRACE.NEW_LINE + requestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    loader.dismiss();
                    TRACE.d("** ResponseResult " + TRACE.NEW_LINE + response.toString());
                    showAlert("success", "¡Corte caja enviado con éxito!");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loader.dismiss();
                    TRACE.d("** ResponseResult ERROR " + TRACE.NEW_LINE + error.toString());
                    showAlert("error", "¡Correo no enviado!");
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
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody,
                                "utf-8");
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
                        responseString = parsed;
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }

            };
            requestQueue.add(stringRequest);
        } catch (JSONException e) {

            TRACE.d("** ResponseResult ERROR " + TRACE.NEW_LINE + e.toString());

        }
    }


}
