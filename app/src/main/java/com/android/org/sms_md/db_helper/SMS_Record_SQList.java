package com.android.org.sms_md.db_helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/10/24 0024.
 * 用于存储转发短信的数据库帮助类
 */

public class SMS_Record_SQList extends SQLiteOpenHelper {

    /*
   建立表record，number（numberOfListen（String）短信接受端，body（String），numberOfForwarding (String)短信转发端，时间）
    */
    private final String CREATE_TABLE_SQL = "create table record(numberOfListen string,body string,numberOfForwarding string,time Long)";

    /*
    当版本升级时，删除原先的表
     */
    private final String DROP_ABLE = "drop table record";

    public SMS_Record_SQList(Context context) {
        super(context,Name.DB_NAME,null, 1);
    }
//第一次调用
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        当版本更新时，删除原来的表
        if (i != i1){
            sqLiteDatabase.execSQL(DROP_ABLE);
        }
    }
}
