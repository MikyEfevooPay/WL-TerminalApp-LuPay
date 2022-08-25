package com.efevoopay.demoui.utils;

public class Transaction {
    String _pan,_date,_amount,_tips;
    String _auth, _date2, _amount2,_time, _card, _process, _status, _approve;
    public Transaction(String pan, String date, String amount, String tips){
        _pan=pan;
        _date = date;
        _amount = amount;
        _tips = tips;
    }
    public Transaction(String auth, String date2, String time, String amount2 ,String card,String process,String status, String approve){
        _auth=auth;
        _date2=date2;
        _amount2=amount2;
        _time=time;
        _card=card;
        _process=process;
        _status=status;
        _approve=approve;
    }

    public String get_amount() {
        return _amount;
    }

    public String get_pan() {
        return _pan;
    }

    public String get_date() {
        return _date;
    }

    public String get_tips() {
        return _tips;
    }

    public String get_auth() {
        return _auth;
    }

    public String get_date2() {
        return _date2;
    }

    public String get_amount2() {
        return _amount2;
    }

    public String get_time() {
        return _time;
    }

    public String get_card() {
        return _card;
    }

    public String get_process() {
        return _process;
    }

    public String get_status() {
        return _status;
    }

    public String get_approve() { return _approve; }
}
