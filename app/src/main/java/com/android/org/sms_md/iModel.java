package com.android.org.sms_md;

import com.android.org.sms_md.javabean.*;

import java.util.List;

/**
 * Created by Administrator on 2017/10/27 0027.
 * 作为MVP的M层数据接口
 */

public interface iModel {

    boolean getConfiguration_Boolean(String string);
    void setConfiguration_Boolean(String string,boolean bool );

    String getConfiguration_String(String string);

//设置启动服务需要的配置信息
    void set_service_configuration(String Listen, boolean ListenFirm , String Forwarding, boolean ForwardingFirm, long StartTime, boolean AllListen);

//    获得所有的数据库数据
    List<javabean> getAllData();

//    删除所有数据
    void delete_all_data();
}
