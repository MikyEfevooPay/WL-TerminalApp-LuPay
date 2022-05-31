package com.dspread.demoui.activities;

import android.os.Bundle;
import android.widget.Button;

import com.dspread.demoui.R;

public class WMX_Ajustes extends BaseActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.setCustomToolbarColor("#EFF2FF");
        setTitle(getString(R.string.wmx_title_welcome));
    }
    @Override
    public void onToolbarLinstener() {
        onBackPressed();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.wmx_ajustes;
    }
}
