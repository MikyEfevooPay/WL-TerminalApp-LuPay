package com.efevoopay.demoui.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.efevoopay.demoui.R;
import com.efevoopay.demoui.utils.DBManager;
import com.efevoopay.demoui.utils.GlobalFunctions;
import com.efevoopay.demoui.utils.InputFilterMinMax;
import com.efevoopay.demoui.utils.TRACE;
import com.google.android.material.textfield.TextInputEditText;

public class WMX_Propinas extends BaseActivity implements View.OnClickListener{
    private Context mContext;
    private String Amount, type_transaction, v_msi="3", v_total_msi,ksn_posId;
    private TextView Total_Amount, tv_zero, tv_ten, tv_fifteen, tv_twenty, tv_twentyfive, tv_total, tv_propina_final, tv_propina_percent, tv_caption,Propinas_subtotal;
    private RadioButton zero, ten, fifteen, twenty, twentyfive, other;
    private GlobalFunctions gf ;
    private TextInputEditText et;
    private Button continuar;
    private Intent intent;
    private LinearLayout ll_otherPercent, ll_tips, ll_total,ll_ZeroPercent,ll_teenPercent,ll_fifteenPercent,ll_twentyPercent,ll_twentyfivePercent;
    private DBManager dbManager;
    Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(getString(R.string.wmx_title_welcome));
        Intent intent = getIntent();
        Amount = intent.getStringExtra("Amount");
        type_transaction = intent.getStringExtra("type_transaction");
        ksn_posId=intent.getStringExtra("ksn_posId");
        mContext = this;
        gf= new GlobalFunctions(mContext);
        Total_Amount = (TextView) findViewById(R.id.Propinas_total_amount);
        tv_zero = (TextView) findViewById(R.id.zeroPercent);
        tv_ten = (TextView) findViewById(R.id.tenPercent);
        tv_fifteen = (TextView) findViewById(R.id.fifteenPercent);
        tv_twenty = (TextView) findViewById(R.id.twentyPercent);
        tv_twentyfive = (TextView) findViewById(R.id.twentyfivePercent);
        tv_total= (TextView) findViewById(R.id.Propinas_total_result);
        tv_propina_final= (TextView) findViewById(R.id.Propinas_final_result);
        tv_propina_percent=(TextView) findViewById(R.id.propinas_percent_label);
        tv_caption = findViewById(R.id.tv_caption);

        zero = (RadioButton) findViewById(R.id.rBZero);
        ten = (RadioButton) findViewById(R.id.rBTen);
        fifteen = (RadioButton) findViewById(R.id.rBFifteen);
        twenty = (RadioButton) findViewById(R.id.rBTwenty);
        twentyfive = (RadioButton) findViewById(R.id.rBTwentyFive);
        other = (RadioButton) findViewById(R.id.rBOther);


        continuar = (Button) findViewById(R.id.Propinas_btn_continue);
        continuar.setOnClickListener(this);

        zero.setOnClickListener(this);
        ten.setOnClickListener(this);
        fifteen.setOnClickListener(this);
        twenty.setOnClickListener(this);
        twentyfive.setOnClickListener(this);
        other.setOnClickListener(this);

        dbManager = new DBManager(mContext);
        dbManager.open();
        cursor = dbManager.fetch(ksn_posId);

