package com.efevoopay.demoui.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.PublicKey;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Table Name
    public static final String TABLE_NAME = "DEVICE";

    // Table columns
    public static final String _ID = "id";
    public static final String _NAME = "name";
    public static final String _KSN = "ksn";
    public static final String _TK = "tk";
    public static final String _KEY = "keys";
    public static final String _P43 = "p43";
    public static final String _P48 = "p48";
    public static final String _P120 = "p120";
    public static final String _ADDRESS = "address";
    public static final String _COMERCIO="comercio";
    public static final String _MSI="msi";
    public static final String _COUNTER="counter";
    public static final String _MSI3="msi3";
    public static final String _MSI6="msi6";
    public static final String _MSI9="msi9";
    public static final String _MSI12="msi12";
    public static final String _MSI18="msi18";
    public static final String _minimo3="minimo3";
    public static final String _minimo6="minimo6";
    public static final String _minimo9="minimo9";
    public static final String _minimo12="minimo12";
    public static final String _minimo18="minimo18";
    public static final String _interfaz="interfaz";
    public static final String _codigopostal="codigopostal";
    public static final String _giro="giro";
    public static final String _redlogica="redlogica";
    public static final String _afiliacion="afiliacion";
    public static final String _statusseller="statusseller";
    public static final String _datafield43="datafield43";
    public static final String _datafield60="datafield60";
    public static final String _tkamex="tkamex";
    public static final String _keyamex="keyamex";
    public static final String _countamex="countamex";

    // Database Information
    static final String DB_NAME = "TPV";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + _NAME
            + " TEXT NOT NULL,"+ _KSN
            + " TEXT NOT NULL,"+ _TK
            + " TEXT NOT NULL,"+ _KEY
            + " TEXT NOT NULL,"+ _P43
            + " TEXT NOT NULL,"+ _P48
            + " TEXT NOT NULL,"+ _P120
            + " TEXT NOT NULL,"+ _ADDRESS
            + " TEXT NOT NULL,"+ _COMERCIO
            + " TEXT NOT NULL,"+ _MSI
            + " TEXT NOT NULL,"+_COUNTER
            + " BIGINT NOT NULL,"+_MSI3
            + " TEXT NOT NULL,"+_MSI6
            + " TEXT NOT NULL,"+_MSI9
            + " TEXT NOT NULL,"+_MSI12
            + " TEXT NOT NULL,"+_MSI18
            + " TEXT NOT NULL," +_minimo3
            + " TEXT NOT NULL,"+_minimo6
            + " TEXT NOT NULL,"+_minimo9
            + " TEXT NOT NULL,"+_minimo12
            + " TEXT NOT NULL,"+_minimo18
            + " TEXT NOT NULL,"+_interfaz
            + " TEXT NOT NULL,"+_codigopostal
            + " TEXT NOT NULL,"+_giro
            + " TEXT NOT NULL,"+_redlogica
            + " TEXT NOT NULL,"+_afiliacion
            + " TEXT NOT NULL,"+_statusseller
            + " TEXT NOT NULL,"+_datafield43
            + " TEXT NOT NULL,"+_datafield60
            + " TEXT NOT NULL,"+_tkamex
            + " TEXT NOT NULL,"+_keyamex
            + " TEXT NOT NULL,"+_countamex
            + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void onDelete(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
