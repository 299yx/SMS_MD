package com.android.org.sms_md.core_functions;

/**
 * Created by Administrator on 2017/10/26 0026.
 * 模型层接口
 */

public interface iModelService {
//    获得是否声音提示
    boolean get_ring_preference();
//    获得是否notification
    boolean get_notification();
//    保存已转发的短信信息
    void save_date(String L,String data,String F,Long time);
}
