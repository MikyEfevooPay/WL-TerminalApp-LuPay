package com.efevoopay.demoui.utils;

public class CorteCaja {
    String _idCorte,_Identificador,_total,_FechaHora, _subtotal, _propina;
    String _reqdukpt_id,_device,_numtxn,_monto,_date,_hora,_pan;

    public CorteCaja(){
    }

    public CorteCaja setHistoricDetails(String idCorte, String Identificador, String total, String FechaHora, String subtotal, String propina) {
        this._idCorte=idCorte;
        this._Identificador = Identificador;
        this._total = total;
        this._FechaHora = FechaHora;
        this._subtotal = subtotal;
        this._propina = propina;
        return this;
    }

    public CorteCaja(String reqdukpt_id, String device, String numtxn, String monto, String date, String hora, String pan){
        _reqdukpt_id=reqdukpt_id;
        _device = device;
        _numtxn = numtxn;
        _monto = monto;
        _date = date;
        _hora = hora;
        _pan = pan;
    }
    public String get_idCorte() {
        return _idCorte;
    }

    public String get_Identificador() {
        return _Identificador;
    }

    public String get_Total() {
        return _total;
    }

    public String get_Subtotal() {
        return _subtotal;
    }

    public String get_Propina() {
        return _propina;
    }

    public String get_FechaHora() {
        return _FechaHora;
    }
    public String get_reqdukpt_id() {
        return _reqdukpt_id;
    }
    public String get_device() {
        return _device;
    }
    public String get_numtxn() {
        return _numtxn;
    }
    public String get_monto() {
        return _monto;
    }
    public String get_date() {
        return _date;
    }
    public String get_hora() {
        return _hora;
    }
    public String get_pan() {
        return _pan;
    }
}
