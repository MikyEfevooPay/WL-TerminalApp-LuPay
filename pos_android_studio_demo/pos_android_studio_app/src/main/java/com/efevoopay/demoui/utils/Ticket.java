package com.efevoopay.demoui.utils;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.RemoteException;

import androidx.core.content.ContextCompat;

import com.action.printerservice.ActionPrinter;
import com.action.printerservice.IPrinterCallback;
import com.action.printerservice.PrintStyle;
import com.efevoopay.demoui.R;

import java.util.Locale;

public class Ticket {

    public interface ICustomPrinterCallback {
        void onPrintStart();

        void onPrintFinish(int height);

        void onError(int error, String message);
    }

    Cursor cursor;
    private String trans_type, approve, card, card_type, date_time, amount, tip, total, ARQC, AID, ksn_posId;
    private boolean printeravailable;
    private ActionPrinter _printer;
    private final String SEPARATOR = "__________________________________";
    private android.content.Context ctx;
    private ICustomPrinterCallback _callback;

    public Ticket(android.content.Context ctx) {
        this.ctx = ctx;
        _printer = getPrinter();
        if(printeravailable) _printer.bind();
    }

    public void setTicketCallback(ICustomPrinterCallback callback) {
        this._callback = callback;
    }

    public void setData(String trans_type, String approve, String total, String amount, String tip, String date_time, Cursor cursor, String  ksn_posId) {
        this.trans_type = trans_type;
        this.approve = approve;
        this.date_time = date_time;
        this.amount = amount;
        this.tip = tip;
        this.total = total;
        this.ksn_posId = ksn_posId;
        this.cursor= cursor;
    }
    public void setData(String trans_type, String approve, String card, String card_type, String date_time, String amount, String tip, String total, String ARQC, String AID,String  ksn_posId,Cursor cursor) {
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
        this.ksn_posId = ksn_posId;
        this.cursor= cursor;
    }


    public  String ticketResumeLayout(int section){
        StringBuilder ticket = new StringBuilder();
        switch (section) {
            case 1:
                ticket.append("Subtotal :                   "+amount);
                ticket.append("\n");
                ticket.append("Propinas :                    "+tip);
                ticket.append("\n");
                ticket.append("MontoTotal :                      "+total);
                break;
            case 2:
                ticket.append("Fecha y Hora :      "+date_time);
                break;
        }
        return ticket.toString();
    }


