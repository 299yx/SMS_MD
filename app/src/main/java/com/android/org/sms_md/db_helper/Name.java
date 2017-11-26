package com.android.org.sms_md.db_helper;

/**
 * Created by Administrator on 2017/10/26 0026.
 * 用于储存数据库和配置的一些名称信息
 */

public class Name {
//    监听的号码
    public static final String LISTEN_1 = "numberOfListen";
//    短信内容
    public static final String BODY_2 = "body";
//    转发给的号码
    public static final String FORWARDING_3 = "numberOfForwarding";
//    短信时间
    public static final String TOME_4 = "time";
//    储存记录的数据库
    public static final String DB_NAME = "SMS_Record_SQLIST";
//表名称
    public static final String DATA = "record";
//通用参数
    public static final String CONFI = "Configuration";
//监听服务参数
    public static final String SERVICE_CONFI = "service_configuration";
//
    public static final String CHECK_FIRM1 = "check_firm1";
//是否转发全部的按钮状态
    public static final String CHECK2_ALL = "check_all";
//
    public static final String CHECK_FIRM2 = "check_firm2";
//转发信息服务状态参数
    public static final String ServiceSTATUS = "service_status";
//监听的号码
    public static final String Listen = "Listen";
//监听的号码是否为公司号码
    public static final String ListenFirm = "ListenFirm";
//转发的号码
    public static final String Forwarding = "Forwarding";
//转发的号码是否为公司的号码
    public static final String ForwardingFirm = "ForwardingFirm";
//开始监听转发服务的时间
    public static final String StartTime = "StartTime";
//是否转发全部
    public static final String AllListen = "AllListen";
//preference参数
    public static final String PREFERENCE = "com.android.org.sms_md_preferences";
//是否声音提醒
    public static final String RING = "ring";
//是否通知提醒
    public static final String TIP = "tip";
//自动转发按钮的状态
    public static final String REPLY_SWITCH = "replySwitch";
//自动转发按软的内容(preference参数中)
    public static final String Reply = "autoReply";
//是否是第一次使用该软件
    public static final String FIRST_USE = "first_use";
//自动回复状态参数
    public static final String REPLY_START = "Is_reply_start";
//自动回复参数
    public static final String SERVICE_REPLY_CONFI = "service_reply_configuration";
//自动回复开始时间
    public static final String REPLY_START_TIME = "service_reply_time";
}
