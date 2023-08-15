package com.efevoopay.demoui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import com.efevoopay.demoui.R;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Utils;

public class WMX_Transaction_Cancel extends BaseActivity {
    private String  Amount, AmountToShow, type_transaction, ksn_posId, _Propina,_noAuth, total, months_total, subtotal, tips, msi, approve;
    private Button btn_retry, btn_cancel;
    private Intent intent;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setProps();
        btn_retry = findViewById(R.id.btn_retry);
        btn_cancel = findViewById(R.id.btn_cancel);

        btn_retry.setOnClickListener(this::onRetry);
        btn_cancel.setOnClickListener(this::onCancel);
    }

    @Override
    public void onStart() {
        super.onStart();
        String ErrorMessage = intent.getStringExtra("error");
        if(!TextUtils.isEmpty(ErrorMessage)) {
            WMX_Transaction_Cancel.super.showAlert("ERROR", ErrorMessage);
        }
    }

    private void onRetry(View view) {
        Intent intent = new Intent(WMX_Transaction_Cancel.this, WMX_Card.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Amount", Amount);
        intent.putExtra("AmountToShow", AmountToShow);
        intent.putExtra("type_transaction", type_transaction);
        intent.putExtra("ksn_posId", ksn_posId);
        intent.putExtra("propina", _Propina);
        intent.putExtra("months", msi);
        intent.putExtra("cp_tv_auth", _noAuth);
        intent.putExtra("total", total);
        intent.putExtra("months_total",  months_total);
        intent.putExtra("subtotal", subtotal);
        intent.putExtra("tips", tips);
        intent.putExtra("approve", approve);
        startActivity(intent);
    }

    private void onCancel(View view) {
        startActivity(new Intent(this, WMX_Menu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    private void setProps() {
        intent = getIntent();
        Amount = intent.getStringExtra("Amount");
        AmountToShow = intent.getStringExtra("AmountToShow");
        type_transaction = intent.getStringExtra("type_transaction");
        ksn_posId = intent.getStringExtra("ksn_posId");
        _Propina=intent.getStringExtra("propina");
        msi= intent.getStringExtra("months");
        _noAuth=intent.getStringExtra("cp_tv_auth");
        total=intent.getStringExtra("total");
        months_total = intent.getStringExtra("months_total");
        subtotal = intent.getStringExtra("subtotal");
        tips =  intent.getStringExtra("tips");
        approve =  intent.getStringExtra("approve");
    }

    @Override
    public void onToolbarLinstener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmn_cancelation_trade;
    }
}
