package com.lupay.demoui.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import com.lupay.demoui.R;
import com.lupay.demoui.utils.ConfigAmex;
import com.lupay.demoui.utils.ConfigTpv;
import com.lupay.demoui.utils.GNTBackEnd;
import com.lupay.demoui.utils.ResponseCode;
import com.lupay.demoui.utils.TRACE;
import com.lupay.demoui.utils.Utils;

public class WMX_Menu extends BaseActivity implements View.OnClickListener {
    // private Button other, ajustes, meses;
    private Intent intent;
    private LinearLayout transfer, other, ajustes, meses, cancelaciones, cortecaja, connection_test;
    public static Cursor cursor;
    public static ConfigTpv configTpv;
    public static ConfigAmex configAmex;
    public static ProgressDialog spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        super.setInvisiblemargin(true);
        getSupportActionBar().hide();
        setTitle(getString(R.string.wmx_title_welcome));
        WMX_KSN.init(this);
        spinner = Utils.getLoaderSpinner(this);

        transfer = findViewById(R.id.btn_transfer);
        other = findViewById(R.id.btn_Other);
        ajustes = findViewById(R.id.btn_Ajustes);
        meses = findViewById(R.id.btn_meses);
        cancelaciones = findViewById(R.id.btn_cancelaciones);
        cortecaja = findViewById(R.id.btn_cortecaja);
        connection_test = findViewById(R.id.btn_connection_test);
        transfer.setOnClickListener(this);
        other.setOnClickListener(this);
        ajustes.setOnClickListener(this);
        meses.setOnClickListener(this);
        cancelaciones.setOnClickListener(this);
        cortecaja.setOnClickListener(this);
        connection_test.setOnClickListener(this);
        getinfoScreen();
        //Config
        Utils.setErrorMessages();
        ResponseCode.setCodeResponses();
        GNTBackEnd.initTransTypeTitles(getResources());
        configTpv = new ConfigTpv(this);
        configTpv.dbManager.onCreate();
        configAmex = new ConfigAmex(this);
        configAmex.dbManager.onCreate();
    }

    @Override
    public void onStart() {
        super.onStart();
        optksn();
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
            WMX_Menu.super.showAlert("informative", "TPV NO INICIALIZADA: " + WMX_KSN.getPosId());

        return init;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_menu;
    }

    @Override
    public void onClick(View view) {
        String posId = WMX_KSN.getPosId();
        if(posId == null) {
            showAlert("Error", "Ha ocurrido un error al cargar la información de la TPV");
            return;
        }
        /*TRACE.d("giro: " +cursor.getString(24));
        TRACE.d("statusseller: " + cursor.getString(27));
        TRACE.d("datafield43: " + cursor.getString(28));
        TRACE.d("datafield60: " + cursor.getString(29));
        TRACE.d("tkamex: " + cursor.getString(30));
        TRACE.d("keyamex: " + cursor.getString(31));
        TRACE.d("countamex: " + cursor.getString(32));*/
        cursor = configTpv.dbManager.fetch(posId);
        switch (view.getId()) {
            case R.id.btn_transfer:
                if (!TPVInitializated())
                    break;
                intent = new Intent(this, WMX_Terminal.class);
                intent.putExtra("type_transaction", "venta");
                intent.putExtra("ksn_posId", posId);
                startActivityMiddleware(intent);
                break;
            case R.id.btn_Other:
                if (!TPVInitializated())
                    break;
                intent = new Intent(this, WMX_Transaccion.class);
                intent.putExtra("ksn_posId", posId);
                startActivityMiddleware(intent);
                break;
            case R.id.btn_Ajustes:
                intent = new Intent(this, WMX_Ajustes.class);
                intent.putExtra("ksn_posId", posId);
                if (TPVInitializated())intent.putExtra("interfaz", cursor.getString(22));
                startActivityMiddleware(intent);
                break;
            case R.id.btn_meses:
                if (!TPVInitializated())
                    break;
                if (cursor.getString(10).equals("1")) {
                    intent = new Intent(this, WMX_Terminal.class);
                    intent.putExtra("type_transaction", "MSI");
                    intent.putExtra("ksn_posId", posId);
                    startActivityMiddleware(intent);
                } else {
                    WMX_Menu.super.showAlert("informative", "OPCIÓN NO HABILITADA");
                }
                break;
            case R.id.btn_cancelaciones:
                if (!TPVInitializated())
                    break;
                intent = new Intent(this, WMX_Historial_Cancelaciones.class);
                intent.putExtra("ksn_posId", posId);
                startActivityMiddleware(intent);
                break;
            case R.id.btn_cortecaja:
                if (!TPVInitializated())
                    break;
                intent = new Intent(this, WMX_Historial_CorteCaja.class);
                intent.putExtra("ksn_posId", posId);
                startActivityMiddleware(intent);
                break;
            case R.id.btn_connection_test:
                intent = new Intent(this, WMX_Connection_Test.class);
                intent.putExtra("ksn_posId", posId);
                intent.putExtra("type", 1);
                startActivityMiddleware(intent);
                break;
        }

    }

    @SuppressLint("NewApi")
    public void optksn() {
        if(!resolveNetworkFlag(getFlags(this.getClass().getName()))) return;
        spinner.show();
        configTpv.spinner = spinner;
        WMX_KSN.getPosIdResult().thenAccept((posId) -> {
            TRACE.d("FUturablePosId: " + posId);
            DbSurce(posId);
            cursor = configTpv.dbManager.fetch(posId);
            TRACE.d("ksn: " + posId);
        });
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

    public void DbSurce(String posId) {
        final Handler handler = new Handler();
        int[] count = { 0 };

        final Runnable runnable = new Runnable() {
            public void run() {
                configTpv.count = count[0];
                if (count[0]++ < 5) {
                    if (!configTpv.bnd[0]) {
                        TRACE.d("configTpv entra");
                        if (!configTpv.nuevainit) {
                            configTpv.tpvConfig(posId, 1);
                        } else {
                            configTpv.tpvConfig(posId, 0);
                        }
                        handler.postDelayed(this, 4000);
                    } else {
                        if(configTpv.bnd[0] && !configAmex.bndamex[0]){
                            TRACE.d("configAmex entra"+configTpv.bnd[0]);
                            handler.removeCallbacks(this);
                            //configAmex = new ConfigAmex(configTpv.context);
                            //configAmex.dbManager.onCreate();
                            Dbamex(configTpv._jsonca,posId);
                        }else{
                            if (spinner.isShowing())
                                spinner.dismiss();
                            handler.removeCallbacks(this);
                        }

                    }
                } else {
                    WMX_Menu.super.showAlert("informative", "TPV NO INICIALIZADA: INTENTE NUEVAMENTE ");
                    if (spinner.isShowing())
                        spinner.dismiss();
                    handler.removeCallbacks(this);
                    configTpv.dbManager.onDelete();
                    configTpv.dbManager.onCreate();
                }

            }
        };
        handler.post(runnable);
    }
    public void Dbamex(String json,String posId) {
        final Handler handler = new Handler();
        int[] count = { 0 };

        final Runnable runnable = new Runnable() {
            public void run() {
                configAmex.countamex = count[0];
                if (count[0]++ < 5) {
                    if (!configAmex.bndamex[0]) {
                        if (!configAmex.nuevainit) {
                            configAmex.tpvConfigAmex(posId, 1);
                        } else {
                            configAmex.tpvConfigAmex(posId, 0);
                        }
                        handler.postDelayed(this, 3000);
                    } else {
                        if (spinner.isShowing())
                            spinner.dismiss();
                        handler.removeCallbacks(this);
                    }
                } else {
                    WMX_Menu.super.showAlert("informative", "TPV NO INICIALIZADA: INTENTE NUEVAMENTE ");
                    if (spinner.isShowing())
                        spinner.dismiss();
                    handler.removeCallbacks(this);
                    configAmex.dbManager.onDelete();
                    configAmex.dbManager.onCreate();
                }

            }
        };
        handler.post(runnable);
    }
}
