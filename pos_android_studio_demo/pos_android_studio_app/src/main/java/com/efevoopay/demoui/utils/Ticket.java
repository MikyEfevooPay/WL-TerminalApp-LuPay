package com.efevoopay.demoui.utils;

import android.database.Cursor;
import android.os.Build;
import android.os.RemoteException;
import android.view.View;

import com.action.printerservice.ActionPrinter;
import com.dspread.print.device.PrintListener;
import com.dspread.print.device.PrinterDevice;
import com.dspread.print.device.PrinterManager;

import java.util.Locale;

public class Ticket {

    Cursor cursor;
    private String trans_type, // Tipo de transaccion - Venta, Cancelacion, MSI, Corte
            approve, // Numero de referencia
            status, // Aprobada, cancelada
            card, // Numero de tarjeta
            card_type, // Credito - Credito
            card_provider, // Visa - Matercard
            date_time, // Fecha de transaccion
            amount, // subtotal
            tip, // Propina
            total, // Total (subtotal + propina)
            ARQC, // ARQC
            AID, // AID
            ksn_posId, // Numero de serie
            msi; // MSI
    private boolean printeravailable;
    private android.content.Context ctx;
    private PrinterDevice mPrinter;

    public Ticket(android.content.Context ctx) {
        this.ctx = ctx;
        try {
            PrinterManager instance = PrinterManager.getInstance();
            mPrinter = instance.getPrinter();
            mPrinter.initPrinter(ctx);
            printeravailable = mPrinter != null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            printeravailable = false;
        }
    }

    // Getters
    public String getTrans_type() {
        return this.trans_type.toUpperCase(Locale.ROOT);
    }

    public String getApprove() {
        return this.approve;
    }

    public String getCard() {
        return this.card;
    }

    public String getCard_type() {
        return this.card_type;
    }

    public String getDate_time() {
        return this.date_time;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getTip() {
        return this.tip;
    }

    public String getTotal() {
        return this.total;
    }

    public String getARQC() {
        return this.ARQC;
    }

    public String getAID() {
        return this.AID;
    }

    public String getKsn_posId() {
        return this.ksn_posId;
    }

    public String getCard_provider() {
        return card_provider;
    }

    public String getStatus() {
        return status;
    }

    public Cursor getCursor() {
        return this.cursor;
    }

    public String getMsi() {
        return this.msi;
    }

    // Setters
    public Ticket setTrans_Type(String trans_type) {
        this.trans_type = trans_type;
        return this;
    }

    public Ticket setApprove(String approve) {
        this.approve = approve;
        return this;
    }

    public Ticket setCard(String card) {
        this.card = card;
        return this;
    }

    public Ticket setCardType(String card_type) {
        this.card_type = card_type;
        return this;
    }

    public Ticket setDate_Time(String date_time) {
        this.date_time = date_time;
        return this;
    }

    public Ticket setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public Ticket setTip(String tip) {
        this.tip = tip;
        return this;
    }

    public Ticket setTotal(String total) {
        this.total = total;
        return this;
    }

    public Ticket setARQC(String ARQC) {
        this.ARQC = ARQC;
        return this;
    }

    public Ticket setAID(String AID) {
        this.AID = AID;
        return this;
    }

    public Ticket setKsn_posId(String ksn_posId) {
        this.ksn_posId = ksn_posId;
        return this;
    }

    public Ticket setCard_provider(String card_provider) {
        this.card_provider = card_provider;
        return this;
    }

    public Ticket setStatus(String status) {
        this.status = status;
        return this;
    }

    public Ticket setCursor(Cursor cursor) {
        this.cursor = cursor;
        return this;
    }

    public Ticket setMsi(String msi) {
        this.msi = msi;
        return this;
    }

    public void setPrintListenner(PrintListener _printListener) {
        mPrinter.setPrintListener(_printListener);
    }

    public boolean printLayout(View Layout) {
        try {
            if (Layout == null)
                throw new RemoteException("No hay layout disponible");
            mPrinter.setPrinterGrey(110);
            mPrinter.printBitmap(this.ctx, Utils.viewToBitmap(Layout));
            return true;
        } catch (RemoteException e) {
            TRACE.d("PRINT ERROR:" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        if (mPrinter == null) return;
            try {
                mPrinter.stopPrint();
                mPrinter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public boolean isPrinterAvailable() {
        return printeravailable && Build.MODEL.equals("D30");
    }

}
