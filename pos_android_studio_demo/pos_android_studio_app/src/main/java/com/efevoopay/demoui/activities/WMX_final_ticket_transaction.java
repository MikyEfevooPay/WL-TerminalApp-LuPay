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

import android.widget.LinearLayout;
import android.widget.TextView;

import com.action.printerservice.ActionPrinter;
import com.action.printerservice.IPrinterCallback;
import com.action.printerservice.PrintStyle;
import com.efevoopay.demoui.R;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Utils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Locale;

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
    public void onBackPressed() {

    }

    private void onFinish() {
        startActivity(new Intent(mContext, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    private void ticket(ActionPrinter printer) throws RemoteException {
        StringBuilder section_1 = new StringBuilder();
        StringBuilder section_2 = new StringBuilder();
        StringBuilder section_3 = new StringBuilder();
        StringBuilder section_4 = new StringBuilder();
        StringBuilder section_5 = new StringBuilder();
        printer.setPrintStyle(PrintStyle.Key.FONT_SIZE, 22);
        section_1.append(tp_tv_trans_type.getText().toString().toUpperCase(Locale.ROOT));
        section_1.append("\n");
        section_1.append(tp_tv_approve.getText().toString());
        section_1.append("\n\n");
        section_1.append("operadora bp sa de cv".toUpperCase(Locale.ROOT));
        section_1.append("\n");
        section_1.append("GOMEZ MORIN");
        section_1.append("\n");
        section_1.append("SAN PEDRO GARZA GARCIA,");
        section_1.append("\n");
        section_1.append("NUEVO LEON ");
        section_1.append("\n");
        section_1.append("TERMINAL");
        section_1.append("\n");
        section_1.append("123");
        printer.addText(section_1.toString());
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.BOLD);
        printer.addText("__________________________________");
        printer.addText("********"+tp_tv_card.getText().toString());
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.NORMAL);
        section_2.append("VISA");
        section_2.append("\n");
        section_2.append(tp_tv_date_time.getText().toString());
        printer.addText(section_2.toString());
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.BOLD);
        printer.addText("__________________________________");
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.NORMAL);
        printer.addText("");
        section_3.append("Monto :                   "+tp_tv_amount.getText().toString());
        section_3.append("\n");
        section_3.append("Propina :                    "+tp_tv_tip.getText().toString());
        section_3.append("\n");
        section_3.append("Total :                      "+tp_tv_total.getText().toString());
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.BOLD);
        printer.addText(section_3.toString());
        printer.addText("__________________________________");
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.NORMAL);
        printer.addText("");
        section_4.append("RRN :                   0000000000623");
        section_4.append("\n");
        section_4.append("ARQC :                   ************C434");
        section_4.append("\n");
        section_4.append("TC :                   ************750F");
        section_4.append("\n");
        section_4.append("AID :                   A0000000031010");
        printer.setPrintStyle(PrintStyle.Key.FONT_SIZE, 18);
        printer.addText(section_4.toString());
        printer.addText("");
        printer.addText("");
        section_5.append("Por este pagare me obligo");
        section_5.append("\n");
        section_5.append("incondicionalmente a pagar a la orden del");
        section_5.append("\n");
        section_5.append("banco acreditante el importe de este");
        section_5.append("\n");
        section_5.append("título. Este pagare procede del contrato");
        section_5.append("\n");
        section_5.append("de apertura de crédito que el banco");
        section_5.append("\n");
        section_5.append("acreditante y el tarjetahabiente tienen celebrado.");

        printer.setPrintStyle(PrintStyle.Key.FONT_SIZE, 16);
        printer.addText(section_5.toString());
        printer.addText("");
    }

    private void generateTicket(PRINT_TYPE type) {
        ActionPrinter printer = ActionPrinter.getInstance(getApplicationContext());
        try {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.logo_ticket);
            Bitmap bitmap = Utils.drawableToBitmap(drawable);
            printer.addBitmap(bitmap, 100);
            printer.setPrintStyle(PrintStyle.Key.ALIGNMENT, PrintStyle.Alignment.CENTER);
            printer.addText("");
            if(type == PRINT_TYPE.CLIENT) {
                printer.addText("*** COPIA CLIENTE ***");
                printer.addText("");
            }
            ticket(printer);
            printer.lineFeed(5);
            printer.print(new IPrinterCallback.Default() {
                @Override
                public void onPrintStart() throws RemoteException {
                    super.onPrintStart();

                    //todo

                    TRACE.d("onPrintStart");

                }

                @Override
                public void onPrintFinish(int height) throws RemoteException {
                    super.onPrintFinish(height);
                    //todo
                    TRACE.d("onPrintFinish");
                }

                @Override
                public void onError(int error, String message) throws RemoteException {
                    super.onError(error, message);
                    TRACE.d("onError");
                    //todo
                }
            });
        }
        catch (RemoteException e) {
            TRACE.d("RemoteException"+ e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

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
