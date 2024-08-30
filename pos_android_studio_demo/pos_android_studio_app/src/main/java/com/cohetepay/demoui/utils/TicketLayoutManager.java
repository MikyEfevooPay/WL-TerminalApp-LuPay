package com.cohetepay.demoui.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.action.printerservice.PrintStyle;
import com.cohetepay.demoui.R;
import com.cohetepay.demoui.interfaces.TicketLayoutType;
import com.dspread.print.device.PrinterDevice;
import com.dspread.print.device.bean.PrintLineStyle;
import com.dspread.print.widget.PrintLine;

import java.util.Locale;

public class TicketLayoutManager {
    private View layout;
    private TicketLayoutType type;
    private PRINT_TYPE entity_print;
    private TextView txt_company, txt_address_1, txt_address_2, txt_address_3, txt_transaction_type,
            txt_ticket_date, txt_ticket_card, txt_ticket_card_type,txt_ticket_consumo_title, txt_ticket_consumo_value,
            txt_ticket_tip_value, txt_ticket_total_value, txt_ticket_status,txt_ticket_entity_type,
            txt_ticket_kposId, txt_ticket_aid, txt_ticket_arqc, txt_ticket_commerce_value, txt_ticket_sign_message;
    private LinearLayout lyt_ticket_sign, lyt_ticket_separator_tiny_commerce;
    private TableRow tbl_ticket_tip;

