package com.efevoopay.demoui.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;

import androidx.core.content.ContextCompat;

import com.action.printerservice.ActionPrinter;
import com.action.printerservice.IPrinterCallback;
import com.action.printerservice.PrintStyle;
import com.efevoopay.demoui.R;
import com.efevoopay.demoui.activities.WMX_Menu;
import java.util.Locale;

public class Ticket {
    private String trans_type, approve, card, card_type, date_time, amount, tip, total, ARQC, AID;
    private final String SEPARATOR = "__________________________________";
    private android.content.Context ctx;

    public Ticket(android.content.Context ctx) {
        this.ctx = ctx;
        ActionPrinter.getInstance(ctx).bind();
    }


    public void setData(String trans_type, String approve, String card, String card_type, String date_time, String amount, String tip, String total, String ARQC, String AID) {
        this.trans_type = trans_type;
        this.approve = approve;
        this.card = card;
        this.card_type = card_type;
        this.date_time = date_time;
        this.amount = amount;
        this.tip = tip;
        this.total = total;
        this.ARQC = ARQC;
        this.AID = AID;
    }

    public String ticketLayout(int transaction_type, int section) {
        StringBuilder ticket = new StringBuilder();

        switch(section) {
            case 1:
                ticket.append(trans_type.toUpperCase(Locale.ROOT));
                ticket.append("\n");
                ticket.append(approve);
                ticket.append("\n\n");
                ticket.append("operadora bp sa de cv".toUpperCase(Locale.ROOT));
                ticket.append("\n");
                ticket.append("GOMEZ MORIN");
                ticket.append("\n");
                ticket.append("SAN PEDRO GARZA GARCIA,");
                ticket.append("\n");
                ticket.append("NUEVO LEON ");
                ticket.append("\n");
                ticket.append("TERMINAL");
                ticket.append("\n");
                ticket.append(WMX_Menu.ksn.posId);
                break;
            case 2:
                ticket.append("********"+card);
                break;
            case 3:
                ticket.append(card_type);
                ticket.append("\n");
                ticket.append(date_time);
                break;
            case 4:
                if(transaction_type == 1) {
                    ticket.append("Monto :                   "+amount);
                    ticket.append("\n");
                    ticket.append("Propina :                    "+tip);
                    ticket.append("\n");
                    ticket.append("Total :                      "+total);
                } else {
                    ticket.append("Total :                      "+total);
                    ticket.append("\n");
                    ticket.append(tip+"                    "+amount);
                }
                break;
            case 5:
                ticket.append("\n");
                ticket.append("ARQC :                   ************"+ARQC);
                ticket.append("\n");
                ticket.append("AID :                   "+AID);
                ticket.append("\n");
                break;
            case 6:
                ticket.append("Por este pagare me obligo");
                ticket.append("\n");
                ticket.append("incondicionalmente a pagar a la orden del");
                ticket.append("\n");
                ticket.append("banco acreditante el importe de este");
                ticket.append("\n");
                ticket.append("título. Este pagare procede del contrato");
                ticket.append("\n");
                ticket.append("de apertura de crédito que el banco");
                ticket.append("\n");
                ticket.append("acreditante y el tarjetahabiente tienen celebrado.");
                break;
        }

        return ticket.toString();
    }


    private void ticket(ActionPrinter printer, int transaction_type) throws RemoteException {
        printer.setPrintStyle(PrintStyle.Key.FONT_SIZE, 22);
        printer.addText(ticketLayout(transaction_type, 1));
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.BOLD);
        printer.addText(SEPARATOR);
        printer.addText(ticketLayout(transaction_type, 2));
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.NORMAL);
        printer.addText(ticketLayout(transaction_type, 3));
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.BOLD);
        printer.addText(SEPARATOR);
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.NORMAL);
        printer.addText("");
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.BOLD);
        printer.addText(ticketLayout(transaction_type, 4));
        printer.addText(SEPARATOR);
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.NORMAL);
        printer.addText("");
        printer.setPrintStyle(PrintStyle.Key.FONT_SIZE, 18);
        printer.addText(ticketLayout(transaction_type, 5));
        printer.addText("");
        printer.addText("");
        printer.setPrintStyle(PrintStyle.Key.FONT_SIZE, 16);
        printer.addText(ticketLayout(transaction_type, 6));
        printer.addText("");
    }

    public String getTicketString(int transaction_type) {
        StringBuilder ticket = new StringBuilder();
        ticket.append(ticketLayout(transaction_type, 1));
        ticket.append(SEPARATOR);
        ticket.append(ticketLayout(transaction_type, 2));
        ticket.append(ticketLayout(transaction_type, 3));
        ticket.append(SEPARATOR);
        ticket.append(ticketLayout(transaction_type, 4));
        ticket.append(SEPARATOR);
        ticket.append(ticketLayout(transaction_type, 5));
        ticket.append(ticketLayout(transaction_type, 6));

        return ticket.toString();
    }

    public void GenerateTicket(PRINT_TYPE type, int transaction_type) {
        ActionPrinter printer = ActionPrinter.getInstance(ctx);
        if(ctx == null || printer == null) return;
        try {
            Drawable drawable = ContextCompat.getDrawable(ctx, R.drawable.logo_ticket);
            Bitmap bitmap = Utils.drawableToBitmap(drawable);
            printer.addBitmap(bitmap, 100);
            printer.setPrintStyle(PrintStyle.Key.ALIGNMENT, PrintStyle.Alignment.CENTER);
            printer.addText("");
            if(type == PRINT_TYPE.CLIENT) {
                printer.addText("*** COPIA CLIENTE ***");
                printer.addText("");
            }
            ticket(printer, transaction_type);
            printer.lineFeed(5);
            printer.print(new IPrinterCallback.Default() {
                @Override
                public void onPrintStart() throws RemoteException {
                    super.onPrintStart();

                    //todo

                    TRACE.d("onPrintStart");

                }

                @Override
                public void onPrintFinish(int height) throws RemoteException {
                    super.onPrintFinish(height);
                    //todo
                    TRACE.d("onPrintFinish");
                }

                @Override
                public void onError(int error, String message) throws RemoteException {
                    super.onError(error, message);
                    TRACE.d("onError");
                    //todo
                }
            });
        }
        catch (RemoteException e) {
            TRACE.d("RemoteException"+ e.toString());
           e.printStackTrace();
        } catch (NullPointerException e) {
            TRACE.d("Exception"+ e.toString());
            e.printStackTrace();
        }
    }

}
