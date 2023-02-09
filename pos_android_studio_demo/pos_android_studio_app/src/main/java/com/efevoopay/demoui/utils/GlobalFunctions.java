package com.efevoopay.demoui.utils;

import android.content.Context;

import java.text.NumberFormat;

public class GlobalFunctions {
    Context mContext;
    public GlobalFunctions(Context context){
        this.mContext = context;
    }

    public String formatMoney(String amount, Boolean withSign ){
        amount = amount.replace(",","");
        double money = Double.parseDouble(amount);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(money);
        if(!withSign)
            moneyString=moneyString.substring(1);

        return moneyString;
    }

    public String unFormatMoney(String formatMoney) {
        return "";
    }
}
