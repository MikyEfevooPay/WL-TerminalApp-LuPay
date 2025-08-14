package com.lupay.demoui.utils;

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
    public void insert(String name,String ksn,String tk,String key,String p43,String p48, String p120,String address,String comercio,String msi,Integer counter,String msi3,String msi6,String msi9,String msi12,String msi18,String minimo3,String minimo6,String minimo9,String minimo12,String minimo18,String interfaz,String codigopostal,String giro,String redlogica,String afiliacion,String statusseller,String datafield43,String datafield60,String tkamex,String keyamex,Integer countamex,String emailaddress, String phonenumber) {
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
        contentValue.put(DatabaseHelper._afiliacion,afiliacion);
        contentValue.put(DatabaseHelper._statusseller,statusseller);
        contentValue.put(DatabaseHelper._datafield43,datafield43);
        contentValue.put(DatabaseHelper._datafield60,datafield60);
        contentValue.put(DatabaseHelper._tkamex,tkamex);
        contentValue.put(DatabaseHelper._keyamex,keyamex);
        contentValue.put(DatabaseHelper._countamex,countamex);
        contentValue.put(DatabaseHelper._emailaddress,emailaddress);
        contentValue.put(DatabaseHelper._phonenumber,phonenumber);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }
    public Cursor fetch(String name) {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper._NAME, DatabaseHelper._KSN,DatabaseHelper._TK,DatabaseHelper._KEY,DatabaseHelper._P43,DatabaseHelper._P48,DatabaseHelper._P120,DatabaseHelper._ADDRESS,DatabaseHelper._COMERCIO,DatabaseHelper._MSI,DatabaseHelper._COUNTER,DatabaseHelper._MSI3,DatabaseHelper._MSI6,DatabaseHelper._MSI9,DatabaseHelper._MSI12,DatabaseHelper._MSI18,DatabaseHelper._minimo3,DatabaseHelper._minimo6,DatabaseHelper._minimo9,DatabaseHelper._minimo12,DatabaseHelper._minimo18,DatabaseHelper._interfaz,DatabaseHelper._codigopostal,DatabaseHelper._giro,DatabaseHelper._redlogica,DatabaseHelper._afiliacion,DatabaseHelper._statusseller,DatabaseHelper._datafield43,DatabaseHelper._datafield60,DatabaseHelper._tkamex,DatabaseHelper._keyamex,DatabaseHelper._countamex,DatabaseHelper._emailaddress,DatabaseHelper._phonenumber };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, " name = ?", new String[] { String.valueOf(name) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public int update(String name,String statusseller,String tkamex,String keyamex,Integer countamex,String datafield43,String datafield60) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper._statusseller, statusseller);
        contentValues.put(DatabaseHelper._tkamex, tkamex);
        contentValues.put(DatabaseHelper._keyamex, keyamex);
        contentValues.put(DatabaseHelper._countamex,countamex);
        contentValues.put(DatabaseHelper._datafield43,datafield43);
        contentValues.put(DatabaseHelper._datafield60,datafield60);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, " name = ?", new String[] { String.valueOf (name ) });
        return i;
    }
    public void delete(String name) {
        database.delete(DatabaseHelper.TABLE_NAME, " name = ?", new String[] { String.valueOf (name ) });
    }
}
