package com.dspread.demoui.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.graphics.drawable.DrawableCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dspread.demoui.R;

public class WMX_Transaction_Desc extends BaseActivity{

    TextView tp_tv_trans_type,tp_tv_auth,tp_tv_amount,tp_tv_tip,tp_tv_total,tp_tv_card,tp_tv_date_time,tp_tv_approve,tp_tv_tip_label,tp_tv_total_label;
    ImageView tp_iv_trans_type,tp_iv_process;
    LinearLayout tp_ll_content_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.switch_title_logo("Detalle Transacción");
        Intent intent = getIntent();

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

    private void initData(Intent intent){
        String auth,date,time,amount,card,process,status, approve;
        auth = intent.getStringExtra("auth");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        amount = intent.getStringExtra("amount");
        card = intent.getStringExtra("card");
        process = intent.getStringExtra("process");
        status = intent.getStringExtra("status");
        approve = intent.getStringExtra("approve");

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

        if(status.equals("A")){
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

        }else if (status.equals("G")){
            tp_iv_trans_type.setImageResource(R.drawable.efevoo_i_check_exito);
            tp_tv_trans_type.setText("Aprobada Venta Normal");
        }else{
            tp_tv_trans_type.setText("Aprobada Venta a Meses");
            tp_tv_tip_label.setText("Meses:");
            tp_tv_tip.setText("6 MSI");
        }

        if (process.equals("MC")){
            tp_iv_process.setImageResource(R.drawable.masterdcard);
        }else if(process.equals("Visa")){
            tp_iv_process.setImageResource(R.drawable.visa);
        }


        tp_tv_auth.setText(auth);
        tp_tv_amount.setText(amount);
        tp_tv_total.setText(amount);
        tp_tv_card.setText("**** "+card);
        tp_tv_date_time.setText(date+" "+time);
        tp_tv_approve.setText(approve);
    }
}
