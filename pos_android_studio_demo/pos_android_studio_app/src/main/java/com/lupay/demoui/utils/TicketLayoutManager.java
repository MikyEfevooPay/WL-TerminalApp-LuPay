package com.lupay.demoui.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.RemoteException;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.action.printerservice.PrintStyle;
import com.lupay.demoui.R;
import com.lupay.demoui.interfaces.TicketLayoutType;
import com.dspread.print.device.PrinterDevice;
import com.dspread.print.device.bean.PrintLineStyle;
import com.dspread.print.widget.PrintLine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class TicketLayoutManager {
    private View layout;
    private TicketLayoutType type;
    private PRINT_TYPE entity_print;
    private TextView txt_company, txt_address_1, txt_address_2, txt_address_3, txt_transaction_type,
            txt_ticket_date, txt_ticket_card, txt_ticket_card_type,txt_ticket_consumo_title, txt_ticket_consumo_value,
            txt_ticket_tip_value, txt_ticket_total_value, txt_ticket_status,txt_ticket_entity_type,
            txt_ticket_kposId, txt_ticket_aid, txt_ticket_arqc, txt_ticket_commerce_value, txt_ticket_sign_message,txt_ticket_tip_title,txt_ticket_total_title,txt_ticket_card_title,txt_ticket_footer,
            txt_ticket_transacciones,txt_ticket_cancelaciones,txt_ticket_total_sin_propina_value,txt_ticket_total_propina_value,txt_ticket_total_neto_value,txt_ticket_visa_cantidad_value,
            txt_ticket_mastercard_value,txt_ticket_amex_cantidad_value,txt_ticket_otros_cantidad_value,txt_ticket_debito_total_value,txt_ticket_credito_total_value, txt_ticket_total_transacciones_value, txt_ticket_date2,
            txt_ticket_dates, txt_ticket_resumen_venta, txt_ticket_venta_title, txt_ticket_Hora_title, txt_ticket_monto_title, txt_ticket_total_sin_propina_title, txt_ticket_total_propina_title, txt_ticket_total_neto_title,
            txt_ticket_reporte_emisor, txt_ticket_emisor_title, txt_ticket_cantidad_title, txt_ticket_visa_title, txt_ticket_mastercard_title, txt_ticket_amex_title, txt_ticket_otros_title, txt_ticket_debito_title,
            txt_ticket_credito_title, txt_ticket_total_transacciones_title;
    private LinearLayout lyt_ticket_sign, lyt_ticket_separator_tiny_commerce, lyt_ticket_resumen_venta;
    private TableLayout table_ticket_resumen_venta;
    private TableRow tbl_ticket_tip;


    public View getLayout() {
        return this.layout;
    }

    public TicketLayoutType getType() {
        return this.type;
    }

    public TicketLayoutManager(LayoutInflater inflater, TicketLayoutType type, PRINT_TYPE entity_print) {
        this.type = type;
        this.entity_print = entity_print;
        this.layout = inflater.inflate(getLayoutTicket(type), null);
    }

    public void setTicketDataByLayout(Ticket ticket) {
        String CommerceNum = ticket.getCursor().getString(26);
        String[] address = ticket.getCursor().getString(8).split(", ");
        String company = Utils.isNull(ticket.getCursor().getString(9), "");
        String conpropina = ticket.getCursor().getString(35);
        switch (this.type) {
            case TRANSACTION:
                String nip = ticket.getCard_nip();
                String entrada = ticket.getCard_entrada();
                boolean showClientSign = Utils.tryIntParse(nip)==0 && entrada.equals("ICC");
                txt_company = layout.findViewById(R.id.txt_company);
                txt_company.getPaint().setAntiAlias(false);
                txt_address_1 = layout.findViewById(R.id.txt_address_1);
                txt_address_1.getPaint().setAntiAlias(false);
                txt_address_2 = layout.findViewById(R.id.txt_address_2);
                txt_address_2.getPaint().setAntiAlias(false);
                txt_address_3 = layout.findViewById(R.id.txt_address_3);
                txt_address_3.getPaint().setAntiAlias(false);
                txt_transaction_type = layout.findViewById(R.id.txt_transaction_type);
                txt_transaction_type.getPaint().setAntiAlias(false);
                txt_ticket_date = layout.findViewById(R.id.txt_ticket_date);
                txt_ticket_date.getPaint().setAntiAlias(false);
                txt_ticket_card = layout.findViewById(R.id.txt_ticket_card);
                txt_ticket_card.getPaint().setAntiAlias(false);
                txt_ticket_card_type = layout.findViewById(R.id.txt_ticket_card_type);
                txt_ticket_card_type.getPaint().setAntiAlias(false);
                txt_ticket_sign_message = layout.findViewById(R.id.txt_ticket_sign_message);
                txt_ticket_sign_message.getPaint().setAntiAlias(false);
                lyt_ticket_sign = layout.findViewById(R.id.lyt_ticket_sign);
                //lyt_ticket_sign.getPaint().setAntiAlias(false);
                lyt_ticket_separator_tiny_commerce = layout.findViewById(R.id.lyt_ticket_separator_tiny_commerce);
                //lyt_ticket_separator_tiny_commerce.getPaint().setAntiAlias(false);
                txt_ticket_consumo_title = layout.findViewById(R.id.txt_ticket_consumo_title);
                txt_ticket_consumo_title.getPaint().setAntiAlias(false);
                txt_ticket_tip_title = layout.findViewById(R.id.txt_ticket_tip_title);
                txt_ticket_tip_title.getPaint().setAntiAlias(false);
                txt_ticket_total_title = layout.findViewById(R.id.txt_ticket_total_title);
                txt_ticket_total_title.getPaint().setAntiAlias(false);
                txt_ticket_card_title = layout.findViewById(R.id.txt_ticket_card_title);
                txt_ticket_card_title.getPaint().setAntiAlias(false);
                txt_ticket_footer = layout.findViewById(R.id.txt_ticket_footer);
                txt_ticket_footer.getPaint().setAntiAlias(false);
                if (Integer.parseInt(ticket.getMsi())>0){
                    txt_ticket_consumo_title.setText(ticket.getMsi()+" MSI");
                }
                txt_ticket_consumo_value = layout.findViewById(R.id.txt_ticket_consumo_value);
                txt_ticket_consumo_value.getPaint().setAntiAlias(false);
                txt_ticket_tip_value = layout.findViewById(R.id.txt_ticket_tip_value);
                txt_ticket_tip_value.getPaint().setAntiAlias(false);
                txt_ticket_total_value = layout.findViewById(R.id.txt_ticket_total_value);
                txt_ticket_total_value.getPaint().setAntiAlias(false);
                txt_ticket_status = layout.findViewById(R.id.txt_ticket_status);
                txt_ticket_status.getPaint().setAntiAlias(false);
                txt_ticket_entity_type = layout.findViewById(R.id.txt_ticket_entity_type);
                txt_ticket_entity_type.getPaint().setAntiAlias(false);
                txt_ticket_kposId = layout.findViewById(R.id.txt_ticket_kposId);
                txt_ticket_kposId.getPaint().setAntiAlias(false);
                txt_ticket_aid = layout.findViewById(R.id.txt_ticket_aid);
                txt_ticket_aid.getPaint().setAntiAlias(false);
                txt_ticket_arqc = layout.findViewById(R.id.txt_ticket_arqc);
                txt_ticket_arqc.getPaint().setAntiAlias(false);
                txt_ticket_commerce_value = layout.findViewById(R.id.txt_ticket_commerce_value);
                txt_ticket_commerce_value.getPaint().setAntiAlias(false);
                tbl_ticket_tip = layout.findViewById(R.id.tbl_ticket_tip);
                //tbl_ticket_tip.getPaint().setAntiAlias(false);
                if(showClientSign) {
                    lyt_ticket_sign.setVisibility(View.VISIBLE);
                    lyt_ticket_separator_tiny_commerce.setVisibility(View.GONE);
                } else {
                    lyt_ticket_sign.setVisibility(View.GONE);
                    lyt_ticket_separator_tiny_commerce.setVisibility(View.VISIBLE);
                }
                txt_ticket_sign_message.setText(ticket.getCard_singtype());
                txt_company.setText(company);
                txt_address_1.setText(Utils.isVacio(address,0)+" "+Utils.isVacio(address,1)+" "+Utils.isVacio(address,2) + " " + Utils.isVacio(address,3));
                txt_address_2.setText(Utils.isVacio(address,4) + ", " +Utils.isVacio(address,5));
                txt_address_3.setText(Utils.isVacio(address,6));
                txt_transaction_type.setText(ticket.getTrans_type() + " " + (ticket.getTrans_id() != 0 ? ticket.getTrans_id() : ""));
                txt_ticket_date.setText(ticket.getDate_time());
                txt_ticket_card.setText(ticket.getCard());
                txt_ticket_card_type.setText(ticket.getCard_type() +"/"+ticket.getCard_emisor()+ "/" + ticket.getCard_provider());
                txt_ticket_consumo_value.setText(ticket.getAmount());
                txt_ticket_tip_value.setText(ticket.getTip());
                txt_ticket_total_value.setText(ticket.getTotal());
                txt_ticket_commerce_value.setText("COMERCIO " + CommerceNum);
                txt_ticket_status.setText(ticket.getStatus() + " " +ticket.getApprove());
                txt_ticket_entity_type.setText(getEntityType(this.entity_print));
                txt_ticket_kposId.setText("Núm. SERIE " + ticket.getKsn_posId());
                txt_ticket_aid.setText("AID  " + ticket.getAID());
                txt_ticket_arqc.setText("ARQC  " + Utils.maskText(ticket.getARQC(), 4));
                if(ticket.getTrans_type().equals(GNTBackEnd.getTitle(GNTBackEnd.TRANS_MSI_TYPE).toUpperCase(Locale.ROOT))) tbl_ticket_tip.setVisibility(View.GONE);
                break;
            case CORTE:
                txt_company = layout.findViewById(R.id.txt_company);
                txt_company.getPaint().setAntiAlias(false);
                txt_address_1 = layout.findViewById(R.id.txt_address_1);
                txt_address_1.getPaint().setAntiAlias(false);
                txt_address_2 = layout.findViewById(R.id.txt_address_2);
                txt_address_2.getPaint().setAntiAlias(false);
                txt_address_3 = layout.findViewById(R.id.txt_address_3);
                txt_address_3.getPaint().setAntiAlias(false);
                txt_transaction_type = layout.findViewById(R.id.txt_transaction_type);
                txt_transaction_type.getPaint().setAntiAlias(false);
                txt_ticket_date = layout.findViewById(R.id.txt_ticket_date);
                txt_ticket_date.getPaint().setAntiAlias(false);
                txt_ticket_consumo_value = layout.findViewById(R.id.txt_ticket_consumo_value);
                txt_ticket_consumo_value.getPaint().setAntiAlias(false);
                txt_ticket_tip_value = layout.findViewById(R.id.txt_ticket_tip_value);
                txt_ticket_tip_value.getPaint().setAntiAlias(false);
                txt_ticket_total_value = layout.findViewById(R.id.txt_ticket_total_value);
                txt_ticket_total_value.getPaint().setAntiAlias(false);
                txt_ticket_kposId = layout.findViewById(R.id.txt_ticket_kposId);
                txt_ticket_kposId.getPaint().setAntiAlias(false);
                txt_company.setText(company);
                txt_address_1.setText(Utils.isVacio(address,0)+" "+Utils.isVacio(address,1)+" "+Utils.isVacio(address,2) + " " + Utils.isVacio(address,3));
                txt_address_2.setText(Utils.isVacio(address,4) + ", " +Utils.isVacio(address,5));
                txt_address_3.setText(Utils.isVacio(address,6));
                txt_transaction_type.setText(ticket.getTrans_type());
                txt_ticket_date.setText(ticket.getDate_time());
                txt_ticket_consumo_value.setText(ticket.getAmount());
                txt_ticket_tip_value.setText(ticket.getTip());
                txt_ticket_total_value.setText(ticket.getTotal());
                txt_ticket_kposId.setText("Núm. SERIE " + ticket.getKsn_posId());
                txt_ticket_consumo_title = layout.findViewById(R.id.txt_ticket_consumo_title);
                txt_ticket_consumo_title.getPaint().setAntiAlias(false);
                txt_ticket_tip_title = layout.findViewById(R.id.txt_ticket_tip_title);
                txt_ticket_tip_title.getPaint().setAntiAlias(false);
                txt_ticket_total_title = layout.findViewById(R.id.txt_ticket_total_title);
                txt_ticket_total_title.getPaint().setAntiAlias(false);
                break;
            case HISTORIAL:
                txt_company = layout.findViewById(R.id.txt_company);
                txt_company.getPaint().setAntiAlias(false);
                txt_address_1 = layout.findViewById(R.id.txt_address_1);
                txt_address_1.getPaint().setAntiAlias(false);
                txt_address_2 = layout.findViewById(R.id.txt_address_2);
                txt_address_2.getPaint().setAntiAlias(false);
                txt_address_3 = layout.findViewById(R.id.txt_address_3);
                txt_address_3.getPaint().setAntiAlias(false);
                txt_transaction_type = layout.findViewById(R.id.txt_transaction_type);
                txt_transaction_type.getPaint().setAntiAlias(false);
                txt_ticket_date = layout.findViewById(R.id.txt_ticket_date);
                txt_ticket_date.getPaint().setAntiAlias(false);
                txt_ticket_date2 = layout.findViewById(R.id.txt_ticket_date2);
                txt_ticket_date2.getPaint().setAntiAlias(false);
                txt_ticket_kposId = layout.findViewById(R.id.txt_ticket_kposId);
                txt_ticket_kposId.getPaint().setAntiAlias(false);
                txt_ticket_dates = layout.findViewById(R.id.txt_ticket_dates);
                txt_ticket_dates.getPaint().setAntiAlias(false);
                txt_ticket_resumen_venta = layout.findViewById(R.id.txt_ticket_resumen_venta);
                txt_ticket_resumen_venta.getPaint().setAntiAlias(false);
                txt_ticket_venta_title = layout.findViewById(R.id.txt_ticket_venta_title);
                txt_ticket_venta_title.getPaint().setAntiAlias(false);
                txt_ticket_Hora_title = layout.findViewById(R.id.txt_ticket_Hora_title);
                txt_ticket_Hora_title.getPaint().setAntiAlias(false);
                txt_ticket_monto_title = layout.findViewById(R.id.txt_ticket_monto_title);
                txt_ticket_monto_title.getPaint().setAntiAlias(false);
                txt_ticket_total_sin_propina_title = layout.findViewById(R.id.txt_ticket_total_sin_propina_title);
                txt_ticket_total_sin_propina_title.getPaint().setAntiAlias(false);
                txt_ticket_total_propina_title = layout.findViewById(R.id.txt_ticket_total_propina_title);
                txt_ticket_total_propina_title.getPaint().setAntiAlias(false);
                txt_ticket_total_neto_title = layout.findViewById(R.id.txt_ticket_total_neto_title);
                txt_ticket_total_neto_title.getPaint().setAntiAlias(false);
                txt_ticket_reporte_emisor = layout.findViewById(R.id.txt_ticket_reporte_emisor);
                txt_ticket_reporte_emisor.getPaint().setAntiAlias(false);
                txt_ticket_emisor_title = layout.findViewById(R.id.txt_ticket_emisor_title);
                txt_ticket_emisor_title.getPaint().setAntiAlias(false);
                txt_ticket_cantidad_title = layout.findViewById(R.id.txt_ticket_cantidad_title);
                txt_ticket_cantidad_title.getPaint().setAntiAlias(false);
                txt_ticket_visa_title = layout.findViewById(R.id.txt_ticket_visa_title);
                txt_ticket_visa_title.getPaint().setAntiAlias(false);
                txt_ticket_mastercard_title = layout.findViewById(R.id.txt_ticket_mastercard_title);
                txt_ticket_mastercard_title.getPaint().setAntiAlias(false);
                txt_ticket_amex_title = layout.findViewById(R.id.txt_ticket_amex_title);
                txt_ticket_amex_title.getPaint().setAntiAlias(false);
                txt_ticket_otros_title = layout.findViewById(R.id.txt_ticket_otros_title);
                txt_ticket_otros_title.getPaint().setAntiAlias(false);
                txt_ticket_debito_title = layout.findViewById(R.id.txt_ticket_debito_title);
                txt_ticket_debito_title.getPaint().setAntiAlias(false);
                txt_ticket_credito_title = layout.findViewById(R.id.txt_ticket_credito_title);
                txt_ticket_credito_title.getPaint().setAntiAlias(false);
                txt_ticket_total_transacciones_title = layout.findViewById(R.id.txt_ticket_total_transacciones_title);
                txt_ticket_total_transacciones_title.getPaint().setAntiAlias(false);

                txt_ticket_transacciones = layout.findViewById(R.id.txt_ticket_transacciones);
                txt_ticket_transacciones.getPaint().setAntiAlias(false);
                txt_ticket_cancelaciones = layout.findViewById(R.id.txt_ticket_cancelaciones);
                txt_ticket_cancelaciones.getPaint().setAntiAlias(false);
                txt_ticket_total_sin_propina_value = layout.findViewById(R.id.txt_ticket_total_sin_propina_value);
                txt_ticket_total_sin_propina_value.getPaint().setAntiAlias(false);
                txt_ticket_total_propina_value = layout.findViewById(R.id.txt_ticket_total_propina_value);
                txt_ticket_total_propina_value.getPaint().setAntiAlias(false);
                txt_ticket_total_neto_value = layout.findViewById(R.id.txt_ticket_total_neto_value);
                txt_ticket_total_neto_value.getPaint().setAntiAlias(false);
                txt_ticket_visa_cantidad_value = layout.findViewById(R.id.txt_ticket_visa_cantidad_value);
                txt_ticket_visa_cantidad_value.getPaint().setAntiAlias(false);
                txt_ticket_mastercard_value = layout.findViewById(R.id.txt_ticket_mastercard_value);
                txt_ticket_mastercard_value.getPaint().setAntiAlias(false);
                txt_ticket_amex_cantidad_value = layout.findViewById(R.id.txt_ticket_amex_cantidad_value);
                txt_ticket_amex_cantidad_value.getPaint().setAntiAlias(false);
                txt_ticket_otros_cantidad_value = layout.findViewById(R.id.txt_ticket_otros_cantidad_value);
                txt_ticket_otros_cantidad_value.getPaint().setAntiAlias(false);
                txt_ticket_debito_total_value = layout.findViewById(R.id.txt_ticket_debito_total_value);
                txt_ticket_debito_total_value.getPaint().setAntiAlias(false);
                txt_ticket_credito_total_value = layout.findViewById(R.id.txt_ticket_credito_total_value);
                txt_ticket_credito_total_value.getPaint().setAntiAlias(false);
                txt_ticket_total_transacciones_value = layout.findViewById(R.id.txt_ticket_total_transacciones_value);
                txt_ticket_total_transacciones_value.getPaint().setAntiAlias(false);
                lyt_ticket_resumen_venta = layout.findViewById(R.id.lyt_ticket_resumen_venta);
                table_ticket_resumen_venta = layout.findViewById(R.id.table_ticket_resumen_venta);

                txt_company.setText(company);
                txt_address_1.setText(Utils.isVacio(address,0)+" "+Utils.isVacio(address,1)+" "+Utils.isVacio(address,2) + " " + Utils.isVacio(address,3));
                txt_address_2.setText(Utils.isVacio(address,4) + ", " +Utils.isVacio(address,5));
                txt_address_3.setText(Utils.isVacio(address,6));
                txt_transaction_type.setText(ticket.getTrans_type());
                txt_ticket_date.setText(ticket.getDate_time2());
                txt_ticket_date2.setText(ticket.getDate_time());
                txt_ticket_total_sin_propina_value.setText(ticket.getAmount());
                txt_ticket_total_propina_value.setText(ticket.getTip());
                txt_ticket_total_neto_value.setText(ticket.getTotal());
                txt_ticket_kposId.setText("Núm. SERIE " + ticket.getKsn_posId());
                txt_ticket_transacciones.setText("Transacciones: " + ticket.getTotal_Transacciones());
                txt_ticket_cancelaciones.setText("Cancelaciones: "+ ticket.getTotal_Cancelaciones());
                txt_ticket_visa_cantidad_value.setText(ticket.getTotal_Visa());
                txt_ticket_mastercard_value.setText(ticket.getTotal_MC());
                txt_ticket_amex_cantidad_value.setText(ticket.getTotal_AMEX());
                txt_ticket_otros_cantidad_value.setText(ticket.getTotal_Otro());
                txt_ticket_debito_total_value.setText(ticket.getTotal_Debito());
                txt_ticket_credito_total_value.setText(ticket.getTotal_Credito());
                txt_ticket_total_transacciones_value.setText(ticket.getTotal_Movimientos());

                try{
                    JSONArray array = new JSONArray(ticket.getTable_Rows());
                    for (int i = 0; i <array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        String venta = object.getString("id");
                        String hora =  object.getString("date") + " " + object.getString("hour");
                        String monto = object.getString("subtotal").replace(" MXN","");

                        TableRow fila = new TableRow(layout.getContext());
                        fila.setLayoutParams(new TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.MATCH_PARENT
                        ));
                        fila.addView(crearCelda(venta, Gravity.LEFT));
                        fila.addView(crearCelda(hora,Gravity.CENTER));
                        fila.addView(crearCelda(monto,Gravity.RIGHT));

                        table_ticket_resumen_venta.addView(fila);
                    }
                } catch (JSONException e) {
                    TRACE.d(e.getMessage());
                }
                break;
        }
    }
    private String getEntityType(PRINT_TYPE entity) {
        switch (entity) {
            case STORE:
                return "Comercio";
            case  CLIENT:
                return "Copia cliente";
        }
        return "";
    }

    private int getLayoutTicket(TicketLayoutType LayoutTicket) {
        switch (LayoutTicket) {
            case CORTE:
                return R.layout.wmx_corte_ticket;
            case TRANSACTION:
                return R.layout.wmx_transaction_ticket;
            case HISTORIAL:
                return R.layout.wmx_historial_ticket;
        }
        return 0;
    }

    private TextView crearCelda(String texto, int gravity){
        TextView tv = new TextView(layout.getContext());
        tv.setText(texto);
        tv.setTextSize(9);
        tv.setGravity(gravity);
        Typeface t = ResourcesCompat.getFont(layout.getContext(), R.font.dm_sans_medium);
        tv.setTypeface(t);
        tv.setTextColor(Color.BLACK);
        tv.setAlpha(1f);
        tv.setVisibility(View.VISIBLE);
        tv.getPaint().setAntiAlias(false);
        TableRow.LayoutParams params;
        if(gravity == Gravity.LEFT){
            params = new TableRow.LayoutParams(60, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        }
        tv.setLayoutParams(params);
        return tv;
    }
}
