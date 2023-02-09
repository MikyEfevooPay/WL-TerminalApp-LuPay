package com.efevoopay.demoui.activities;

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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.efevoopay.demoui.R;
import com.efevoopay.demoui.interfaces.TransactionsViewInterface;
import com.efevoopay.demoui.utils.TRACE;
import com.efevoopay.demoui.utils.Transaction;
import com.efevoopay.demoui.widget.TransactionItemAdapter2;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
    Intent intent;
    private String ksn_posId;
    private WMX_llamada_dukpt jsondukpt=new WMX_llamada_dukpt();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.switch_title_logo("Historial");
        super.show_calendar();

        btn_date = findViewById(R.id.btn_fecha);
        txt_date = findViewById(R.id.btn_date_txt);
        dpFecha = (DatePicker) findViewById(R.id.dpFecha);

        btn_date.setOnClickListener(this);
        txt_date.setOnClickListener(this);
        txt_date.setText(getFecha());
        DatePickerListener();

        intent = getIntent();
        ksn_posId = intent.getStringExtra("ksn_posId");
        try {
            readJsontxn();
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //readJson();

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

//    public void readJson(){
//        try {
//            JSONArray jsonArray = new JSONArray(JsonDataFromAsset());
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject data = jsonArray.getJSONObject(i);
//                Transaction _data = new Transaction(
//                        data.getString("noAuth"),
//                        data.getString("date"),
//                        data.getString("hour"),
//                        data.getString("amount"),
//                        data.getString("pan"),
//                        data.getString("procesador"),
//                        data.getString("tipo"),
//                        data.getString("approve"));
//                transactions.add(_data);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

   /* private String JsonDataFromAsset() throws IOException{
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
    }*/

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
                //readJson();
                try {
                    readJsontxn();
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
        intent.putExtra("redtarj", transactions.get(position).get_redtarj());
        intent.putExtra("tipotarj", transactions.get(position).get_tipotarj());
        intent.putExtra("status", transactions.get(position).get_tipotxn());
        intent.putExtra("propina", transactions.get(position).get_propina());
        intent.putExtra("total", transactions.get(position).get_total());
        intent.putExtra("msi", transactions.get(position).get_msi());
        intent.putExtra("approve", transactions.get(position).get_approve());
        intent.putExtra("ksn_posId",ksn_posId);

        startActivity(intent);
    }
    private void getHistorial(String _devicesid)throws IOException{
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://wmx-iso-apps1.eba-9vhqtwgu.us-west-2.elasticbeanstalk.com/matriz/certificacion/Dukptnumtxn";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("deviceid", _devicesid);
            jsonBody.put("pantalla", "Historial");
            final String requestBody = jsonBody.toString();
            TRACE.d("requestBody " +  TRACE.NEW_LINE + requestBody );
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    jsondukpt.readJsonnew(response.toString());
                    //TRACE.d("** ResponseResult " +  TRACE.NEW_LINE + response.toString() );
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + error.toString() );
                    WMX_Transaccion.super.showAlert("ERROR", error.toString());
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    String parsed;
                    try {
                        parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    } catch (UnsupportedEncodingException var4) {
                        parsed = new String(response.data);
                    }

                    if (response != null) {
                        responseString = String.valueOf(parsed);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }

            };
            transactions=jsondukpt.transactions;
            requestQueue.add(stringRequest);
        } catch (JSONException e) {

            TRACE.d("** ResponseResult ERROR " +  TRACE.NEW_LINE + e.toString() );

        }
    }
    public void readJsontxn(){
        try {
            getHistorial(ksn_posId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
