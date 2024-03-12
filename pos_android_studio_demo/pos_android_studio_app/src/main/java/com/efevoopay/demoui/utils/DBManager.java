package com.efevoopay.demoui.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }
    public void onUpgrade() throws SQLException {
        dbHelper.onUpgrade(database,1,1);
    }
    public void onCreate() throws SQLException {
        dbHelper.onCreate(database);
    }
    public void onDelete() throws SQLException {
        dbHelper.onDelete(database);
    }

    public void close() {
        dbHelper.close();
    }
    public void insert(String name,String ksn,String tk,String key,String p43,String p48, String p120,String address,String comercio,String msi,Integer counter,String msi3,String msi6,String msi9,String msi12,String msi18,String minimo3,String minimo6,String minimo9,String minimo12,String minimo18,String interfaz,String codigopostal,String giro,String redlogica) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper._NAME, name);
        contentValue.put(DatabaseHelper._KSN, ksn);
        contentValue.put(DatabaseHelper._TK, tk);
        contentValue.put(DatabaseHelper._KEY, key);
        contentValue.put(DatabaseHelper._P43, p43);
        contentValue.put(DatabaseHelper._P48, p48);
        contentValue.put(DatabaseHelper._P120, p120);
        contentValue.put(DatabaseHelper._ADDRESS, address);
        contentValue.put(DatabaseHelper._COMERCIO, comercio);
        contentValue.put(DatabaseHelper._MSI, msi);
        contentValue.put(DatabaseHelper._COUNTER, counter);
        contentValue.put(DatabaseHelper._MSI3, msi3);
        contentValue.put(DatabaseHelper._MSI6, msi6);
        contentValue.put(DatabaseHelper._MSI9, msi9);
        contentValue.put(DatabaseHelper._MSI12, msi12);
        contentValue.put(DatabaseHelper._MSI18, msi18);
        contentValue.put(DatabaseHelper._minimo3, minimo3);
        contentValue.put(DatabaseHelper._minimo6, minimo6);
        contentValue.put(DatabaseHelper._minimo9, minimo9);
        contentValue.put(DatabaseHelper._minimo12, minimo12);
        contentValue.put(DatabaseHelper._minimo18, minimo18);
        contentValue.put(DatabaseHelper._interfaz, interfaz);
        contentValue.put(DatabaseHelper._codigopostal, codigopostal);
        contentValue.put(DatabaseHelper._giro, giro);
        contentValue.put(DatabaseHelper._redlogica, redlogica);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }
    public Cursor fetch(String name) {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper._NAME, DatabaseHelper._KSN,DatabaseHelper._TK,DatabaseHelper._KEY,DatabaseHelper._P43,DatabaseHelper._P48,DatabaseHelper._P120,DatabaseHelper._ADDRESS,DatabaseHelper._COMERCIO,DatabaseHelper._MSI,DatabaseHelper._COUNTER,DatabaseHelper._MSI3,DatabaseHelper._MSI6,DatabaseHelper._MSI9,DatabaseHelper._MSI12,DatabaseHelper._MSI18,DatabaseHelper._minimo3,DatabaseHelper._minimo6,DatabaseHelper._minimo9,DatabaseHelper._minimo12,DatabaseHelper._minimo18,DatabaseHelper._interfaz,DatabaseHelper._codigopostal,DatabaseHelper._giro,DatabaseHelper._redlogica };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, " name = ?", new String[] { String.valueOf(name) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public int update(String name,String ksn,String tk,String key,String p43,String p48, String p120,String address,String comercio,String msi,Integer counter,String msi3,String msi6,String msi9,String msi12,String msi18,String minimo3,String minimo6,String minimo9,String minimo12,String minimo18,String interfaz,String codigopostal,String giro,String redlogica) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper._NAME, name);
        contentValues.put(DatabaseHelper._KSN, ksn);
        contentValues.put(DatabaseHelper._TK, tk);
        contentValues.put(DatabaseHelper._KEY, key);
        contentValues.put(DatabaseHelper._P43, p43);
        contentValues.put(DatabaseHelper._P48, p48);
        contentValues.put(DatabaseHelper._P120, p120);
        contentValues.put(DatabaseHelper._ADDRESS, address);
        contentValues.put(DatabaseHelper._COMERCIO, comercio);
        contentValues.put(DatabaseHelper._MSI, msi);
        contentValues.put(DatabaseHelper._COUNTER, counter);
        contentValues.put(DatabaseHelper._MSI3, msi3);
        contentValues.put(DatabaseHelper._MSI6, msi6);
        contentValues.put(DatabaseHelper._MSI9, msi9);
        contentValues.put(DatabaseHelper._MSI12, msi12);
        contentValues.put(DatabaseHelper._MSI18, msi18);
        contentValues.put(DatabaseHelper._minimo3, minimo3);
        contentValues.put(DatabaseHelper._minimo6, minimo6);
        contentValues.put(DatabaseHelper._minimo9, minimo9);
        contentValues.put(DatabaseHelper._minimo12, minimo12);
        contentValues.put(DatabaseHelper._minimo18, minimo18);
        contentValues.put(DatabaseHelper._interfaz, interfaz);
        contentValues.put(DatabaseHelper._codigopostal, codigopostal);
        contentValues.put(DatabaseHelper._giro, giro);
        contentValues.put(DatabaseHelper._redlogica, redlogica);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, " name = ?", new String[] { String.valueOf (name ) });
        return i;
    }
    public void delete(String name) {
        database.delete(DatabaseHelper.TABLE_NAME, " name = ?", new String[] { String.valueOf (name ) });
    }
}
