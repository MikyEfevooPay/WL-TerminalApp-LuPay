package com.efevoopay.demoui.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
            + " BIGINT NOT NULL);";

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
