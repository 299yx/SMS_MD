package com.android.org.sms_md.core_function_2;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.android.org.sms_md.db_helper.Name;


import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/11/26 0026.
 * 自动回复服务
 */


public class ReplyService extends Service implements iPresenter{
    ReplyService.Reply reply;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        reply = new ReplyService.Reply(new Handler());
        getContentResolver().registerContentObserver(Uri.parse("content://sms"),false,reply);
        Toast.makeText(this,"短信自动回复功能开启",Toast.LENGTH_LONG).show();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(reply);
        Toast.makeText(this,"自动短信回复功能停止",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean send_SMS(String number) {
        SmsManager smsManager = SmsManager.getDefault();
        PendingIntent pendingIntent = PendingIntent.getActivity(ReplyService.this,0,new Intent(),0);
        String string = get_SMS_body()+"\n--来自短信助手";
        smsManager.sendTextMessage(number,null,string,pendingIntent,null);
        return true;
    }

    @Override
    public long get_start_time() {
        SharedPreferences sharedPreferences = getSharedPreferences(Name.SERVICE_REPLY_CONFI, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(Name.REPLY_START_TIME,Long.MAX_VALUE);
    }

    @Override
    public String get_SMS_body() {
        SharedPreferences sharedPreferences = getSharedPreferences(Name.PREFERENCE,Context.MODE_PRIVATE);
        return sharedPreferences.getString(Name.Reply,null);
    }

    @Override
    public void update_time(long newTime) {
        SharedPreferences.Editor editor = getSharedPreferences(Name.SERVICE_REPLY_CONFI, Context.MODE_PRIVATE).edit();
        editor.putLong(Name.REPLY_START_TIME,newTime).apply();
    }
//contentObserver
    private class Reply extends ContentObserver{

        Reply(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            synchronized (""){
                super.onChange(selfChange);
                Log.d(TAG, "onChange: 11");
                Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"),null,null,null,null);
                while (cursor.moveToNext()){
                    if (get_start_time() < Long.valueOf(cursor.getString(cursor.getColumnIndex("receive_date")))){
                        send_SMS(cursor.getString(cursor.getColumnIndex("address")));
                        update_time( Long.valueOf(cursor.getString(cursor.getColumnIndex("receive_date"))));
                        cursor.close();
                        return;
                    }
                }
                cursor.close();
            }
        }
    }
}
