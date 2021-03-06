package com.linson.phonesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.linson.phonesafe.db.BlackNumberOpenHelper;
import com.linson.phonesafe.db.domain.BlackNumberInfo;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2016/10/10.
 */

public class BlackNumberDao {

    private static BlackNumberDao blackNumberDao;
    private BlackNumberOpenHelper helper;
    private String table = "blacknumber";


    private BlackNumberDao(Context context) {
        helper = new BlackNumberOpenHelper(context);
    }

    public static BlackNumberDao getInstance(Context context) {
        if (blackNumberDao == null) {
            blackNumberDao = new BlackNumberDao(context);
        }
        return blackNumberDao;
    }

    public void insert(String phone, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", phone);
        values.put("mode", mode);
        db.insert(table, null, values);
        db.close();
    }

    public void delete(String phone) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(table, "phone = ?", new String[]{phone});
        db.close();
    }

    public void update(String phone, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", phone);
        values.put("mode", mode);
        db.update(table, values, "phone = ?", new String[]{phone});
        db.close();
    }

    public ArrayList<BlackNumberInfo> findAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(table, null, null, null, null, null, "_id desc");
        ArrayList<BlackNumberInfo> arrayList = new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()) {
            BlackNumberInfo info = new BlackNumberInfo();
            info.phone = cursor.getString(cursor.getColumnIndex("phone"));
            info.mode = cursor.getString(cursor.getColumnIndex("mode"));
            info.id = cursor.getInt(cursor.getColumnIndex("_id"));
            arrayList.add(info);
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<BlackNumberInfo> find(int index) {
        ArrayList<BlackNumberInfo> arrayList = new ArrayList<BlackNumberInfo>();
        String sql = "select * from " + table + " order by _id desc limit ? ,10";
        Log.i(TAG, "find: " + sql);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{index + ""});
        while (cursor.moveToNext()) {
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String mode = cursor.getString(cursor.getColumnIndex("mode"));
            BlackNumberInfo info = new BlackNumberInfo();
            info.phone = phone;
            info.mode = mode;
            arrayList.add(info);
        }
        cursor.close();
        db.close();
        Log.i(TAG, "find: " + arrayList.size());
        return arrayList;
    }

    public int getCount() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select count(*) from " + table;
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public BlackNumberInfo findByPhone(String originatingAddress) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from " + table + " where phone = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{originatingAddress});
        BlackNumberInfo info = null;
        if (cursor.moveToNext()) {
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String mode = cursor.getString(cursor.getColumnIndex("mode"));
            info = new BlackNumberInfo();
            info.phone = phone;
            info.mode = mode;
        }
        cursor.close();
        db.close();
        return info;
    }
}
