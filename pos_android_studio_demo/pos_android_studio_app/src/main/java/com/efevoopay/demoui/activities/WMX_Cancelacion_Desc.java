package com.efevoopay.demoui.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.drawable.DrawableCompat;

import com.efevoopay.demoui.R;
import com.efevoopay.demoui.interfaces.TicketLayoutType;
import com.efevoopay.demoui.utils.DBManager;
import com.efevoopay.demoui.utils.GNTBackEnd;
import com.efevoopay.demoui.utils.GlobalFunctions;
import com.efevoopay.demoui.utils.Ticket;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.NumberFormat;
import java.util.Locale;

public class WMX_Cancelacion_Desc extends BaseActivity  {
    TextView cp_tv_trans_type,cp_tv_auth,cp_tv_amount,cp_tv_tip,cp_tv_total,cp_tv_card,cp_tv_date_time,cp_tv_approve,cp_tv_tip_label,cp_tv_total_label,cp_tv_tipotarjeta,cp_tv_aid,cp_tv_arqc,tp_tv_amount;
    ImageView cp_iv_trans_type,cp_iv_process;
    LinearLayout cp_ll_content_card,ll_msi;
    AppCompatButton cp_btn_trans_cancelar, cp_btn_trans_final;
    Context mContext;
    private String card_provider, tipotarjeta;
    private int transaction_type;
    private Intent intent;
    private String ksn_posId;
    private String meses;
    private DBManager dbManager;
    Cursor cursor;
    private GlobalFunctions gf ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.switch_title_logo("Detalle Transacción");
        Intent intent = getIntent();
        mContext=this;

