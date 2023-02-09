package com.efevoopay.demoui.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.efevoopay.demoui.R;
import com.efevoopay.demoui.utils.KSN;
import com.efevoopay.demoui.utils.TRACE;

public class WMX_Menu extends BaseActivity implements View.OnClickListener {
    //private Button  other, ajustes, meses;
    private Intent intent;
    private LinearLayout transfer,other, ajustes, meses, cancelaciones;
    public static KSN ksn;
    @Override



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        super.setInvisiblemargin(true);
        getSupportActionBar().hide();
        setTitle(getString(R.string.wmx_title_welcome));

        transfer=findViewById(R.id.btn_transfer);
        other= findViewById(R.id.btn_Other);
        ajustes= findViewById(R.id.btn_Ajustes);
        meses= findViewById(R.id.btn_meses);
        cancelaciones= findViewById(R.id.btn_cancelaciones);
        transfer.setOnClickListener(this);
        other.setOnClickListener(this);
        ajustes.setOnClickListener(this);
        meses.setOnClickListener(this);
        cancelaciones.setOnClickListener(this);
        getinfoScreen();

        ksn = new KSN();

    }

    public void getinfoScreen() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        TRACE.d("display size pixels "+ TRACE.NEW_LINE+ "height:" +height+ TRACE.NEW_LINE+"width: "+width);

        int height2;
        Resources myResources = getResources();
        int idStatusBarHeight = myResources.getIdentifier( "status_bar_height", "dimen", "android");

        if (idStatusBarHeight > 0) {
            height = getResources().getDimensionPixelSize(idStatusBarHeight);
            //Toast.makeText(this, "Status Bar Height = " + height, Toast.LENGTH_LONG).show();
        } else {
            height = 0;
            //Toast.makeText(this, "Resources NOT found", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onToolbarLinstener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_menu;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_transfer:
                intent = new Intent(this, WMX_Terminal.class);
                intent.putExtra("type_transaction", "venta");
                startActivity(intent);
                break;
            case R.id.btn_Other:
                intent = new Intent(this, WMX_Transaccion.class);
                startActivity(intent);
                break;
            case R.id.btn_Ajustes:
                intent = new Intent(this, WMX_Ajustes.class);
                startActivity(intent);
                break;
            case R.id.btn_meses:
                //super.showAlert("Transacción rechazada","Fondos insuficientes");
                intent = new Intent(this, WMX_Terminal.class);
                intent.putExtra("type_transaction", "msi");
                startActivity(intent);
                break;
            case R.id.btn_cancelaciones:
                intent = new Intent(this, WMX_Historial_Cancelaciones.class);
                startActivity(intent);
                break;
        }
    }


}
