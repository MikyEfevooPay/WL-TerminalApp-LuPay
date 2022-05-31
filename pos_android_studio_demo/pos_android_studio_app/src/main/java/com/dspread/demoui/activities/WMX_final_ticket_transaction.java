package com.dspread.demoui.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.graphics.drawable.DrawableCompat;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dspread.demoui.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class WMX_final_ticket_transaction extends BaseActivity implements View.OnClickListener {

    private Button btn_ticket_final;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().hide();
        setTitle(getString(R.string.wmx_title_welcome));

        mContext=this;


        btn_ticket_final = findViewById(R.id.btn_ticket_final);
        btn_ticket_final.setOnClickListener(this);

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
                openModalSendEmail();
                break;
            default:

        }
    }

    private void openModalSendEmail(){
        new MaterialAlertDialogBuilder(mContext, R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle("¿Recibe tu ticket por mail?")
                .setIcon(R.drawable.cancelar_transaccion_icon)
//                .setPositiveButton("Confirmar",(dialog, lis) -> {
//                    //sendCancelFinal();
//                })
                .setNeutralButton("Enviar",(dialog, lis) -> {
                    dialog.dismiss();
                })
                .show();
    }
}
