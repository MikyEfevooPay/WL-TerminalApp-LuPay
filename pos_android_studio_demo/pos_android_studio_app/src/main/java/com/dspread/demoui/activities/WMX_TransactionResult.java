package com.dspread.demoui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dspread.demoui.R;

public class WMX_TransactionResult extends BaseActivity implements View.OnClickListener{
    private String result = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.wmx_title_welcome));
        Intent _intent = getIntent();
        result = _intent.getStringExtra("result");


    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onToolbarLinstener() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_transaction_result;
    }

}
