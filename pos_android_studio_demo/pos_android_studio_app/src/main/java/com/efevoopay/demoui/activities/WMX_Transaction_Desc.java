package com.efevoopay.demoui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.action.printerservice.IPrinterCallback;
import com.dspread.xpos.r;
import com.dspread.helper.printer.PrinterClass;
import com.action.printerservice.ActionPrinter;
import com.action.printerservice.PrintStyle;

import com.efevoopay.demoui.R;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Utils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Locale;

enum PRINT_TYPE {
    STORE, CLIENT
}

public class WMX_Transaction_Desc extends BaseActivity implements View.OnClickListener{

    TextView tp_tv_trans_type,tp_tv_auth,tp_tv_amount,tp_tv_tip,tp_tv_total,tp_tv_card,tp_tv_date_time,tp_tv_approve,tp_tv_tip_label,tp_tv_total_label,tp_tv_tipotarjeta;
    ImageView tp_iv_trans_type,tp_iv_process;
    LinearLayout tp_ll_content_card;
    private String blueTootchAddress = "";
    Context mContext;
    private Intent intent;
    private String ksn_posId;
    AppCompatButton btn_ticket_print;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btn_ticket_print =  (AppCompatButton) findViewById(R.id.btn_ticket_print);
        btn_ticket_print.setOnClickListener(this);
        super.switch_title_logo("Detalle Transacción");
        Intent intent = getIntent();
        mContext=this;
        initData(intent);
        ActionPrinter.getInstance(getApplicationContext()).bind();
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
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_ticket_print:
                new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog_secondary)
                        .setTitle("Impresion de Ticket")
                        //.setIcon(R.drawable.printer)
                        .setPositiveButton("Comercio",(dialog, lis) -> {
                            onPrintStore();
                        })
                        .setNeutralButton("Cliente",(dialog, lis) -> {
                            onPrintClient();
                        })
                        .show();
                break;
        }
    }


    private void onPrintStore() {
        generateTicket(PRINT_TYPE.STORE);
    }

    private void onPrintClient() {
        generateTicket(PRINT_TYPE.CLIENT);
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

    private void initData(Intent intent){
        String auth,date,time,amount,card,redtarj,tipotarjeta,status,propina,total,msi, approve;
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

            Drawable layoutDrawable = tp_ll_content_card.getBackground();
            layoutDrawable = DrawableCompat.wrap(layoutDrawable);
            //the color is a direct color int and not a color resource
            DrawableCompat.setTint(layoutDrawable, 0xFFFDC0C0);
            tp_ll_content_card.setBackground(layoutDrawable);

        }else if (status.equals("VN")){
            tp_iv_trans_type.setImageResource(R.drawable.efevoo_i_check_exito);
            tp_tv_trans_type.setText("Aprobada Venta Normal");
            tp_tv_tip.setText(propina);
        }else{
            tp_tv_trans_type.setText("Aprobada Venta a Meses");
            tp_tv_tip_label.setText("Meses:");
            tp_tv_tip.setText(msi+" MSI");
        }

        if (redtarj.equals("MC")){
            tp_iv_process.setImageResource(R.drawable.masterdcard);
        }else if(redtarj.equals("Visa")){
            tp_iv_process.setImageResource(R.drawable.visa);
        }

        tp_tv_tipotarjeta.setText("Tarjeta "+tipotarjeta);
        tp_tv_auth.setText(auth);
        tp_tv_amount.setText(amount);
        tp_tv_total.setText(total);
        tp_tv_card.setText("**** "+card);
        tp_tv_date_time.setText(date+" "+time);
        tp_tv_approve.setText(approve);
    }
}
