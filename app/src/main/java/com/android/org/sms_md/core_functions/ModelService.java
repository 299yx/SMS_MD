package com.android.org.sms_md.core_functions;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.android.org.sms_md.db_helper.*;

/**
 * Created by Administrator on 2017/10/26 0026.
 * 做为ListenService的模型层
 ** */

public class ModelService implements iModelService {
    private Context context;

    private String Listen;
    private boolean ListenFirm;
    private String Forwarding;
    private boolean ForwardingFirm;
    private long StartTime;
    private boolean AllListen;

//    get和set方法*************

    public String getListen() {
        return Listen;
    }

    public void setListen(String listen) {
        Listen = listen;
    }

    public boolean isListenFirm() {
        return ListenFirm;
    }

    public void setListenFirm(boolean listenFirm) {
        ListenFirm = listenFirm;
    }
    public String getForwarding() {
        return Forwarding;
    }

    public void setForwarding(String forwarding) {
        Forwarding = forwarding;
    }

    public boolean isForwardingFirm() {
        return ForwardingFirm;
    }

    public void setForwardingFirm(boolean forwardingFirm) {
        ForwardingFirm = forwardingFirm;
    }

    public long getStartTime() {
        return StartTime;
    }

    public void setStartTime(long startTime) {
        StartTime = startTime;
        SharedPreferences.Editor editor = context.getSharedPreferences(Name.SERVICE_CONFI,Context.MODE_PRIVATE).edit();
        editor.putLong(Name.StartTime,StartTime).apply();
    }

    public boolean isAllListen() {
        return AllListen;
    }

    public void setAllListen(boolean allListen) {
        AllListen = allListen;
    }


//    构造方法*************初始化参数
    public ModelService(Context context,iPresenterService iPresenterService) {
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name.SERVICE_CONFI,Context.MODE_PRIVATE);
        Listen = sharedPreferences.getString(Name.Listen,null);
        ListenFirm = sharedPreferences.getBoolean(Name.ListenFirm,false);
        Forwarding = sharedPreferences.getString(Name.Forwarding,null);
        ForwardingFirm = sharedPreferences.getBoolean(Name.ForwardingFirm,false);
        StartTime = sharedPreferences.getLong(Name.StartTime,Long.MAX_VALUE);
        AllListen = sharedPreferences.getBoolean(Name.AllListen,false);
    }


    @Override
    public boolean get_ring_preference() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name.PREFERENCE,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Name.RING,false);
    }

    @Override
    public boolean get_notification() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name.PREFERENCE,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Name.TIP,false);
    }

    @Override
    public void save_date(String L,String data,String F,Long time) {
        Object[] objects = {L,data,F,time};
        SMS_Record_SQList sms_record_sQList = new SMS_Record_SQList(context);
        SQLiteDatabase sqLiteDatabase = sms_record_sQList.getWritableDatabase();
        sqLiteDatabase.execSQL("insert into record values(?,?,?,?)",objects);
        sqLiteDatabase.close();
    }
}
