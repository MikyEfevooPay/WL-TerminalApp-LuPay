package com.efevoopay.demoui.utils;

public class Transaction {
    String _pan,_date,_amount,_tips;
    String _auth, _date2, _amount2,_time, _card, _redtarj,_tipotarj, _tipotxn,_propina,_total,_msi, _approve;
    public Transaction(String pan, String date, String amount, String tips){
        _pan=pan;
        _date = date;
        _amount = amount;
        _tips = tips;
    }
    public Transaction(String auth, String date2, String time, String amount2 ,String card,String redtarj,String tipotarj,String tipotxn,String propina,String total,String msi, String approve){
        _auth=auth;
        _date2=date2;
        _amount2=amount2;
        _time=time;
        _card=card;
        _redtarj=redtarj;
        _tipotarj=tipotarj;
        _tipotxn=tipotxn;
        _propina=propina;
        _total=total;
        _msi=msi;
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

    public String get_redtarj() {
        return _redtarj;
    }
    public String get_tipotarj() {
        return _tipotarj;
    }

    public String get_tipotxn() {
        return _tipotxn;
    }
    public String get_propina() {
        return _propina;
    }
    public String get_total(){return _total;}
    public String get_msi(){return _msi;}

    public String get_approve() { return _approve; }
}
