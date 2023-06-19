package com.efevoopay.demoui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.efevoopay.demoui.R;
import com.efevoopay.demoui.utils.ConfigTpv;
import com.efevoopay.demoui.utils.DBManager;
import com.efevoopay.demoui.utils.ResponseCode;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Utils;

public class WMX_Menu extends BaseActivity implements View.OnClickListener {
    // private Button other, ajustes, meses;
    private Intent intent;
    private LinearLayout transfer, other, ajustes, meses, cancelaciones, cortecaja;
    public WMX_KSN ksn;
    public static Cursor cursor;
    private ConfigTpv configTpv;
    ProgressDialog spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        super.setInvisiblemargin(true);
        getSupportActionBar().hide();
        setTitle(getString(R.string.wmx_title_welcome));

        ksn = new WMX_KSN();
        ksn.onCreate();

        spinner = Utils.getLoaderSpinner(this);

        ResponseCode.setCodeResponses();

        transfer = findViewById(R.id.btn_transfer);
        other = findViewById(R.id.btn_Other);
        ajustes = findViewById(R.id.btn_Ajustes);
        meses = findViewById(R.id.btn_meses);
        cancelaciones = findViewById(R.id.btn_cancelaciones);
        cortecaja = findViewById(R.id.btn_cortecaja);
        transfer.setOnClickListener(this);
        other.setOnClickListener(this);
        ajustes.setOnClickListener(this);
        meses.setOnClickListener(this);
        cancelaciones.setOnClickListener(this);
        cortecaja.setOnClickListener(this);
        getinfoScreen();
        configTpv = new ConfigTpv(this);
        configTpv.dbManager.onCreate();

        spinner.show();
        configTpv.spinner = spinner;
        optksn();
        LLave();

    }

    public void getinfoScreen() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        TRACE.d("display size pixels " + TRACE.NEW_LINE + "height:" + height + TRACE.NEW_LINE + "width: " + width);

        int height2;
        Resources myResources = getResources();
        int idStatusBarHeight = myResources.getIdentifier("status_bar_height", "dimen", "android");

        if (idStatusBarHeight > 0) {
            height = getResources().getDimensionPixelSize(idStatusBarHeight);
            // Toast.makeText(this, "Status Bar Height = " + height,
            // Toast.LENGTH_LONG).show();
        } else {
            height = 0;
            // Toast.makeText(this, "Resources NOT found", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onToolbarLinstener() {

    }

    private boolean TPVInitializated() {
        boolean init = cursor.getCount() > 0;

        if (!init)
            WMX_Menu.super.showAlert("informative", "TPV NO INICIALIZADA: " + ksn.posId);

        return init;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_menu;
    }

    @Override
    public void onClick(View view) {
        cursor = configTpv.dbManager.fetch(ksn.posId);
        switch (view.getId()){
            case R.id.btn_transfer:
                if (!TPVInitializated()) break;
                intent = new Intent(this, WMX_Terminal.class);
                intent.putExtra("type_transaction", "venta");
                intent.putExtra("ksn_posId", ksn.posId);
                startActivity(intent);
                break;
            case R.id.btn_Other:
                if (!TPVInitializated()) break;
                intent = new Intent(this, WMX_Transaccion.class);
                intent.putExtra("ksn_posId", ksn.posId);
                startActivity(intent);
                break;
            case R.id.btn_Ajustes:
                intent = new Intent(this, WMX_Ajustes.class);
                intent.putExtra("ksn_posId", ksn.posId);
                startActivity(intent);
                break;
            case R.id.btn_meses:
                if (!TPVInitializated()) break;
                if(cursor.getString(10).equals("1")){
                        intent = new Intent(this, WMX_Terminal.class);
                        intent.putExtra("type_transaction", "msi");
                        intent.putExtra("ksn_posId", ksn.posId);
                        startActivity(intent);
                    }else{
                        WMX_Menu.super.showAlert("informative","OPCIÓN NO HABILITADA");
                    }
                break;
            case R.id.btn_cancelaciones:
                if (!TPVInitializated())  break;
                intent = new Intent(this, WMX_Historial_Cancelaciones.class);
                intent.putExtra("ksn_posId", ksn.posId);
                startActivity(intent);
                break;
            case R.id.btn_cortecaja:
                if (!TPVInitializated())  break;
                intent = new Intent(this, WMX_Historial_CorteCaja.class);
                intent.putExtra("ksn_posId", ksn.posId);
                startActivity(intent);
                break;
        }

    }

    public void optksn() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                // need to do tasks on the UI thread
                handler.postDelayed(this, 1000);
                cursor = configTpv.dbManager.fetch(ksn.posId);
                TRACE.d("ksn: " + ksn.posId);
                handler.removeCallbacks(this);
            }
        };
        // trigger first time
        handler.post(runnable);
    }

    public void LLave() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                DbSurce();
                handler.removeCallbacks(this);
            }
        }, 1000);

    }

    public void cerrarapk() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                finishAffinity();
                System.exit(0);
                handler.removeCallbacks(this);
            }
        }, 2000);

    }

    public void DbSurce() {
        final boolean[] isClickable = { Boolean.FALSE };
        final Handler handler = new Handler();
        int[] count = { 0 };

        final Runnable runnable = new Runnable() {
            public void run() {
                // need to do tasks on the UI thread
                configTpv.count = count[0];
                if (count[0]++ < 7) {
                    if (!configTpv.bnd[0]) {
                        if (!configTpv.nuevainit) {
                            configTpv.tpvConfig(ksn.posId, 1);
                        } else {
                            configTpv.tpvConfig(ksn.posId, 0);
                        }
                        handler.postDelayed(this, 5000);
                    } else {
                        if (spinner.isShowing())
                            spinner.dismiss();
                        handler.removeCallbacks(this);
                    }
                } else {
                    WMX_Menu.super.showAlert("informative", "TPV NO INICIALIZADA: " + ksn.posId);
                    if (spinner.isShowing())
                        spinner.dismiss();
                    handler.removeCallbacks(this);
                    configTpv.dbManager.onDelete();
                    configTpv.dbManager.onCreate();
                    cerrarapk();
                }

            }
        };
        // trigger first time
        handler.post(runnable);
    }
}
