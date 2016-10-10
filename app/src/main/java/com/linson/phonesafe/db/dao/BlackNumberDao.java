package com.linson.phonesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.linson.phonesafe.db.BlackNumberOpenHelper;
import com.linson.phonesafe.db.domain.BlackNumberInfo;

import java.util.ArrayList;

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
}
