package com.lupay.demoui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.drawable.DrawableCompat;

import com.android.volley.Request;
import com.lupay.demoui.R;
import com.lupay.demoui.interfaces.FetchEntity;
import com.lupay.demoui.interfaces.FetchOptions;
import com.lupay.demoui.interfaces.TicketLayoutType;
import com.lupay.demoui.utils.DBManager;
import com.lupay.demoui.utils.Fetch;
import com.lupay.demoui.utils.FetchUIManager;
import com.lupay.demoui.utils.GNTBackEnd;
import com.lupay.demoui.utils.GlobalFunctions;
import com.lupay.demoui.utils.ResponseCode;
import com.lupay.demoui.utils.TRACE;
import com.lupay.demoui.utils.Ticket;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lupay.demoui.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WMX_Cancelacion_Desc extends BaseActivity  {
    TextView cp_tv_trans_type,cp_tv_auth,cp_tv_amount,cp_tv_tip,cp_tv_total,cp_tv_card,cp_tv_date_time,cp_tv_approve,cp_tv_tip_label,cp_tv_total_label,cp_tv_tipotarjeta,cp_tv_aid,cp_tv_arqc,tp_tv_amount;
    ImageView cp_iv_trans_type,cp_iv_process;
    LinearLayout cp_ll_content_card,ll_msi;
    AppCompatButton cp_btn_trans_cancelar, cp_btn_trans_final;
    Context mContext;
    private String card_provider, tipotarjeta,tarjeta, datetime,card,redtarj,_approve,emisor,nip,entrada;
    private int transaction_type, trans_id, newTrans_id;
    private Intent intent;
    private String ksn_posId;
    private String meses;
    private DBManager dbManager;
    Cursor cursor;
    private GlobalFunctions gf ;
    private final String CANCEL_PROSA = "cancel_prosa";
    private final String CANCEL_AMEX = "cancel_amex";
    public static ProgressDialog spinner;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.switch_title_logo("Detalle Transacción");
        Intent intent = getIntent();
        mContext=this;
        spinner = Utils.getLoaderSpinner(this);
        initData(intent);
        dbManager = new DBManager(mContext);
        dbManager.open();
        cursor = dbManager.fetch(ksn_posId);
        buttonListener();
    }

    @Override
    public void onToolbarLinstener() {
        onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wmx_cancelacion_prev;
    }

    @Override
    public TicketLayoutType getPrintLayout() {
        return TicketLayoutType.TRANSACTION;
    }


    @Override
    public void setTicketData(Ticket ticket) {
        ticket.setTrans_Type(cp_tv_trans_type.getText().toString())
                .setStatus("APROBADA")
                .setApprove(cp_tv_approve.getText().toString())
                .setCard(cp_tv_card.getText().toString())
                .setCardType(card_provider)
                .setCard_provider(tipotarjeta)
                .setDate_Time(datetime)
                .setAmount(cp_tv_amount.getText().toString())
                .setTip(cp_tv_tip.getText().toString())
                .setTotal(cp_tv_total.getText().toString())
                .setARQC(cp_tv_arqc.getText().toString())
                .setAID(cp_tv_aid.getText().toString())
                .setKsn_posId(ksn_posId)
                .setCursor(cursor)
                .setTransId(trans_id);
    }

    @Override
    public void addFetchs(FetchUIManager manager) throws Exception {
        Fetch history = manager.addFetch(CANCEL_PROSA, new FetchOptions(Utils.TERMINAL_API + "/matriz/cancelacion/dukpt", Request.Method.POST));
        history.setSetBodyListenner(this::getBodyProsa);
        Fetch details = manager.addFetch(CANCEL_AMEX, new FetchOptions(Utils.TERMINAL_AMEX + "/amex/tpv/txn1420", Request.Method.POST));
        details.setSetBodyListenner(this::getBodyAmex);
    }

    private void getBodyProsa(JSONObject body) throws JSONException {
        body.put("deviceid", ksn_posId);
        body.put("tipo", "DUKPT");
        body.put("tipotxn", "CAN");
        body.put("actualizada","1");
        body.put("id", trans_id);
    }
    private void getBodyAmex(JSONObject body) throws JSONException {
        body.put("deviceid", ksn_posId);
        body.put("tipo", "AMEX");
        body.put("tipotxn", "REV");
        body.put("entrada", "tarjetapresente");
        body.put("id", trans_id);
    }
    @Override
    public void onFetchCurrentResult(FetchEntity entity, @Nullable FetchEntity error) {
        super.onFetchCurrentResult(entity, error);
        if (entity.result == null)
            return;
        switch (entity.key) {
            case CANCEL_PROSA:
            case CANCEL_AMEX:
                if (spinner.isShowing())
                    spinner.dismiss();
                String code = approvedDukpt(entity.result.toString());
                if (code.equals("00") ||code.equals("000") || code.equals("400")) {
                    ChangeViewToTicket();
                } else if (code.equals("")) {
                    TRACE.d("CALL TRANSACTION ERROR ENTRY");
                    showAlert("error", "Respuesta no obtenida correctamente, consulte historial o intente nuevamente.");
                } else {
                    TRACE.d("CALL TRANSACTION ERROR ENTRY");
                    ResponseCode.CodeDetails details = ResponseCode.getCodeDetails(code);
                    showAlert("error", details.description);
                }
                break;
            default:
                break;
        }
    }
    private void initData(Intent intent){
        String auth,date,time,subtotal,status,propina,total,msi,aid,arqc, approve, fecha, segundos;
        auth = intent.getStringExtra("auth");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        fecha = intent.getStringExtra("datetime");
        segundos = !fecha.equals("") ? fecha.substring(fecha.length() - 3) : fecha;
        datetime = date + " " + time + segundos;
        subtotal = intent.getStringExtra("subtotal");
        card = intent.getStringExtra("card");
        redtarj = intent.getStringExtra("redtarj");
        tipotarjeta = intent.getStringExtra("tipotarj");
        if(tipotarjeta.equals("null"))
        {
            tipotarjeta="Crédito";
        }
        status = intent.getStringExtra("status");
        propina=intent.getStringExtra("propina");
        total=intent.getStringExtra("total");
        msi=intent.getStringExtra("msi");
        aid=intent.getStringExtra("aid");
        arqc=intent.getStringExtra("arqc");
        tarjeta=intent.getStringExtra("tarjeta");
        emisor= intent.getStringExtra("emisor");
        nip= intent.getStringExtra("nip");
        entrada= intent.getStringExtra("entrada");
        approve = intent.getStringExtra("approve");
        ksn_posId=intent.getStringExtra("ksn_posId");
        trans_id = intent.getIntExtra("id", 0);
        meses=msi;


        cp_tv_trans_type = findViewById(R.id.cp_tv_trans_type);
        cp_tv_auth = findViewById(R.id.cp_tv_auth);
        cp_tv_amount = findViewById(R.id.cp_tv_amount);
        cp_tv_tip = findViewById(R.id.cp_tv_tip);
        cp_tv_total = findViewById(R.id.cp_tv_total);
        cp_tv_card = findViewById(R.id.cp_tv_card);
        cp_tv_date_time = findViewById(R.id.cp_tv_date_time);
        cp_tv_approve = findViewById(R.id.cp_tv_approve);
        cp_iv_trans_type = findViewById(R.id.cp_iv_trans_type);
        cp_iv_process = findViewById(R.id.cp_iv_process);
        cp_ll_content_card = findViewById(R.id.cp_ll_content_card);
        //ll_msi = findViewById(R.id.ll_msi);
        tp_tv_amount=findViewById(R.id.tp_tv_amount);
        cp_tv_tip_label =findViewById(R.id.cp_tv_tip_label);
        cp_tv_total_label = findViewById(R.id.cp_tv_total_label);
        cp_tv_tipotarjeta = findViewById(R.id.cp_tv_tipotarjeta);
        cp_tv_aid = findViewById(R.id.txt_AID);
        cp_tv_arqc = findViewById(R.id.txt_ARQC);

        cp_tv_amount.setText(subtotal);
        cp_tv_tip.setText(propina);
         if (status.equals("VN")){
            cp_iv_trans_type.setImageResource(R.drawable.efevoo_i_check_exito);
            cp_tv_trans_type.setText(GNTBackEnd.getTitle(GNTBackEnd.TRANS_CAN_TYPE));
             transaction_type = 1;
        }else{
             cp_tv_trans_type.setText(GNTBackEnd.getTitle(GNTBackEnd.TRANS_CANMSI_TYPE));
//             Float _amount = Float.parseFloat(total.replace("$","").replace(",","").replace(" ",""));
//             Float total_msi= _amount / Integer.parseInt(msi);
//             NumberFormat format = NumberFormat.getCurrencyInstance();
//             format.setMaximumFractionDigits(2);
             cp_tv_total_label.setText(meses+"MSI");
             cp_tv_amount.setText(GNTBackEnd.Amount_msi(total,meses));
             //cp_tv_tip.setText(propina);
             //ll_msi.setVisibility(View.GONE);
             transaction_type = 0;
        }

        if (redtarj.toUpperCase(Locale.ROOT).equals("MC")){
            card_provider = "MASTERCARD";
            cp_iv_process.setImageResource(R.drawable.masterdcard);
        }else if(redtarj.toUpperCase(Locale.ROOT).equals("VISA")){
            cp_iv_process.setImageResource(R.drawable.visa);
        }else if(redtarj.toUpperCase(Locale.ROOT).equals("AMEX")){
            cp_iv_process.setImageResource(R.drawable.amex);
        }else {
            card_provider = "NA";
            cp_iv_process.setImageResource(R.drawable.internacional);
        }

        cp_tv_tipotarjeta.setText("Tarjeta "+tipotarjeta);
        cp_tv_auth.setText(auth);
        cp_tv_total.setText(total);
        cp_tv_card.setText("**** "+card);
        cp_tv_date_time.setText(date+" "+time);
        cp_tv_approve.setText(approve);
        cp_tv_aid.setText(aid);
        cp_tv_arqc.setText(arqc);
    }

    private void buttonListener(){

        cp_btn_trans_cancelar = findViewById(R.id.cp_btn_trans_cancelar);
        cp_btn_trans_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialAlertDialogBuilder(mContext, R.style.ThemeOverlay_App_MaterialAlertDialog)
                        .setTitle("¿Quieres cancelar la Transacción?")
                        .setIcon(R.drawable.efevoo_i_grupo_41699)
                        .setPositiveButton("Confirmar",(dialog, lis) -> {
                            dialog.dismiss();
                            //sendCancelFinal();
                            spinner.show();
                            cancelTransaction();
                        })
                        .setNeutralButton("Regresar",(dialog, lis) -> {
                            dialog.dismiss();
                        })
                .show();
            }
        });
        cp_btn_trans_final = findViewById(R.id.cp_btn_trans_final);
        cp_btn_trans_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrintTicket();
                onBackPressed();
            }
        });

    }

    private void sendCancelFinal(){
        cp_iv_trans_type.setImageResource(R.drawable.efevoo_i_grupo_41699);

        cp_tv_trans_type.setTextColor(0xFFCC1818);
        cp_tv_trans_type.setText("Cancelada Venta Normal");
        cp_tv_total_label.setTextColor(0xFF5A5A5A);
        cp_tv_total.setTextColor(0xFF5A5A5A);
        cp_tv_date_time.setTextColor(0xFF121212);

        Drawable layoutDrawable = cp_ll_content_card.getBackground();
        layoutDrawable = DrawableCompat.wrap(layoutDrawable);
        //the color is a direct color int and not a color resource
        DrawableCompat.setTint(layoutDrawable, 0xFFFDC0C0);
        cp_ll_content_card.setBackground(layoutDrawable);

        cp_btn_trans_cancelar.setVisibility(View.GONE);
        cp_btn_trans_final.setVisibility(View.VISIBLE);
    }
    private void changeView(){
        intent = new Intent(this, WMX_Card.class);
        intent.putExtra("AmountToShow",formatMoney(cp_tv_total.getText().toString().replace("$","").replace(",","").replace(" ","")));
        intent.putExtra("type_transaction","Cancelacion" );
        intent.putExtra("cp_tv_auth",cp_tv_auth.getText());
        intent.putExtra("trans_id", trans_id);
        intent.putExtra("ksn_posId",ksn_posId);
        String tmp = cp_tv_total.getText().toString().replace("$","").replace(",","").replace(" ","").replace(" MXN","");
        intent.putExtra("Amount",tmp);

        intent.putExtra("total",formatMoney(cp_tv_total.getText().toString().replace("$","").replace(",","").replace(" ","")));

        intent.putExtra("subtotal",formatMoney(cp_tv_amount.getText().toString().replace("$","").replace(",","").replace(" ","")));
        intent.putExtra("months",meses);
        intent.putExtra("approve",cp_tv_approve.getText().toString());
        intent.putExtra("tips",formatMoney(cp_tv_tip.getText().toString().replace("$","").replace(",","").replace(" ","")));
        intent.putExtra("propina",cp_tv_tip.getText().toString().replace("$","").replace(",","").replace(" ",""));
        intent.putExtra("tarjeta",tarjeta);
        startActivityMiddleware(intent);
    }
    private void ChangeViewToTicket() {
        intent = new Intent(this, WMX_final_ticket_transaction.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date date = new Date();
        intent.putExtra("type_transaction", GNTBackEnd.tipo("Cancelacion"));
        intent.putExtra("v_total", formatMoney(cp_tv_total.getText().toString().replace("$","").replace(",","").replace(" ","")));
        intent.putExtra("v_time", dateFormat.format(date).toString());
        intent.putExtra("v_card", "**** " + card);
        intent.putExtra("v_redtarjeta", redtarj);
        intent.putExtra("v_tipotarjeta", tipotarjeta);
        intent.putExtra("v_AID", cp_tv_aid.getText().toString());
        intent.putExtra("v_ARQC", cp_tv_arqc.getText().toString());
        intent.putExtra("v_noauth", "");
        intent.putExtra("v_approve", _approve);
        intent.putExtra("ksn_posId", ksn_posId);
        intent.putExtra("v_emisor", emisor);
        intent.putExtra("v_nip", nip);
        intent.putExtra("v_entrada", entrada);
        intent.putExtra("v_trans_id", newTrans_id);
        String v_subtotal = formatMoney(cp_tv_amount.getText().toString().replace("$","").replace(",","").replace(" ",""));
        String v_tip = formatMoney(cp_tv_tip.getText().toString().replace("$","").replace(",","").replace(" ",""));
        intent.putExtra("v_months", meses);
        intent.putExtra("v_tip", v_tip.toString());
        intent.putExtra("v_subtotal", v_subtotal.toString());

        getFetchManager().clear();

        startActivity(intent);
        finish();
    }
    private void cancelTransaction(){
        if(redtarj.toUpperCase(Locale.ROOT).equals("AMEX")){
            getFetchManager().CallById(CANCEL_AMEX);
        } else {
            getFetchManager().CallById(CANCEL_PROSA);
        }
    }
    public String formatMoney(String amount){
        String str="";
        str = "$" + amount +" MXN";
        return str;
    }
    public String approvedDukpt(String _json)
    {
        TRACE.d("approvedDukp(" + _json+")");
        String trans_code="";
        newTrans_id=0;
        _approve="";
        try {
            if (!_json.equals("")){
                JSONObject object = new JSONObject(_json);
                if(object.has("codigo"))
                {
                    _approve=object.getString("numref");
                    if(!object.getString("id").equals("")){
                        newTrans_id=Integer.parseInt(object.getString("id"));
                    }
                    trans_code=object.getString("codigo");
                }else if(object.has("msg")){
                    trans_code=object.getString("msg");
                    if (trans_code=="null")
                    {
                        trans_code="";
                    }
                }
                else{
                    trans_code=_json;
                }
            }
            else{
                trans_code=_json;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            trans_code="";
        }
        return trans_code;
    }
}