        initData(intent);
        dbManager = new DBManager(mContext);
        dbManager.open();
        cursor = dbManager.fetch(ksn_posId);
        buttonListener();
    }

    @Override
    public void onToolbarLinstener() {
        onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_cancelacion_prev;
    }

    @Override
    public TicketLayoutType getPrintLayout() {
        return TicketLayoutType.TRANSACTION;
    }


    @Override
    public void setTicketData(Ticket ticket) {
        ticket.setTrans_Type(cp_tv_trans_type.getText().toString())
                .setStatus("APROBADA")
                .setApprove(cp_tv_approve.getText().toString())
                .setCard(cp_tv_card.getText().toString())
                .setCardType(card_provider)
                .setCard_provider(tipotarjeta)
                .setDate_Time(cp_tv_date_time.getText().toString())
                .setAmount(cp_tv_amount.getText().toString())
                .setTip(cp_tv_tip.getText().toString())
                .setTotal(cp_tv_total.getText().toString())
                .setARQC(cp_tv_arqc.getText().toString())
                .setAID(cp_tv_aid.getText().toString())
                .setKsn_posId(ksn_posId)
                .setCursor(cursor);
    }

    private void initData(Intent intent){
        String auth,date,time,subtotal,card,redtarj,status,propina,total,msi,aid,arqc, approve;
        auth = intent.getStringExtra("auth");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        subtotal = intent.getStringExtra("subtotal");
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
        meses=msi;


        cp_tv_trans_type = findViewById(R.id.cp_tv_trans_type);
        cp_tv_auth = findViewById(R.id.cp_tv_auth);
        cp_tv_amount = findViewById(R.id.cp_tv_amount);
        cp_tv_tip = findViewById(R.id.cp_tv_tip);
        cp_tv_total = findViewById(R.id.cp_tv_total);
        cp_tv_card = findViewById(R.id.cp_tv_card);
        cp_tv_date_time = findViewById(R.id.cp_tv_date_time);
        cp_tv_approve = findViewById(R.id.cp_tv_approve);
        cp_iv_trans_type = findViewById(R.id.cp_iv_trans_type);
        cp_iv_process = findViewById(R.id.cp_iv_process);
        cp_ll_content_card = findViewById(R.id.cp_ll_content_card);
        //ll_msi = findViewById(R.id.ll_msi);
        tp_tv_amount=findViewById(R.id.tp_tv_amount);
        cp_tv_tip_label =findViewById(R.id.cp_tv_tip_label);
        cp_tv_total_label = findViewById(R.id.cp_tv_total_label);
        cp_tv_tipotarjeta = findViewById(R.id.cp_tv_tipotarjeta);
        cp_tv_aid = findViewById(R.id.txt_AID);
        cp_tv_arqc = findViewById(R.id.txt_ARQC);

        cp_tv_amount.setText(subtotal);
        cp_tv_tip.setText(propina);
         if (status.equals("VN")){
            cp_iv_trans_type.setImageResource(R.drawable.efevoo_i_check_exito);
            cp_tv_trans_type.setText(GNTBackEnd.getTitle(GNTBackEnd.TRANS_CAN_TYPE));
             transaction_type = 1;
        }else{
             cp_tv_trans_type.setText(GNTBackEnd.getTitle(GNTBackEnd.TRANS_CANMSI_TYPE));
//             Float _amount = Float.parseFloat(total.replace("$","").replace(",","").replace(" ",""));
//             Float total_msi= _amount / Integer.parseInt(msi);
//             NumberFormat format = NumberFormat.getCurrencyInstance();
//             format.setMaximumFractionDigits(2);
             cp_tv_total_label.setText(meses+"MSI");
             cp_tv_amount.setText(GNTBackEnd.Amount_msi(total,meses));
             //cp_tv_tip.setText(propina);
             //ll_msi.setVisibility(View.GONE);
             transaction_type = 0;
        }

        if (redtarj.equals("MC")){
            card_provider = "MASTERCARD";
            cp_iv_process.setImageResource(R.drawable.masterdcard);
        }else if(redtarj.equals("Visa")){
            cp_iv_process.setImageResource(R.drawable.visa);
        }

        cp_tv_tipotarjeta.setText("Tarjeta "+tipotarjeta);
        cp_tv_auth.setText(auth);
        cp_tv_total.setText(total);
        cp_tv_card.setText("**** "+card);
        cp_tv_date_time.setText(date+" "+time);
        cp_tv_approve.setText(approve);
        cp_tv_aid.setText(aid);
        cp_tv_arqc.setText(arqc);
    }

    private void buttonListener(){

        cp_btn_trans_cancelar = findViewById(R.id.cp_btn_trans_cancelar);
        cp_btn_trans_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialAlertDialogBuilder(mContext, R.style.ThemeOverlay_App_MaterialAlertDialog)
                        .setTitle("¿Quieres cancelar la Transacción?")
                        .setIcon(R.drawable.efevoo_i_grupo_41699)
                        .setPositiveButton("Confirmar",(dialog, lis) -> {
                            //sendCancelFinal();
                            changeView();
                        })
                        .setNeutralButton("Regresar",(dialog, lis) -> {
                            dialog.dismiss();
                        })
                .show();
            }
        });
        cp_btn_trans_final = findViewById(R.id.cp_btn_trans_final);
        cp_btn_trans_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrintTicket();
                onBackPressed();
            }
        });

    }

    private void sendCancelFinal(){
        cp_iv_trans_type.setImageResource(R.drawable.efevoo_i_grupo_41699);

        cp_tv_trans_type.setTextColor(0xFFCC1818);
        cp_tv_trans_type.setText("Cancelada Venta Normal");
        cp_tv_total_label.setTextColor(0xFF5A5A5A);
        cp_tv_total.setTextColor(0xFF5A5A5A);
        cp_tv_date_time.setTextColor(0xFF121212);

        Drawable layoutDrawable = cp_ll_content_card.getBackground();
        layoutDrawable = DrawableCompat.wrap(layoutDrawable);
        //the color is a direct color int and not a color resource
        DrawableCompat.setTint(layoutDrawable, 0xFFFDC0C0);
        cp_ll_content_card.setBackground(layoutDrawable);

        cp_btn_trans_cancelar.setVisibility(View.GONE);
        cp_btn_trans_final.setVisibility(View.VISIBLE);
    }
    private void changeView(){
        intent = new Intent(this, WMX_Card.class);
        intent.putExtra("AmountToShow",formatMoney(cp_tv_total.getText().toString().replace("$","").replace(",","").replace(" ","")));
        intent.putExtra("type_transaction","Cancelacion" );
        intent.putExtra("cp_tv_auth",cp_tv_auth.getText());
        intent.putExtra("ksn_posId",ksn_posId);
        String tmp = cp_tv_total.getText().toString().replace("$","").replace(",","").replace(" ","").replace(" MXN","");
        intent.putExtra("Amount",tmp);

        intent.putExtra("total",formatMoney(cp_tv_total.getText().toString().replace("$","").replace(",","").replace(" ","")));

        intent.putExtra("subtotal",formatMoney(cp_tv_amount.getText().toString().replace("$","").replace(",","").replace(" ","")));
        intent.putExtra("months",meses);
        intent.putExtra("approve",cp_tv_approve.getText().toString());
        intent.putExtra("tips",formatMoney(cp_tv_tip.getText().toString().replace("$","").replace(",","").replace(" ","")));
        intent.putExtra("propina",cp_tv_tip.getText().toString().replace("$","").replace(",","").replace(" ",""));

        startActivity(intent);
    }
    public String formatMoney(String amount){
        String str="";
        str = "$" + amount +" MXN";
        return str;
    }
}