    public String ticketStoreLayout(ActionPrinter printer, int transaction_type, int section) throws RemoteException {
        StringBuilder ticket = new StringBuilder();

        switch(section) {
            case 1:
                String[] _list = address(cursor.getString(8));
                TRACE.d("length"+_list.length);
                ticket.append(trans_type.toUpperCase(Locale.ROOT));
                ticket.append("\n");
                if(card_type != null) ticket.append(tildetarjeta(approve));
                ticket.append("\n\n");
                ticket.append(Utils.isNull(cursor.getString(9), "N/A").toUpperCase(Locale.ROOT));
                ticket.append("\n");
                ticket.append(Utils.isVacio(_list,0)+" "+Utils.isVacio(_list,1)+" "+Utils.isVacio(_list,2));
                ticket.append("\n");
                ticket.append(Utils.isVacio(_list,3));
                ticket.append("\n");
                ticket.append(Utils.isVacio(_list,4));
                ticket.append("\n");
                ticket.append(Utils.isVacio(_list,5));
                ticket.append("\n");
                if(transaction_type == 0) {
                    ticket.append("NO. TERMINAL");
                }else{
                    ticket.append("TERMINAL");
                }
                ticket.append("\n");
                ticket.append(ksn_posId);
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
                ticket.append("ARQC :             "+ARQC);
                ticket.append("\n");
                ticket.append("AID :              "+AID);
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


    private void ticket_store(ActionPrinter printer, int transaction_type) throws RemoteException {
        printer.setPrintStyle(PrintStyle.Key.FONT_SIZE, 22);
        printer.addText(ticketStoreLayout(printer, transaction_type, 1));
        printer.lineFeed(1);
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.BOLD);
        printer.addText(SEPARATOR);
        printer.lineFeed(1);
        printer.addText(ticketStoreLayout(printer,transaction_type, 2));
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.NORMAL);
        printer.addText(ticketStoreLayout(printer,transaction_type, 3));
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.BOLD);
        printer.lineFeed(1);
        printer.addText(SEPARATOR);
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.NORMAL);
        printer.lineFeed(1);
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.BOLD);
        printer.addText(ticketStoreLayout(printer,transaction_type, 4));
        printer.lineFeed(1);
        printer.addText(SEPARATOR);
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.NORMAL);
        printer.lineFeed(1);
        printer.setPrintStyle(PrintStyle.Key.FONT_SIZE, 18);
        printer.addText(ticketStoreLayout(printer,transaction_type, 5));
        printer.lineFeed(1);
        printer.setPrintStyle(PrintStyle.Key.FONT_SIZE, 16);
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.BOLD);
        printer.addText(ticketStoreLayout(printer,transaction_type, 6));
        printer.lineFeed(5);
    }

    private void ticket_resume(ActionPrinter printer) throws RemoteException {
        printer.setPrintStyle(PrintStyle.Key.FONT_SIZE, 22);
        printer.addText(ticketStoreLayout(printer,0, 1));
        printer.lineFeed(1);
        printer.addText("Resumen Corte de Caja");
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.BOLD);
        printer.addText(SEPARATOR);
        printer.lineFeed(1);
        printer.addText(ticketResumeLayout(1));
        printer.lineFeed(1);
        printer.addText(SEPARATOR);
        printer.lineFeed(1);
        printer.addText(ticketResumeLayout(2));
        printer.lineFeed(1);
        printer.addText(SEPARATOR);
        printer.lineFeed(1);
        printer.addText("Fin del resumen");
        printer.lineFeed(5);
    }


    public boolean isPrinterAvailable() { return printeravailable && Build.MODEL.equals("D30"); }

    private ActionPrinter getPrinter() {
        try {
            ActionPrinter printer = ActionPrinter.getInstance(ctx);
            printeravailable = printer != null;
            return printer;
        } catch(NullPointerException e) {
            e.printStackTrace();
            printeravailable = false;
            return null;
        }
    }

    private Bitmap getImageLogo() {
        Drawable drawable = ContextCompat.getDrawable(ctx, R.drawable.logo_ticket);
        return Utils.drawableToBitmap(drawable);
    }


    public void GenerateTicket(PRINT_TYPE type, int ...transaction_type) {
        if(ctx == null || _printer == null) return;
        try {
            _printer.addBitmap(getImageLogo(), 100);
            _printer.setPrintStyle(PrintStyle.Key.ALIGNMENT, PrintStyle.Alignment.CENTER);
            _printer.lineFeed(2);
            _printer.setParameter(1, 2);
            if(type == PRINT_TYPE.CLIENT || type == PRINT_TYPE.STORE) {
                if(type == PRINT_TYPE.CLIENT) {
                    _printer.addText("*** COPIA CLIENTE ***");
                    _printer.addText("");
                }
                ticket_store(_printer, transaction_type[0]);
            } else {
                ticket_resume(_printer);
            }
            _printer.print(new IPrinterCallback.Default() {
                @Override
                public void onPrintStart() throws RemoteException {
                    TRACE.d("TICKET START");
                    super.onPrintStart();
                }

                @Override
                public void onPrintFinish(int height) throws RemoteException {
                    super.onPrintFinish(height);
                    TRACE.d(" TICKET END");
                    if(_callback != null) _callback.onPrintFinish(height);
                }

                @Override
                public void onError(int error, String message) throws RemoteException {
                    super.onError(error, message);
                    if(_callback != null) _callback.onError(error, message);
                }
            });
        }
        catch (RemoteException e) {
            TRACE.d("RemoteException"+ e.toString());
            if(_callback != null) _callback.onError(0, "");
           e.printStackTrace();
        } catch (NullPointerException e) {
            TRACE.d("Exception"+ e.toString());
            if(_callback != null) _callback.onError(0, "");
            e.printStackTrace();
        }
    }
    public String tildetarjeta(String _text){
        if(_text.contains("dito")){
            return "Crédito";
        }else{
            return "Débito";
        }
    }
    public String[] address(String _address){
        String[] _list = _address.split(", ");
        return _list;
    }

}
