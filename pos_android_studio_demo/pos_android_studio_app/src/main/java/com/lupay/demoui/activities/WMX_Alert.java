package com.lupay.demoui.activities;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lupay.demoui.R;

public class WMX_Alert extends AppCompatActivity {
    private Toast toast;
    private LayoutInflater inflater;
    public WMX_Alert (Context mContext){
        LayoutInflater inflater;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.wmx_alert, (ViewGroup)findViewById((R.id.custom_alert)));
        Toast toast = new Toast(mContext);
        toast.setGravity(Gravity.TOP, 0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setView(layout);
    }

    public void show(){
        if (toast!=null){
            toast.show();
        }
    }

}
