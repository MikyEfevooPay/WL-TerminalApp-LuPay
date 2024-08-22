package com.cohetepay.demoui.interfaces;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;

import com.cohetepay.demoui.utils.PRINT_TYPE;
import com.cohetepay.demoui.utils.Ticket;

public interface ITicket {
    void onPrintFinished(boolean isSuccess, PRINT_TYPE print_type, TicketLayoutType layoutType);

    void onPrintError(boolean isSuccess, String status, PRINT_TYPE print_type, TicketLayoutType layoutType);

    TicketLayoutType getPrintLayout();

    void setTicketData(Ticket ticket);
}
