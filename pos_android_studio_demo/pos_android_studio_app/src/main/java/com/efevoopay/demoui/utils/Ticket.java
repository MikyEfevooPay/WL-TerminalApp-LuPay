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


    private void ticket(ActionPrinter printer, int transaction_type) throws RemoteException {
        StringBuilder section_1 = new StringBuilder();
        StringBuilder section_2 = new StringBuilder();
        StringBuilder section_3 = new StringBuilder();
        StringBuilder section_4 = new StringBuilder();
        StringBuilder section_5 = new StringBuilder();
        printer.setPrintStyle(PrintStyle.Key.FONT_SIZE, 22);
        section_1.append(trans_type.toUpperCase(Locale.ROOT));
        section_1.append("\n");
        section_1.append(approve);
        section_1.append("\n\n");
        section_1.append("operadora bp sa de cv".toUpperCase(Locale.ROOT));
        section_1.append("\n");
        section_1.append("GOMEZ MORIN");
        section_1.append("\n");
        section_1.append("SAN PEDRO GARZA GARCIA,");
        section_1.append("\n");
        section_1.append("NUEVO LEON ");
        section_1.append("\n");
        section_1.append("TERMINAL");
        section_1.append("\n");
        section_1.append(WMX_Menu.ksn.posId);
        printer.addText(section_1.toString());
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.BOLD);
        printer.addText("__________________________________");
        printer.addText("********"+card);
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.NORMAL);
        section_2.append(card_type);
        section_2.append("\n");
        section_2.append(date_time);
        printer.addText(section_2.toString());
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.BOLD);
        printer.addText("__________________________________");
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.NORMAL);
        printer.addText("");
        if(transaction_type == 1) {
            section_3.append("Monto :                   "+amount);
            section_3.append("\n");
            section_3.append("Propina :                    "+tip);
            section_3.append("\n");
            section_3.append("Total :                      "+total);
        } else {
            section_3.append("Total :                      "+total);
            section_3.append("\n");
            section_3.append(tip+"                    "+amount);
        }
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.BOLD);
        printer.addText(section_3.toString());
        printer.addText("__________________________________");
        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.NORMAL);
        printer.addText("");
        section_4.append("\n");
        section_4.append("ARQC :                   ************"+ARQC);
        section_4.append("\n");
        section_4.append("AID :                   "+AID);
        printer.setPrintStyle(PrintStyle.Key.FONT_SIZE, 18);
        printer.addText(section_4.toString());
        printer.addText("");
        printer.addText("");
        section_5.append("Por este pagare me obligo");
        section_5.append("\n");
        section_5.append("incondicionalmente a pagar a la orden del");
        section_5.append("\n");
        section_5.append("banco acreditante el importe de este");
        section_5.append("\n");
        section_5.append("título. Este pagare procede del contrato");
        section_5.append("\n");
        section_5.append("de apertura de crédito que el banco");
        section_5.append("\n");
        section_5.append("acreditante y el tarjetahabiente tienen celebrado.");

        printer.setPrintStyle(PrintStyle.Key.FONT_SIZE, 16);
        printer.addText(section_5.toString());
        printer.addText("");
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
