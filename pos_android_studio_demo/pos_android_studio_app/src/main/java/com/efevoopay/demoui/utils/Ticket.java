package com.efevoopay.demoui.utils;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;

import com.action.printerservice.ActionPrinter;
import com.dspread.print.device.PrintListener;
import com.dspread.print.device.PrinterDevice;
import com.dspread.print.device.PrinterManager;
import com.dspread.xpos.Util;

import java.util.Locale;

public class Ticket {

    Cursor cursor;
    private int trans_id;
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
            msi,// MSI
            card_emisor, //Emisor de banco
            card_nip, //NIP tarjeta
            card_entrada, //Entrda tarjeta
            card_singtype; //Leyenda firma
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

    public int getTrans_id() { return this.trans_id; }
    public String getTrans_type() {
        return Utils.isNull(this.trans_type, "").toUpperCase(Locale.ROOT);
    }

    public String getApprove() {
        return Utils.isNull(this.approve, "");
    }

    public String getCard() {
        return Utils.isNull(this.card, "");
    }

    public String getCard_type() {
        return Utils.isNull(this.card_type, "");
    }

    public String getDate_time() {
        return Utils.isNull(this.date_time, "");
    }

    public String getAmount() {
        return Utils.isNull(this.amount, "");
    }

    public String getTip() {
        return Utils.isNull(this.tip, "");
    }

    public String getTotal() {
        return Utils.isNull(this.total, "");
    }

    public String getARQC() {
        return Utils.isNull(this.ARQC, "");
    }

    public String getAID() {
        return Utils.isNull(this.AID, "");
    }

    public String getKsn_posId() {
        return Utils.isNull(this.ksn_posId, "");
    }

    public String getCard_provider() {
        return Utils.isNull(this.card_provider, "");
    }

    public String getStatus() {
        return Utils.isNull(this.status, "");
    }

    public Cursor getCursor() {
        return Utils.isNull(this.cursor, Ticket.createDefaultCursor());
    }

    public String getMsi() {
        return Utils.isNull(this.msi, "");
    }

    public String getCard_emisor() {
        return Utils.isNull(this.card_emisor, "");
    }

    public String getCard_nip() {return Utils.isNull(this.card_nip, ""); }

    public String getCard_entrada() {return Utils.isNull(this.card_entrada, ""); }

    public String getCard_singtype() {return Utils.isNull(this.card_singtype, ""); }

    // Setters
    public Ticket setTransId(int trans_id) {
        this.trans_id = trans_id;
        return this;
    }
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
    public Ticket setCard_emisor(String card_emisor) {
        this.card_emisor = card_emisor;
        return this;
    }
    public Ticket setCard_nip(String card_nip) {
        this.card_nip = card_nip;
        return this;
    }
    public Ticket setCard_entrada(String card_entrada) {
        this.card_entrada = card_entrada;
        return this;
    }
    public Ticket setCard_singtype(String card_singtype) {
        this.card_singtype = card_singtype;
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

    private static Cursor createDefaultCursor() {
        return new Cursor() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public boolean move(int offset) {
                return false;
            }

            @Override
            public boolean moveToPosition(int position) {
                return false;
            }

            @Override
            public boolean moveToFirst() {
                return false;
            }

            @Override
            public boolean moveToLast() {
                return false;
            }

            @Override
            public boolean moveToNext() {
                return false;
            }

            @Override
            public boolean moveToPrevious() {
                return false;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean isBeforeFirst() {
                return false;
            }

            @Override
            public boolean isAfterLast() {
                return false;
            }

            @Override
            public int getColumnIndex(String columnName) {
                return 0;
            }

            @Override
            public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
                return 0;
            }

            @Override
            public String getColumnName(int columnIndex) {
                return null;
            }

            @Override
            public String[] getColumnNames() {
                return new String[0];
            }

            @Override
            public int getColumnCount() {
                return 0;
            }

            @Override
            public byte[] getBlob(int columnIndex) {
                return new byte[0];
            }

            @Override
            public String getString(int columnIndex) {
                return null;
            }

            @Override
            public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {

            }

            @Override
            public short getShort(int columnIndex) {
                return 0;
            }

            @Override
            public int getInt(int columnIndex) {
                return 0;
            }

            @Override
            public long getLong(int columnIndex) {
                return 0;
            }

            @Override
            public float getFloat(int columnIndex) {
                return 0;
            }

            @Override
            public double getDouble(int columnIndex) {
                return 0;
            }

            @Override
            public int getType(int columnIndex) {
                return 0;
            }

            @Override
            public boolean isNull(int columnIndex) {
                return false;
            }

            @Override
            public void deactivate() {

            }

            @Override
            public boolean requery() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public void registerContentObserver(ContentObserver observer) {

            }

            @Override
            public void unregisterContentObserver(ContentObserver observer) {

            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void setNotificationUri(ContentResolver cr, Uri uri) {

            }

            @Override
            public Uri getNotificationUri() {
                return null;
            }

            @Override
            public boolean getWantsAllOnMoveCalls() {
                return false;
            }

            @Override
            public void setExtras(Bundle extras) {

            }

            @Override
            public Bundle getExtras() {
                return null;
            }

            @Override
            public Bundle respond(Bundle extras) {
                return null;
            }
        };
    }

}
