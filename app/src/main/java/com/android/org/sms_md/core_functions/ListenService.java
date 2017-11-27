package com.android.org.sms_md.core_functions;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.android.org.sms_md.R;
import com.android.org.sms_md.tool.*;

/**
 * Created by Administrator on 2017/10/24 0024.
 * 作为接受短信的服务进程
 */

public class ListenService extends Service implements iPresenterService {

    private ModelService modelService;
    private ListenService.SmsObService smsObService;

    String TAG = "测试";

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        modelService = new ModelService(getApplicationContext(),this);
        core();
        Toast.makeText(this,"自动转发服务已成功启动",Toast.LENGTH_SHORT).show();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(smsObService);
        Toast.makeText(this,"自动转发服务已停止",Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void core() {
        smsObService = new SmsObService(new Handler());
        getContentResolver().registerContentObserver(Uri.parse("content://sms"),true,smsObService);
    }
//用于判断是不是企业码，不是的话加+86
    public String work_num(String num){
        if (!modelService.isListenFirm()){
            return "+86"+num;
        }
        return num;
    }

    @Override
    public void sendSMS(String L,String F, String body, long time) {
        SmsManager smsManager = SmsManager.getDefault();
        PendingIntent pendingIntent = PendingIntent.getActivity(ListenService.this,0,new Intent(),0);
        String string = "转发至："+L+'\n'+"原文："+body+'\n'+"接受时间："+Uitls.data_L_to_S(time)+'\n'+getResources().getString(R.string.extra);
        smsManager.sendTextMessage(F,null,string,pendingIntent,null);//实现转发功能
    }

    @Override
    public void sendNotice() {
        if (modelService.get_notification()){
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE) ;
            Notification notification;
            if (modelService.get_ring_preference()){
                notification = new Notification.Builder(this).setTicker("来自短信助手")
                        .setSmallIcon(R.mipmap.notice).setDefaults(Notification.DEFAULT_SOUND).setContentTitle("提醒：")
                        .setContentText("已成功转发一条短信，详情进入应用查看").build();
            }else {
                notification = new Notification.Builder(this).setTicker("来自短信助手")
                        .setSmallIcon(R.mipmap.notice).setContentTitle("提醒：")
                        .setContentText("已成功转发一条短信，详情进入应用查看").build();
            }
            notificationManager.notify(0x123,notification);
        }
    }

    //    用于数据库监听回调的class
    private class SmsObService extends ContentObserver {
        public SmsObService(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            synchronized (""){
                super.onChange(selfChange);
                Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"),null,null,null,null);
//            开始筛选和判断号码   遍历整个inbox表内所有的短信记录的
                while (cursor.moveToNext()){
//                先比较时间上是否符合要求
                    if (modelService.getStartTime() < Long.valueOf(cursor.getString(cursor.getColumnIndex("receive_date")))){
//                    比较号码是否符合要求（先解决是否全部转发）
                        if (modelService.isAllListen()){//转发所有短信
                            // TODO: 2017/10/26 0026 转发短息
                            modelService.setStartTime(Long.valueOf(cursor.getString(cursor.getColumnIndex("receive_date"))));//刷新时间
                            String Listen_Forwarding = cursor.getString(cursor.getColumnIndex("address"));
                            sendSMS(Listen_Forwarding,modelService.getForwarding(),cursor.getString(cursor.getColumnIndex("body")),modelService.getStartTime());
                            modelService.save_date(cursor.getString(cursor.getColumnIndex("address")),cursor.getString(cursor.getColumnIndex("body")),modelService.getForwarding(),Long.valueOf(cursor.getString(cursor.getColumnIndex("receive_date"))));
                            sendNotice();
                            break;
                        }else {
                            if (work_num(modelService.getListen()).equals(cursor.getString(cursor.getColumnIndex("address")))){//转发特定号码的短信
                                // TODO: 2017/10/26 0026 转发短信
                                String Listen_Forwarding = cursor.getString(cursor.getColumnIndex("address"));
                                modelService.setStartTime(Long.valueOf(cursor.getString(cursor.getColumnIndex("receive_date"))));//刷新时间
                                sendSMS(Listen_Forwarding,modelService.getForwarding(),cursor.getString(cursor.getColumnIndex("body")),modelService.getStartTime());
                                modelService.save_date(cursor.getString(cursor.getColumnIndex("address")),cursor.getString(cursor.getColumnIndex("body")),modelService.getForwarding(),Long.valueOf(cursor.getString(cursor.getColumnIndex("receive_date"))));
                                sendNotice();
                                break;
                            }
                        }
                    }
                }
                cursor.close();
            }
        }
    }
}
