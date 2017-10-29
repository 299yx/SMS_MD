package com.android.org.sms_md;

import com.android.org.sms_md.javabean.javabean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/25 0025
 * 写MVP的P层的接口.
 */

public interface iPresenter {
    void hide_in_view_ALL();//隐藏要转发的输入框
    void show_in_view_NoAll();//显示要转发的输入框

    void setCheckBox1(boolean bool);//设置CheckBox1

    void setCheckBox3(boolean bool);//设置CheckBox2

    /**
     * 启动服务的核心服务
     * @param Listen        要转发的号码
     * @param Forwarding    要转发给
     * @param StartTime     在什么时刻后
     * @param AllListen     是否全部转发
     */
    void intent_service(String Listen,boolean ListenFirm ,String Forwarding,boolean ForwardingFirm,long StartTime,boolean AllListen);

    /**
     * 开始服务
     */
    void intent_service_start(String L,String R);
    /**
     * 停止服务
     */
    void intent_service_stop();

//    判断输入Listen号码是都符合规范
    boolean judge_Listen(String L);
//    判断输入Forwarding码号是否符合规范
    boolean judge_Forwarding(String L,String F);

//    获得本机号码
    String getSelfNumber();

//    处理菜单about
    void about();

//    获得数据库数据（本质向上层传输数据）
    List<javabean> getAllData();

//    删除所有数据
    void delete_all_data();

}

