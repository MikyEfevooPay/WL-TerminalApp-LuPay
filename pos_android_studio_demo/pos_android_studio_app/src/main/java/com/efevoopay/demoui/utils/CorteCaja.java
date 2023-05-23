package com.efevoopay.demoui.utils;

public class CorteCaja {
    String _idCorte,_Identificador,_Cantidad,_FechaHora;
    String _reqdukpt_id,_device,_numtxn,_monto,_date,_hora,_pan;

    public CorteCaja(String idCorte, String Identificador, String Cantidad, String FechaHora){
        _idCorte=idCorte;
        _Identificador = Identificador;
        _Cantidad = Cantidad;
        _FechaHora = FechaHora;
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

    public String get_Cantidad() {
        return _Cantidad;
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
