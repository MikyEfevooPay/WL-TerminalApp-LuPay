package com.efevoopay.demoui.utils;

public class Transaction {
    String _pan,_date,_amount,_tips;
    int _id;
    String _auth, _date2, _subtotal,_time, _card, _redtarj,_tipotarj, _tipotxn,_propina,_total,_msi,_aid,_arqc, _approve,_emisor,_nip,_entrada;
    public Transaction(int id,String auth, String date2, String time, String subtotal ,String card,String redtarj,String tipotarj,String tipotxn,String propina,String total,String msi,String aid,String arqc, String approve,String emisor,String nip,String entrada){
        _id = id;
        _auth=auth;
        _date2=date2;
        _subtotal=subtotal;
        _time=time;
        _card=card;
        _redtarj=redtarj;
        _tipotarj=tipotarj;
        _tipotxn=tipotxn;
        _propina=propina;
        _total=total;
        _msi=msi;
        _aid=aid;
        _arqc=arqc;
        _approve=approve;
        _emisor=emisor;
        _nip=nip;
        _entrada=entrada;
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

    public String get_subtotal() {
        return _subtotal;
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

    public String get_aid(){return _aid;}

    public String get_arqc(){return _arqc;}
    public String get_emisor(){return _emisor;}

    public String get_nip(){return _nip;}

    public String get_entrada(){return _entrada;}

    public String get_approve() { return _approve; }

    public int get_id() { return _id; }
}
