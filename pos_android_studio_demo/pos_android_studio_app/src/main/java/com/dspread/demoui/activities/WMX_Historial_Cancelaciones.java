package com.dspread.demoui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dspread.demoui.R;
import com.dspread.demoui.interfaces.TransactionsViewInterface;
import com.dspread.demoui.utils.Transaction;
import com.dspread.demoui.widget.CancelacionesItemAdapter;
import com.dspread.demoui.widget.TransactionItemAdapter2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class WMX_Historial_Cancelaciones extends BaseActivity implements View.OnClickListener, TransactionsViewInterface {
    RecyclerView recyclerView;
    ArrayList<Transaction> transactions = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.setCustomToolbarColor("#EFF2FF");
        super.switch_title_logo("Cancelaciones");

        readJson();

        recyclerView = findViewById(R.id.historial_cancelaciones_List);
        CancelacionesItemAdapter transactionItemAdapter = new CancelacionesItemAdapter(this,transactions, this);
        recyclerView.setAdapter(transactionItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    public void onClick(View view) {

    }

    @Override
    public void onToolbarLinstener() {
        onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_historial_cancelaciones;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(WMX_Historial_Cancelaciones.this, WMX_Cancelacion_Desc.class);
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

    public void readJson(){
        try {
            JSONArray jsonArray = new JSONArray(JsonDataFromAsset());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = jsonArray.getJSONObject(i);
                if(!data.getString("tipo").equals("A")){
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


}
