package com.efevoopay.demoui.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.efevoopay.demoui.R;
import com.efevoopay.demoui.interfaces.TicketLayoutType;

import java.util.Locale;

public class TicketLayoutManager {
    private View layout;
    private TicketLayoutType type;
    private PRINT_TYPE entity_print;
    private TextView txt_company, txt_address_1, txt_address_2, txt_address_3, txt_transaction_type,
            txt_ticket_date, txt_ticket_card, txt_ticket_card_type,txt_ticket_consumo_title, txt_ticket_consumo_value,
            txt_ticket_tip_value, txt_ticket_total_value, txt_ticket_status,txt_ticket_entity_type,
            txt_ticket_kposId, txt_ticket_aid, txt_ticket_arqc, txt_ticket_commerce_value;
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
        String CommerceNum = ticket.getCursor().getString(6);
        String[] address = ticket.getCursor().getString(8).split(", ");
        String company = Utils.isNull(ticket.getCursor().getString(9), "");
        switch (this.type) {
            case TRANSACTION:
                txt_company = layout.findViewById(R.id.txt_company);
                txt_address_1 = layout.findViewById(R.id.txt_address_1);
                txt_address_2 = layout.findViewById(R.id.txt_address_2);
                txt_address_3 = layout.findViewById(R.id.txt_address_3);
                txt_transaction_type = layout.findViewById(R.id.txt_transaction_type);
                txt_ticket_date = layout.findViewById(R.id.txt_ticket_date);
                txt_ticket_card = layout.findViewById(R.id.txt_ticket_card);
                txt_ticket_card_type = layout.findViewById(R.id.txt_ticket_card_type);
                if (Integer.parseInt(ticket.getMsi())>0){
                    txt_ticket_consumo_title = layout.findViewById(R.id.txt_ticket_consumo_title);
                    txt_ticket_consumo_title.setText(ticket.getMsi()+" MSI");
                }
                txt_ticket_consumo_value = layout.findViewById(R.id.txt_ticket_consumo_value);
                txt_ticket_tip_value = layout.findViewById(R.id.txt_ticket_tip_value);
                txt_ticket_total_value = layout.findViewById(R.id.txt_ticket_total_value);
                txt_ticket_status = layout.findViewById(R.id.txt_ticket_status);
                txt_ticket_entity_type = layout.findViewById(R.id.txt_ticket_entity_type);
                txt_ticket_kposId = layout.findViewById(R.id.txt_ticket_kposId);
                txt_ticket_aid = layout.findViewById(R.id.txt_ticket_aid);
                txt_ticket_arqc = layout.findViewById(R.id.txt_ticket_arqc);
                txt_ticket_commerce_value = layout.findViewById(R.id.txt_ticket_commerce_value);
                tbl_ticket_tip = layout.findViewById(R.id.tbl_ticket_tip);
                txt_company.setText(company);
                txt_address_1.setText(Utils.isVacio(address,0)+" "+Utils.isVacio(address,1)+" "+Utils.isVacio(address,2) + " " + Utils.isVacio(address,3));
                txt_address_2.setText(Utils.isVacio(address,4) + ", " +Utils.isVacio(address,5));
                txt_address_3.setText(Utils.isVacio(address,6));
                txt_transaction_type.setText(ticket.getTrans_type());
                txt_ticket_date.setText(ticket.getDate_time());
                txt_ticket_card.setText(ticket.getCard());
                txt_ticket_card_type.setText(ticket.getCard_type() + "/" + ticket.getCard_provider());
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
                txt_address_1 = layout.findViewById(R.id.txt_address_1);
                txt_address_2 = layout.findViewById(R.id.txt_address_2);
                txt_address_3 = layout.findViewById(R.id.txt_address_3);
                txt_transaction_type = layout.findViewById(R.id.txt_transaction_type);
                txt_ticket_date = layout.findViewById(R.id.txt_ticket_date);
                txt_ticket_consumo_value = layout.findViewById(R.id.txt_ticket_consumo_value);
                txt_ticket_tip_value = layout.findViewById(R.id.txt_ticket_tip_value);
                txt_ticket_total_value = layout.findViewById(R.id.txt_ticket_total_value);
                txt_ticket_kposId = layout.findViewById(R.id.txt_ticket_kposId);
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
        }
        return 0;
    }

}
