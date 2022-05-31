package com.dspread.demoui.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dspread.demoui.R;
import com.dspread.demoui.interfaces.TransactionsViewInterface;
import com.dspread.demoui.utils.Transaction;
import com.dspread.demoui.widget.TransactionItemAdapter2;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.function.Predicate;

public class WMX_Transaccion extends BaseActivity implements View.OnClickListener, TransactionsViewInterface {

    RecyclerView recyclerView;
    ArrayList<Transaction> transactions = new ArrayList<>();
    ImageButton btn_date;
    TextView txt_date;
    DatePicker dpFecha;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.setCustomToolbarColor("#EFF2FF");
        super.switch_title_logo("Historial");
        super.show_calendar();

        btn_date = findViewById(R.id.btn_fecha);
        txt_date = findViewById(R.id.btn_date_txt);
        dpFecha = (DatePicker) findViewById(R.id.dpFecha);

        btn_date.setOnClickListener(this);
        txt_date.setOnClickListener(this);
        txt_date.setText(getFecha());
        DatePickerListener();
        readJson();

        recyclerView = findViewById(R.id.transactionList);
        TransactionItemAdapter2 transactionItemAdapter = new TransactionItemAdapter2(this,transactions, this);
        recyclerView.setAdapter(transactionItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onToolbarLinstener() {
        onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_transaccion;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_fecha:
                showCalendar();
                break;
            case R.id.btn_date_txt:
                showCalendar();
                break;
        }
    }

    @Override
    public void onCalendarLinstener(){

        Locale locale = new Locale("es", "ES");
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());



        MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTheme(R.style.MaterialCalendarThemeBackground);
        MaterialDatePicker<Pair<Long, Long>> picker = builder.build();

        picker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

    };

    public void readJson(){
        try {
            JSONArray jsonArray = new JSONArray(JsonDataFromAsset());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = jsonArray.getJSONObject(i);
                Transaction _data = new Transaction(
                        data.getString("noAuth"),
                        data.getString("date"),
                        data.getString("hour"),
                        data.getString("amount"),
                        data.getString("pan"),
                        data.getString("procesador"),
                        data.getString("tipo"),
                        data.getString("approve"));
                transactions.add(_data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String JsonDataFromAsset() throws IOException{
        String json =null;
        try{
            InputStream inputStream = getAssets().open("dataDummy2.json");
            int sizeOfFile = inputStream.available();
            byte[] bufferData =  new byte[sizeOfFile];
            inputStream.read(bufferData);
            inputStream.close();
            json = new String(bufferData, "UTF-8");
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public void DatePickerListener() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        dpFecha.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {


                Date date1= null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yy").parse(dayOfMonth+"/"+(month+1)+"/"+year);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                String strDate = dateFormat.format(date1);

                txt_date.setText(strDate);
                dpFecha.setVisibility(View.GONE);
                readJson();
                FilterDate();
            }
        });
    }

    public void FilterDate(){

        Predicate<Transaction> byDate = person -> person.get_date().indexOf(txt_date.getText().toString()) == 0 ;
        ArrayList<Transaction> result = transactions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result.removeIf(t-> t.get_date().indexOf(txt_date.getText().toString()) != 0);
        }

        transactions = result;
    }

    public String getFecha()  {

        String dia = String.valueOf(dpFecha.getDayOfMonth());
        String mes = String.valueOf(dpFecha.getMonth()+1);
        String anio = String.valueOf(dpFecha.getYear());

        Date date1= null;
        try {
            date1 = new SimpleDateFormat("dd/MM/yy").parse(dia+"/"+mes+"/"+anio);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        String strDate = dateFormat.format(date1);

        return strDate;
    }

    public void showCalendar() {
        dpFecha.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(WMX_Transaccion.this, WMX_Transaction_Desc.class);
        intent.putExtra("auth", transactions.get(position).get_auth());
        intent.putExtra("date", transactions.get(position).get_date2());
        intent.putExtra("time", transactions.get(position).get_time());
        intent.putExtra("amount", transactions.get(position).get_amount2());
        intent.putExtra("card", transactions.get(position).get_card());
        intent.putExtra("process", transactions.get(position).get_process());
        intent.putExtra("status", transactions.get(position).get_status());
        intent.putExtra("approve", transactions.get(position).get_approve());

        startActivity(intent);
    }
}
