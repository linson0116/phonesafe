package com.linson.phonesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/10/10.
 */

public class BlackNumberOpenHelper extends SQLiteOpenHelper {
    static String name = "blacknumber.db";
    static int version = 1;

    public BlackNumberOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table blacknumber(_id integer primary key autoincrement,phone varchar(20),mode varchar(10))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
