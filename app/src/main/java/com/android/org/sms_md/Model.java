package com.android.org.sms_md;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.org.sms_md.db_helper.Name;
import com.android.org.sms_md.db_helper.SMS_Record_SQList;
import com.android.org.sms_md.javabean.javabean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/27 0027.
 * 数据层的处理，主要处理数据存取
 */

public class Model implements iModel {

    private Context context;
    private iPresenter iPresenter;

    public Model(Context context,iPresenter iPresenter) {
        this.context = context;
        this.iPresenter =iPresenter;
    }

    @Override
    public boolean getConfiguration_Boolean(String string) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name.CONFI, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(string,false);
    }

    @Override
    public void setConfiguration_Boolean(String string,boolean bool) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name.CONFI,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(string,bool);
        editor.apply();
    }

    @Override
    public String getConfiguration_String(String string) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name.SERVICE_CONFI, Context.MODE_PRIVATE);
        return sharedPreferences.getString(string,null);
    }

    @Override
    public void set_service_configuration(String Listen, boolean ListenFirm, String Forwarding, boolean ForwardingFirm, long StartTime, boolean AllListen) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name.SERVICE_CONFI,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Name.Listen,Listen);
        editor.putBoolean(Name.ListenFirm,ListenFirm);
        editor.putString(Name.Forwarding,Forwarding);
        editor.putBoolean(Name.ForwardingFirm,ForwardingFirm);
        editor.putLong(Name.StartTime,StartTime);
        editor.putBoolean(Name.AllListen,AllListen);
        editor.apply();
    }

    @Override
    public List<javabean> getAllData() {
        List<javabean> list = new ArrayList<>();
        SMS_Record_SQList sms_record_sqList = new SMS_Record_SQList(context);
        SQLiteDatabase sqLiteDatabase = sms_record_sqList.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from record",null);
        while (cursor.moveToNext()){
            javabean javabean = new javabean();
            javabean.setListen(cursor.getString(cursor.getColumnIndex(Name.LISTEN_1)));
            javabean.setBaby(cursor.getString(cursor.getColumnIndex(Name.BODY_2)));
            javabean.setForwarding(cursor.getString(cursor.getColumnIndex(Name.FORWARDING_3)));
            javabean.setTime(cursor.getLong(cursor.getColumnIndex(Name.TOME_4)));
            list.add(javabean);
        }
        cursor.close();
        sms_record_sqList.close();
        return list;
    }

    @Override
    public void delete_all_data() {
        SMS_Record_SQList sms_record_sqList = new SMS_Record_SQList(context);
        SQLiteDatabase sqLiteDatabase = sms_record_sqList.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from record");
    }
}
