package com.efevoopay.demoui.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.efevoopay.demoui.R;
import com.efevoopay.demoui.utils.KSN;
import com.efevoopay.demoui.utils.ResponseCode;
import com.efevoopay.demoui.utils.SQLiteTpv;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Utils;

public class WMX_Menu extends BaseActivity implements View.OnClickListener {
    //private Button  other, ajustes, meses;
    private Intent intent;
    private LinearLayout transfer,other, ajustes, meses, cancelaciones;
    public static WMX_KSN ksn;
    private SQLiteTpv sqLiteTpv;
    Cursor cursor;
    private SQLiteDatabase db;
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

        ResponseCode.setCodeResponses();

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

        sqLiteTpv = new SQLiteTpv(this);
        db = sqLiteTpv.getWritableDatabase();
        sqLiteTpv.onCreate(db);
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
        //sqLiteTpv.TpvDelete(ksn.posId);
        //sqLiteTpv.TpvInsert(ksn.posId,"00000040471811000001","445AB557576C642548F7B52916D8B4F4","B2A5B99DE4314F3257F70DECE62B2C96");
        //sqLiteTpv.TpvInsert("12100509021042600834","00000095874315400001","57F223B1B0852C1C2384D04283D576D2","3DE27BCB004EB361A6390832086B0CB9");
        //sqLiteTpv.TpvInsert("13100106222080200038","00000208656788600001","4BB7A675B598FCA413D84C7FCD8B4EE8","4D74392EFA5B7C8CCCD29539CBEA7954");
        cursor=sqLiteTpv.TpvConsult(ksn.posId);
        switch (view.getId()){
            case R.id.btn_transfer:
                if (cursor.getCount()>0){
                intent = new Intent(this, WMX_Terminal.class);
                intent.putExtra("type_transaction", "venta");
                intent.putExtra("ksn_posId", ksn.posId);
                startActivity(intent);
                }else{
                    WMX_Menu.super.showAlert("ERROR","TPV NO INICIALIZADA: "+ksn.posId);
                    //TRACE.d("cursor: "+ TRACE.NEW_LINE+ cursor.getCount());

                }
                break;
            case R.id.btn_Other:
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
                if (cursor.getCount()>0){
                    intent = new Intent(this, WMX_Terminal.class);
                    intent.putExtra("type_transaction", "msi");
                    intent.putExtra("ksn_posId", ksn.posId);
                    startActivity(intent);
                }else{
                    WMX_Menu.super.showAlert("ERROR","TPV NO INICIALIZADA: "+ksn.posId);
                }
                break;
            case R.id.btn_cancelaciones:
                intent = new Intent(this, WMX_Historial_Cancelaciones.class);
                intent.putExtra("ksn_posId", ksn.posId);
                startActivity(intent);
                break;
        }
    }


}