    private PrintLineStyle printLineStyle;
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
        switch (this.type) {
            case TRANSACTION:
                String nip = ticket.getCard_nip();
                String entrada = ticket.getCard_entrada();
                boolean showClientSign = Utils.tryIntParse(nip)==0 && entrada.equals("ICC");
                txt_company = layout.findViewById(R.id.txt_company);
                txt_address_1 = layout.findViewById(R.id.txt_address_1);
                txt_address_2 = layout.findViewById(R.id.txt_address_2);
                txt_address_3 = layout.findViewById(R.id.txt_address_3);
                txt_transaction_type = layout.findViewById(R.id.txt_transaction_type);
                txt_ticket_date = layout.findViewById(R.id.txt_ticket_date);
                txt_ticket_card = layout.findViewById(R.id.txt_ticket_card);
                txt_ticket_card_type = layout.findViewById(R.id.txt_ticket_card_type);
                txt_ticket_sign_message = layout.findViewById(R.id.txt_ticket_sign_message);
                lyt_ticket_sign = layout.findViewById(R.id.lyt_ticket_sign);
                lyt_ticket_separator_tiny_commerce = layout.findViewById(R.id.lyt_ticket_separator_tiny_commerce);
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
                txt_ticket_aid.setText("AID  " + Utils.maskText(ticket.getAID(), 4));
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
    public PrinterDevice setTicketLayout(Ticket ticket, PrinterDevice mPrinter) {
        String CommerceNum = ticket.getCursor().getString(26);
        String[] address = ticket.getCursor().getString(8).split(", ");
        String company = Utils.isNull(ticket.getCursor().getString(9), "");
        printLineStyle = new PrintLineStyle();
        mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.BOLD, PrintLine.CENTER, 11));
        Bitmap bitmap = BitmapFactory.decodeResource(this.getLayout().getResources(), R.drawable.logo_cohetepay_bmp_try3);
        switch (this.type) {
            case TRANSACTION:
                String nip = ticket.getCard_nip();
                String entrada = ticket.getCard_entrada();
                boolean showClientSign = Utils.tryIntParse(nip)==0 && entrada.equals("ICC");
                try {

//                    printLineStyle.setFontStyle(PrintStyle.FontStyle.BOLD);
//                    printLineStyle.setFontSize(10);
//                    printLineStyle.setAlign(PrintLine.CENTER);
//                    mPrinter.addPrintLintStyle(printLineStyle);
                    mPrinter.addBitmap(Bitmap.createScaledBitmap(bitmap, 375, 100, false));
                    mPrinter.setPrintStyle(printLineStyle);
                    mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.BOLD, PrintLine.CENTER, 11));
                    mPrinter.addText(company);
                    mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.NORMAL, PrintLine.CENTER, 9));
                    mPrinter.addText(Utils.isVacio(address,0)+" "+Utils.isVacio(address,1)+" "+Utils.isVacio(address,2) + " " + Utils.isVacio(address,3));
                    mPrinter.addText(Utils.isVacio(address,4) + ", " +Utils.isVacio(address,5));
                    mPrinter.addText(Utils.isVacio(address,6));
                    mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.NORMAL, PrintLine.CENTER, 8));
                    mPrinter.addText("________");
                    mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.BOLD, PrintLine.CENTER, 9));
                    mPrinter.addText(ticket.getTrans_type() + " " + (ticket.getTrans_id() != 0 ? ticket.getTrans_id() : ""));
                    mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.NORMAL, PrintLine.CENTER, 8));
                    mPrinter.addText(ticket.getDate_time());
                    mPrinter.addText("Número de tarjeta");
                    mPrinter.addText(ticket.getCard());
                    mPrinter.addText(ticket.getCard_type() +"/"+ticket.getCard_emisor()+ "/" + ticket.getCard_provider());
                    mPrinter.addText("________________________________________________");
                    mPrinter.addTexts(new String[]{"Consumo", ticket.getAmount()}, new int[]{5, 5}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.ALIGN_OPPOSITE});
                    mPrinter.addTexts(new String[]{"Propina", ticket.getTip()}, new int[]{5, 5}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.ALIGN_OPPOSITE});
                    mPrinter.addTexts(new String[]{"Total:", ticket.getTotal()}, new int[]{5, 10}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.ALIGN_OPPOSITE,PrintStyle.FontStyle.BOLD});
                    mPrinter.addText("________________________________________________");
                    mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.BOLD, PrintLine.CENTER, 9));
                    mPrinter.addText(ticket.getStatus() + " " +ticket.getApprove());
                    mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.NORMAL, PrintLine.CENTER, 8));
                    mPrinter.addText(getEntityType(this.entity_print));
                    mPrinter.addText("________");
                    mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.BOLD, PrintLine.CENTER, 9));
                    mPrinter.addText("COMERCIO " + CommerceNum);
                    mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.NORMAL, PrintLine.CENTER, 8));
                    mPrinter.addText(ticket.getCard_singtype());
                    mPrinter.addText("________");
                    mPrinter.addText("Núm. SERIE " + ticket.getKsn_posId());
                    mPrinter.addText("AID  " + Utils.maskText(ticket.getAID(), 4));
                    mPrinter.addText("ARQC  " + Utils.maskText(ticket.getARQC(), 4));
                    mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.NORMAL, PrintLine.CENTER, 6));
                    mPrinter.addText("Por este pagaré me obligo incondicionalmente a pagar a la orden del banco acreditante el importe de este título. Este pagaré procede del contrato de apertura de crédito que el banco acreditante y el tarjetahabiente tienen celebrado.");
                    mPrinter.addText("");
                    mPrinter.addText("");
                    mPrinter.setFooter(100);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                /*

                txt_ticket_commerce_value.setText("COMERCIO " + CommerceNum);
                txt_ticket_status.setText(ticket.getStatus() + " " +ticket.getApprove());
                txt_ticket_entity_type.setText(getEntityType(this.entity_print));
                txt_ticket_kposId.setText("Núm. SERIE " + ticket.getKsn_posId());
                txt_ticket_aid.setText("AID  " + Utils.maskText(ticket.getAID(), 4));
                txt_ticket_arqc.setText("ARQC  " + Utils.maskText(ticket.getARQC(), 4));
                if(ticket.getTrans_type().equals(GNTBackEnd.getTitle(GNTBackEnd.TRANS_MSI_TYPE).toUpperCase(Locale.ROOT))) tbl_ticket_tip.setVisibility(View.GONE);*/
                break;
            case CORTE:
                try {
                    mPrinter.addBitmap(Bitmap.createScaledBitmap(bitmap, 375, 100, false));
                    mPrinter.setPrintStyle(printLineStyle);
                    mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.BOLD, PrintLine.CENTER, 11));
                    mPrinter.addText(company);
                    mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.NORMAL, PrintLine.CENTER, 10));
                    mPrinter.addText(Utils.isVacio(address,0)+" "+Utils.isVacio(address,1)+" "+Utils.isVacio(address,2) + " " + Utils.isVacio(address,3));
                    mPrinter.addText(Utils.isVacio(address,4) + ", " +Utils.isVacio(address,5));
                    mPrinter.addText(Utils.isVacio(address,6));
                    mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.NORMAL, PrintLine.CENTER, 8));
                    mPrinter.addText("________");
                    mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.BOLD, PrintLine.CENTER, 10));
                    mPrinter.addText(ticket.getTrans_type());
                    mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.NORMAL, PrintLine.CENTER, 9));
                    mPrinter.addText(ticket.getDate_time());
                    mPrinter.addText("__________________________________________");
                    mPrinter.addTexts(new String[]{"Subtotal", ticket.getAmount()}, new int[]{5, 5}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.ALIGN_OPPOSITE});
                    mPrinter.addTexts(new String[]{"Propina", ticket.getTip()}, new int[]{5, 5}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.ALIGN_OPPOSITE});
                    mPrinter.addTexts(new String[]{"Total:", ticket.getTotal()}, new int[]{5, 10}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.ALIGN_OPPOSITE,PrintStyle.FontStyle.BOLD});
                    mPrinter.addText("__________________________________________");
                    mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.BOLD, PrintLine.CENTER, 10));
                    mPrinter.addText("FIN DEL RESUMEM");
                    mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.NORMAL, PrintLine.CENTER, 8));
                    mPrinter.addText("________");
                    mPrinter.addText("Núm. SERIE " + ticket.getKsn_posId());
                    mPrinter.addText("");
                    mPrinter.addText("");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }


