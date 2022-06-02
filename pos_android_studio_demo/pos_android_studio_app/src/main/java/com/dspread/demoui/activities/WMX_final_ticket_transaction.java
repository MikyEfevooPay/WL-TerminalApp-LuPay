package com.dspread.demoui.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Trace;
import android.view.LayoutInflater;
import android.view.View;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.drawable.DrawableCompat;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dspread.demoui.R;
import com.dspread.demoui.utils.TRACE;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class WMX_final_ticket_transaction extends BaseActivity implements View.OnClickListener {

    AppCompatButton btn_ticket_final;
    private LinearLayout ll_btn_open_modal_email;
    Context mContext;
    private String  type_transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().hide();
        setTitle(getString(R.string.wmx_title_welcome));

        mContext=this;


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
                ticket_tv_method_value = findViewById(R.id.ticket_tv_method_value);

        LinearLayout ticket_ll_subtotal = findViewById(R.id.ticket_ll_subtotal);

        Intent intent = getIntent();

        String v_total = intent.getStringExtra("v_total");
        String v_time = intent.getStringExtra("v_time");
        String v_card = intent.getStringExtra("v_card");
        String v_type_transaction = intent.getStringExtra("type_transaction");

        ticket_tv_total_value.setText(v_total);
        ticket_tv_time_value.setText(v_time);
        ticket_tv_card_value.setText(v_card);
        ticket_tv_method_value.setText((v_type_transaction.equals("msi")?"Crédito":"Debito"));

        if (type_transaction.equals("msi")){
            String v_months = intent.getStringExtra("v_months");
            String v_months_total = intent.getStringExtra("v_months_total");

            ticket_tv_title.setText("Resumen de pago a MSI");
            ticket_ll_subtotal.setVisibility(View.GONE);

            ticket_tv_tip_label.setText( v_months+" MSI");
            ticket_tv_tip_value.setText(v_months_total);

        }else if(type_transaction.equals("venta")){
            String v_tip = intent.getStringExtra("v_tip");
            String v_subtotal = intent.getStringExtra("v_subtotal");

            ticket_tv_tip_value.setText(v_tip);
            ticket_tv_subtotal_value.setText(v_subtotal);
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
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_ticket_final:
                startActivity(new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

        modalEmailCreate.show();

        btn_modal_sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modalEmailCreate.dismiss();
                showAlert("success", "¡Ticket enviado con éxito!");
                startActivity(new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }
}
