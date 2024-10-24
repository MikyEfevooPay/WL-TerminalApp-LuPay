package com.lupay.demoui.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteTpv extends SQLiteOpenHelper {
    private static final String NOMBRE_BASE_DE_DATOS = "tpv", NOMBRE_TABLA_DEVICE = "device";
    private static final int VERSION_BASE_DE_DATOS = 1;
    public static final String COLUMNA_ID = "device_id";
    public static final String COLUMNA_name = "device_name";
    public static final String COLUMNA_ksn = "device_ksn";
    public static final String COLUMNA_tk = "device_tk";
    public static final String COLUMNA_key = "device_key";
    public static final String COLUMNA_p43 = "device_p43";
    public static final String COLUMNA_p48 = "device_p48";
    public static final String COLUMNA_p120 = "device_p120";
    public static final String COLUMNA_address = "device_address";
    public static final String COLUMNA_comercio="device_comercio";
    public static final String COLUMNA_msi="device_msi";
    private static final String SQL_CREAR = "CREATE TABLE IF NOT EXISTS "
            + NOMBRE_TABLA_DEVICE + "(" + COLUMNA_ID
            + " integer primary key autoincrement, " + COLUMNA_name
            + " text not null,"+ COLUMNA_ksn
            + " text not null,"+ COLUMNA_tk
            + " text not null,"+ COLUMNA_key
            + " text not null,"+ COLUMNA_p43
            + " text not null,"+ COLUMNA_p48
            + " text not null,"+ COLUMNA_p120
            + " text not null,"+ COLUMNA_address
            + " text not null,"+ COLUMNA_comercio
            + " text not null,"+ COLUMNA_msi
            + " text not null);";

    public SQLiteTpv(Context context) {
        super(context, NOMBRE_BASE_DE_DATOS, null, VERSION_BASE_DE_DATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //db.execSQL("DROP DATABASE IF EXISTS "+NOMBRE_BASE_DE_DATOS);
        //db.execSQL("DROP TABLE IF EXISTS "+NOMBRE_TABLA_DEVICE);
        db.execSQL(SQL_CREAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void TpvInsert(String _name,String _ksn,String _tk,String _key,String _p43,String _p48, String _p120,String _address,String _comercio,String msi){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMNA_name, _name);
        values.put(COLUMNA_ksn, _ksn);
        values.put(COLUMNA_tk, _tk);
        values.put(COLUMNA_key, _key);
        values.put(COLUMNA_p43, _p43);
        values.put(COLUMNA_p48, _p48);
        values.put(COLUMNA_p120, _p120);
        values.put(COLUMNA_address, _address);
        values.put(COLUMNA_comercio, _comercio);
        values.put(COLUMNA_msi, msi);

        db.insert(NOMBRE_TABLA_DEVICE, null,values);
        db.close();
    }
    public Cursor TpvConsult(String _posId){

        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMNA_ID,COLUMNA_name, COLUMNA_ksn,COLUMNA_tk,COLUMNA_key,COLUMNA_p43,COLUMNA_p48,COLUMNA_p120,COLUMNA_address,COLUMNA_comercio,COLUMNA_msi};

        Cursor cursor =
                db.query(NOMBRE_TABLA_DEVICE,
                        projection,
                        " device_name = ?",
                        new String[] { String.valueOf(_posId) },
                        null,
                        null,
                        null,
                        null);


        if (cursor != null)
            cursor.moveToFirst();

        //System.out.println("El nombre es " +  cursor.getString(1) );
        db.close();
        return cursor;
    }
    public boolean TpvDelete(String _posId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.delete(NOMBRE_TABLA_DEVICE,
                    " device_name = ?",
                    new String[] { String.valueOf (_posId ) });
            db.close();
            return true;

        }catch(Exception ex){
            return false;
        }
    }

}
