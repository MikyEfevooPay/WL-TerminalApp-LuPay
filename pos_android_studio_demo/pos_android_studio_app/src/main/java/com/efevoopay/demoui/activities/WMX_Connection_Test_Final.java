package com.efevoopay.demoui.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.efevoopay.demoui.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WMX_Connection_Test_Final extends BaseActivity implements View.OnClickListener {
    private Intent intent;
    private CONNECTION_TEST_TYPE type;
    private Button btn_continue;
    private LinearLayout lyt_connection_error, lyt_connection_test_final_header;
    private TextView txt_connection_success_title, txt_connection_success_date,txt_connection_test_kpos_id_value;

    public enum CONNECTION_TEST_TYPE {
        SUCCESS(0), FAILED(1);

        private int id;
        CONNECTION_TEST_TYPE(int id) {
            this.id = id;
        }

        public static CONNECTION_TEST_TYPE getByNumber(int _id) {
            for(CONNECTION_TEST_TYPE type : values()) {
                if(type.id == _id) {
                    return type;
                }
            }
            return CONNECTION_TEST_TYPE.FAILED;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.switch_title_logo(getResources().getString(R.string.wmx_connection_test_final_header), Color.WHITE);
        super.setCustomToolbarColor("#002344ED");
        super.setToolbarIconColor(Color.WHITE);
        lyt_connection_error = findViewById(R.id.lyt_connection_error);
        lyt_connection_test_final_header = findViewById(R.id.lyt_connection_test_final_header);
        txt_connection_success_title = findViewById(R.id.txt_connection_success_title);
        txt_connection_success_date = findViewById(R.id.txt_connection_success_date);
        txt_connection_test_kpos_id_value=findViewById(R.id.txt_connection_test_kpos_id_value);
        btn_continue = findViewById(R.id.btn_connection_test_continue);
        btn_continue.setOnClickListener(this);
        intent = getIntent();
        type = CONNECTION_TEST_TYPE.getByNumber(intent.getIntExtra("type", 1));
        DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy hh:mm aa");
        txt_connection_success_date.setText(dateFormat.format(new Date()));
        txt_connection_test_kpos_id_value.setText(intent.getStringExtra("ksn_posId"));
        initLayout();
    }

    @Override
    public void onFetchResult() {
    }


    private void initLayout() {
        Resources rsc = getResources();
        switch (type) {
            case SUCCESS:
                txt_connection_success_title.setText(rsc.getText(R.string.wmx_connection_test_final_title));
                btn_continue.setText(rsc.getText(R.string.common_continue));
                lyt_connection_error.setVisibility(View.GONE);
                break;
            case FAILED:
                lyt_connection_test_final_header.setBackgroundColor(rsc.getColor(R.color.ep_crt_error));
                txt_connection_success_title.setText(rsc.getText(R.string.wmx_connection_test_error_title));
                btn_continue.setText(rsc.getText(R.string.common_close));
                lyt_connection_error.setVisibility(View.VISIBLE);
                super.setCustomToolbarColor(rsc.getColor(R.color.ep_crt_error));
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_connection_test_continue:
                if(type == CONNECTION_TEST_TYPE.SUCCESS) ModalSussessful();
                else startActivity(new Intent(this, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
    }

    @Override
    public void onToolbarLinstener() {
        super.onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_connection_test_final;
    }


    private void ModalSussessful() {
        LayoutInflater inflater=getLayoutInflater();
        View dialogContentView =inflater.inflate(R.layout.wmx_connection_test_success_modal, null);
        MaterialAlertDialogBuilder modal = new MaterialAlertDialogBuilder(this,  R.style.ThemeOverlay_App_MaterialAlertDialog);
        modal.setView(dialogContentView);

        AppCompatButton btn_connection_success = dialogContentView.findViewById(R.id.btn_connection_test_button);

        AlertDialog modalCreate = modal.create();

        modalCreate.show();

        btn_connection_success.setOnClickListener((view) -> {
            modalCreate.dismiss();
            startActivity(new Intent(this, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });
    }
}