//                txt_company = layout.findViewById(R.id.txt_company);
//                txt_address_1 = layout.findViewById(R.id.txt_address_1);
//                txt_address_2 = layout.findViewById(R.id.txt_address_2);
//                txt_address_3 = layout.findViewById(R.id.txt_address_3);
//                txt_transaction_type = layout.findViewById(R.id.txt_transaction_type);
//                txt_ticket_date = layout.findViewById(R.id.txt_ticket_date);
//                txt_ticket_consumo_value = layout.findViewById(R.id.txt_ticket_consumo_value);
//                txt_ticket_tip_value = layout.findViewById(R.id.txt_ticket_tip_value);
//                txt_ticket_total_value = layout.findViewById(R.id.txt_ticket_total_value);
//                txt_ticket_kposId = layout.findViewById(R.id.txt_ticket_kposId);
//                txt_company.setText(company);
//                txt_address_1.setText(Utils.isVacio(address,0)+" "+Utils.isVacio(address,1)+" "+Utils.isVacio(address,2) + " " + Utils.isVacio(address,3));
//                txt_address_2.setText(Utils.isVacio(address,4) + ", " +Utils.isVacio(address,5));
//                txt_address_3.setText(Utils.isVacio(address,6));
//                txt_transaction_type.setText(ticket.getTrans_type());
//                txt_ticket_date.setText(ticket.getDate_time());
//                txt_ticket_consumo_value.setText(ticket.getAmount());
//                txt_ticket_tip_value.setText(ticket.getTip());
//                txt_ticket_total_value.setText(ticket.getTotal());
//                txt_ticket_kposId.setText("Núm. SERIE " + ticket.getKsn_posId());
                break;
        }
        return mPrinter;
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
