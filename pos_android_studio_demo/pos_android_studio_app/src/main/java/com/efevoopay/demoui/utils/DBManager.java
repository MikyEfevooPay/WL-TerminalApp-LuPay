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
    public void insert(String name,String ksn,String tk,String key,String p43,String p48, String p120,String address,String comercio,String msi,Integer counter) {
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
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }
    public Cursor fetch(String name) {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper._NAME, DatabaseHelper._KSN,DatabaseHelper._TK,DatabaseHelper._KEY,DatabaseHelper._P43,DatabaseHelper._P48,DatabaseHelper._P120,DatabaseHelper._ADDRESS,DatabaseHelper._COMERCIO,DatabaseHelper._MSI,DatabaseHelper._COUNTER };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, " name = ?", new String[] { String.valueOf(name) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public int update(String name,String ksn,String tk,String key,String p43,String p48, String p120,String address,String comercio,String msi,Integer counter) {
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
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, " name = ?", new String[] { String.valueOf (name ) });
        return i;
    }
    public void delete(String name) {
        database.delete(DatabaseHelper.TABLE_NAME, " name = ?", new String[] { String.valueOf (name ) });
    }
}
