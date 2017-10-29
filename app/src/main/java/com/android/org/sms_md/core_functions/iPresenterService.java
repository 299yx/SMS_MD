package com.android.org.sms_md.core_functions;

/**
 * Created by Administrator on 2017/10/24 0024.
 * 作为service接口
 */

public interface iPresenterService {

    // 主要监听方法，内需加同步锁
     void core();
//    发送短信
    void sendSMS(String L,String F,String body,long time);
//     发送通知
    void sendNotice();


}