        Total_Amount.setText("$"+Amount+" MXN");
        tv_total.setText("$"+Amount+" MXN");
        et = (TextInputEditText) findViewById(R.id.otherPercentet);
        et.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "1000"), new InputFilter.LengthFilter(4)});
        changeTextListener();
        setTipsTexts();
        initViewType();
    }

    public void initViewType(){
        ll_otherPercent = findViewById(R.id.ll_otherPercent);
        ll_tips = findViewById(R.id.ll_tips);
        ll_ZeroPercent = findViewById(R.id.ll_ZeroPercent);
        ll_teenPercent = findViewById(R.id.ll_teenPercent);
        ll_fifteenPercent = findViewById(R.id.ll_fifteenPercent);
        ll_twentyPercent = findViewById(R.id.ll_twentyPercent);
        ll_twentyfivePercent = findViewById(R.id.ll_twentyfivePercent);
        ll_total = findViewById(R.id.ll_total);
        Propinas_subtotal=findViewById(R.id.Propinas_subtotal);
        if(type_transaction.equals("MSI")){
            // Caption
            Propinas_subtotal.setText("Total");
            tv_caption.setText("Selecciona tus mensualidades");
            // Options
            zero.setText("3 MSI");
            ten.setText("6 MSI");
            fifteen.setText("9 MSI");
            twenty.setText("12 MSI");
            twentyfive.setText("18 MSI");
            Habilitamsi();
            ll_otherPercent.setVisibility(View.GONE);
            // Tips
            ll_tips.setVisibility(View.GONE);
            ll_total.setGravity(Gravity.BOTTOM);
            ll_total.setVisibility(View.GONE);

            // MSI totals
            Float _amount = Float.parseFloat(Amount.replace(",",""));
            Float total_msi= _amount / 3;
            tv_zero.setText(gf.formatMoney(String.valueOf(total_msi),true) + " MXN");
            total_msi= _amount / 6;
            tv_ten.setText(gf.formatMoney(String.valueOf(total_msi),true) + " MXN");
            total_msi= _amount / 9;
            tv_fifteen.setText(gf.formatMoney(String.valueOf(total_msi),true) + " MXN");
            total_msi= _amount / 12;
            tv_twenty.setText(gf.formatMoney(String.valueOf(total_msi),true) + " MXN");
            total_msi= _amount / 18;
            tv_twentyfive.setText(gf.formatMoney(String.valueOf(total_msi),true) + " MXN");

        }else if(type_transaction.equals("venta")){
            tv_caption.setText("¿Desea agregar propina?");
            ll_twentyfivePercent.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

        int id =view.getId();
        if(id == R.id.Propinas_btn_continue)
            changeView();
        else
            changeCheck(id);

        if(type_transaction.equals("MSI") && id != R.id.Propinas_btn_continue){
            switch (id){
                case R.id.rBZero:
                    v_msi="3";
                    break;
                case R.id.rBTen:
                    v_msi="6";
                    break;
                case R.id.rBFifteen:
                    v_msi="9";
                    break;
                case R.id.rBTwenty:
                    v_msi="12";
                    break;
                case R.id.rBTwentyFive:
                    v_msi="18";
                    break;
            }
        }else if(id != R.id.Propinas_btn_continue){
            Float percetn=0.0F ;
            switch (id){
                case R.id.rBZero:
                    tv_propina_percent.setText(" (0%)");
                    break;
                case R.id.rBTen:
                    percetn = 0.10F;
                    tv_propina_percent.setText(" (10%)");
                    break;
                case R.id.rBFifteen:
                    percetn = 0.15F;
                    tv_propina_percent.setText(" (15%)");
                    break;
                case R.id.rBTwenty:
                    percetn = 0.20F;
                    tv_propina_percent.setText(" (20%)");
                    break;
                case R.id.rBOther:

                    if(et.getText().toString().equals(""))
                        tv_propina_percent.setText(" (0%)");
                    else{
                        calculateCustomTip();
                        tv_propina_percent.setText(" ("+et.getText().toString()+"%)");
                    }
                    break;
            }
            if(id != R.id.rBOther)
                setTipsPrice(percetn);
        }

    }

    private void changeCheck(int id){
        zero.setChecked(false);
        ten.setChecked(false);
        fifteen.setChecked(false);
        twenty.setChecked(false);
        twentyfive.setChecked(false);
        other.setChecked(false);
        et.setEnabled(false);

        switch (id){
            case R.id.rBZero:
                zero.setChecked(true);
                break;
            case R.id.rBTen:
                ten.setChecked(true);
                break;
            case R.id.rBFifteen:
                fifteen.setChecked(true);
                break;
            case R.id.rBTwenty:
                twenty.setChecked(true);
                break;
            case R.id.rBTwentyFive:
                twentyfive.setChecked(true);
                break;
            case R.id.rBOther:
                other.setChecked(true);
                et.setEnabled(true);
                break;
        }
    }

    private void changeTextListener(){
        et.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                calculateCustomTip();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

    }

    private void calculateCustomTip(){
        String str_customPer = et.getText().toString();
        if(!str_customPer.isEmpty()){
            Float f_customPer = Float.parseFloat(str_customPer);
            Float _amount = Float.parseFloat(Amount.replace(",",""));
            Float _total = _amount + (_amount * (f_customPer*.01F));
            tv_total.setText(gf.formatMoney(String.valueOf(_total),true) + " MXN");
            tv_propina_final.setText(gf.formatMoney(String.valueOf(_amount * (f_customPer*.01F)),true) + " MXN");
            tv_propina_percent.setText(" ("+str_customPer+"%)");
        }else{
            Float _amount = Float.parseFloat(Amount.replace(",",""));
            tv_total.setText(gf.formatMoney(String.valueOf(_amount),true) + " MXN");
            tv_propina_final.setText(gf.formatMoney(String.valueOf(0.0),true) + " MXN");
            tv_propina_percent.setText(" (0%)");
        }
    }

    private void changeView(){
        String tmp = tv_total.getText().toString().replace("$","").replace(",","").replace(" MXN","");
        if(ValidarMonto(Float.parseFloat(tmp))){
        intent = new Intent(this, WMX_Card.class);
        intent.putExtra("AmountToShow",tv_total.getText());
        intent.putExtra("type_transaction",type_transaction );
        intent.putExtra("ksn_posId",ksn_posId);
        //String tmp = tv_total.getText().toString().replace("$","").replace(",","").replace(" MXN","");
        intent.putExtra("Amount",tmp);

        intent.putExtra("total",tv_total.getText());

        if(type_transaction.equals("MSI")){
            intent.putExtra("months",v_msi);
            Float _amount = Float.parseFloat(Amount.replace(",",""));
            Float total_msi= _amount / Float.valueOf(v_msi);
            intent.putExtra("months_total",gf.formatMoney(String.valueOf(total_msi),true) + " MXN");
            intent.putExtra("propina","0.00");
        }else{
            intent.putExtra("subtotal",Total_Amount.getText());
            intent.putExtra("tips",tv_propina_final.getText());
            intent.putExtra("propina",tv_propina_final.getText().toString().replace("$","").replace(",","").replace(" MXN",""));
        }

        startActivityMiddleware(intent);
        }else{
            WMX_Propinas.super.showAlert("error", "¡No cumple importe mínimo de compra!");
        }
    }

    @Override
    public void onToolbarLinstener() { onBackPressed(); }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_propinas;
    }

    public void setTipsTexts (){
        Float _amount = Float.parseFloat(Amount.replace(",",""));
        Float tip= _amount * 0.10F;
        tv_ten.setText(gf.formatMoney(String.valueOf(tip),true));
        tip= _amount * 0.15F;
        tv_fifteen.setText(gf.formatMoney(String.valueOf(tip),true));
        tip= _amount * 0.2F;
        tv_twenty.setText(gf.formatMoney(String.valueOf(tip),true));
    }

    public void setTipsPrice (Float per){
        Float _amount = Float.parseFloat(Amount.replace(",",""));
        Float tip= _amount * per;
        _amount = _amount +tip;
        tv_total.setText(gf.formatMoney(String.valueOf(_amount),true) + " MXN");
        tv_propina_final.setText(gf.formatMoney(String.valueOf(tip),true) + " MXN");
    }
    public boolean ValidarMonto(Float importe){
        TRACE.d("IMPORTE:" + importe);
        if (type_transaction.equals("MSI"))
        {
            if(v_msi=="3" && importe>=Float.parseFloat(cursor.getString(17))){
                return true;
            }else if(v_msi=="6" && importe>=Float.parseFloat(cursor.getString(18))){
                return true;
            }else if(v_msi=="9" && importe>=Float.parseFloat(cursor.getString(19))){
                return true;
            }else if(v_msi=="12" && importe>=Float.parseFloat(cursor.getString(20))){
                return true;
            }else if(v_msi=="18" && importe>=Float.parseFloat(cursor.getString(21))){
                return true;
            }else {
                return false;
            }
        } else{
            return true;
        }

    }
    public void Habilitamsi(){
        // cursor
        if (cursor.getString(12).equals("0")){
            zero.setChecked(false);
            //zero.callOnClick();
            ll_ZeroPercent.setVisibility(View.GONE);
        }
        if (cursor.getString(13).equals("0")){
            ten.setChecked(false);
            //ten.callOnClick();
            ll_teenPercent.setVisibility(View.GONE);
        }
        if (cursor.getString(14).equals("0")){
            fifteen.setChecked(false);
            //fifteen.callOnClick();
            ll_fifteenPercent.setVisibility(View.GONE);
        }
        if (cursor.getString(15).equals("0")){
            twenty.setChecked(false);
            //twenty.callOnClick();
            ll_twentyPercent.setVisibility(View.GONE);
        }
        if (cursor.getString(16).equals("0")){
            twentyfive.setChecked(false);
            //twentyfive.callOnClick();
            ll_twentyfivePercent.setVisibility(View.GONE);
        }
        if (cursor.getString(12).equals("1") && cursor.getString(13).equals("1") && cursor.getString(14).equals("1")){
            zero.setChecked(true);
            //zero.callOnClick();
        }else if (cursor.getString(13).equals("1") && cursor.getString(14).equals("1") && cursor.getString(15).equals("1")){
            ten.setChecked(true);
            ten.callOnClick();
        }else if (cursor.getString(14).equals("1") && cursor.getString(15).equals("1") && cursor.getString(16).equals("1")){
            fifteen.setChecked(true);
            fifteen.callOnClick();
        }
    }
}
