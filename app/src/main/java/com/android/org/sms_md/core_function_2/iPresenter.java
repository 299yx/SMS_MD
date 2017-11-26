package com.android.org.sms_md.core_function_2;

/**
 * Created by Administrator on 2017/11/26 0026.
 * 自动转发服务接口程序
 */

public interface iPresenter {
//    发送信息
    boolean send_SMS(String number);
//    获得开始监听的时间
    long get_start_time();
//    获得要回复的内容
    String get_SMS_body();
//    更新时间
    void update_time(long newTime);

}
