package com.efevoopay.demoui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.efevoopay.demoui.R;

import java.text.NumberFormat;

public class WMX_Terminal extends BaseActivity implements View.OnClickListener {
    private Button n0,n1,n2,n3,n4,n5,n6,n7,n8,n9,_transfer, _delete, nDot;
    private Intent intent;
    private TextView Amount;
    private Animation bounce;
    private String type_transaction,ksn_posId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(getString(R.string.wmx_title_welcome));
        intent = getIntent();
        type_transaction = intent.getStringExtra("type_transaction");
        ksn_posId = intent.getStringExtra("ksn_posId");

        nDot=(Button) findViewById(R.id.btn_Dot);
        n0=(Button) findViewById(R.id.btn_0);
        n1=(Button) findViewById(R.id.btn_1);
        n2=(Button) findViewById(R.id.btn_2);
        n3=(Button) findViewById(R.id.btn_3);
        n4=(Button) findViewById(R.id.btn_4);
        n5=(Button) findViewById(R.id.btn_5);
        n6=(Button) findViewById(R.id.btn_6);
        n7=(Button) findViewById(R.id.btn_7);
        n8=(Button) findViewById(R.id.btn_8);
        n9=(Button) findViewById(R.id.btn_9);
        _transfer=(Button) findViewById(R.id.WMX_btn_trade);
        Amount = (TextView) findViewById(R.id.wmx_label_Pin);
        _delete=(Button) findViewById(R.id.btn_delete);
        nDot.setOnClickListener(this);
        n0.setOnClickListener(this);
        n1.setOnClickListener(this);
        n2.setOnClickListener(this);
        n3.setOnClickListener(this);
        n4.setOnClickListener(this);
        n5.setOnClickListener(this);
        n6.setOnClickListener(this);
        n7.setOnClickListener(this);
        n8.setOnClickListener(this);
        n9.setOnClickListener(this);
        _transfer.setOnClickListener(this);
        _delete.setOnClickListener(this);

        bounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
    }

    @Override
    public void onToolbarLinstener() {
        onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_terminal;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            /*case R.id.btn_Dot:
                setAmount(".");
                break;*/
            case R.id.btn_0:
                setAmount("0");
                break;
            case R.id.btn_1:
                setAmount("1");
                break;
            case R.id.btn_2:
                setAmount("2");
                break;
            case R.id.btn_3:
                setAmount("3");
                break;
            case R.id.btn_4:
                setAmount("4");
                break;
            case R.id.btn_5:
                setAmount("5");
                break;
            case R.id.btn_6:
                setAmount("6");
                break;
            case R.id.btn_7:
                setAmount("7");
                break;
            case R.id.btn_8:
                setAmount("8");
                break;
            case R.id.btn_9:
                setAmount("9");
                break;
            case R.id.WMX_btn_trade:
                intent = new Intent(this, WMX_Propinas.class);
                intent.putExtra("Amount", (String) Amount.getText());

                intent.putExtra("type_transaction",type_transaction);
                intent.putExtra("ksn_posId",ksn_posId);
//                if(type_transaction.equals("msi")){
//                    intent.putExtra("type_transaction","msi");
//
//                }else if(type_transaction.equals("venta")){
//                    intent.putExtra("type_transaction","tip");
//                }

                startActivity(intent);
                break;
            case R.id.btn_delete:
                setAmount("del");
                break;

        }
    }

    private void setAmount(String num){
        String currentAmount = (String) Amount.getText();
        String NewAmount = "";

        if(num != "del"){
            if(currentAmount.length()<10){
                NewAmount = appendAmount(currentAmount, num);
            }else{
                NewAmount = currentAmount;
            }
        }else{
            NewAmount = deleteCharAmount(currentAmount);
        }


        Amount.setText(NewAmount);

        // Modificar boton si ya hay monto
        Float f = Float.parseFloat( NewAmount.replace(",",""));
        if(f > 0 ){
            _transfer.setEnabled(true);
            _transfer.setAlpha(1);
        }else{
            _transfer.setEnabled(false);
            _transfer.setAlpha(0.5F);
        }

    }

    private String appendAmount(String current, String _new){
        current =current.replace(",","");
        String NewAmount = "";
        char[] ch;

        if(current.length()>4){
            ch = new char[current.length()+1];
            ch[current.length()] = _new.charAt(0);
        }else{
            ch = new char[current.length()];
        }

        for (int i = 0; i < current.length(); i++) {
            ch[i] = current.charAt(i);
        }

        if(current.length()>4){
            char[] tmp = new char[ch.length];
            System.arraycopy(ch,0,tmp,0,ch.length);
            tmp[tmp.length-3] = ch[ch.length-4];
            tmp[tmp.length-4] = ch[ch.length-3];

            /*if(ch.length > 6){
                tmp[tmp.length-6] = ch[ch.length-5];
                tmp[tmp.length-5] = ch[ch.length-6];
            }*/

            ch=tmp;
        }else{
            if(ch[0] == '0' && ch[2] == '0' && ch[3] == '0'){
                ch [3]= _new.charAt(0);
            }else if(ch[0] == '0' && ch[2] == '0'){
                char[] tmp = new char[ch.length];
                System.arraycopy(ch,0,tmp,0,ch.length);
                tmp[tmp.length-2] = ch[ch.length-1];
                tmp[tmp.length-1] = _new.charAt(0);
                ch=tmp;
            }
            else if(ch[0] == '0' ){
                char[] tmp = new char[ch.length];
                System.arraycopy(ch,0,tmp,0,ch.length);
                tmp[tmp.length-4] = ch[ch.length-2];
                tmp[tmp.length-2] = ch[tmp.length-1];
                tmp[tmp.length-1] = _new.charAt(0);
                ch=tmp;
            }else{
                char[] tmp = new char[ch.length+1];
                System.arraycopy(ch,0,tmp,0,ch.length);
                tmp[tmp.length-1] = _new.charAt(0);

                tmp[tmp.length-4] = ch[ch.length-2];
                tmp[tmp.length-3] = ch[ch.length-3];
                ch=tmp;

            }
        }
        for (char c : ch) {
            NewAmount = NewAmount + c;
        }

        double money = Double.parseDouble(NewAmount);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(money);
        moneyString=moneyString.substring(1);

        return moneyString;
    }

    private String deleteCharAmount(String current) {
        current =current.replace(",","");
        Float amount = Float.parseFloat(current);
        amount = amount * 0.1F;
        String _final = String.valueOf(amount);
        String NewAmount = "";

        char[] ch = new char[_final.length()];;

        if (amount > 0){
            for (int i = 0; i < _final.length(); i++) {
                if(i>2 && _final.charAt(i-3)=='.') {
                    break;
                }
                ch[i] = _final.charAt(i);
            }


            for (char c : ch) {
                if(Character.isDigit(c) || c =='.')
                    NewAmount = NewAmount + c;
            }

        }else{
            NewAmount="0.00";
            Amount.startAnimation(bounce);
        }

        double money = Double.parseDouble(NewAmount);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(money);
        moneyString=moneyString.substring(1);

        return moneyString;
    }
}
