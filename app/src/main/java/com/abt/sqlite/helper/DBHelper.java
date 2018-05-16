package com.abt.sqlite.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @描述： @DBHelper
 * @作者： @黄卫旗
 * @创建时间： @2018/5/16
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mydb.db"; // 数据库名称
    private static final int version = 1; // 数据库版本

    public DBHelper(Context context) {
        super(context,DB_NAME,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table user(username varchar(20) not null , password varchar(60) not null );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ;
    }
}
