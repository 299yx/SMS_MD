package com.android.org.sms_md.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/10/27 0027.
 * 工具类
 */

public class Uitls
{
    public static String data_L_to_S(long time){
        Date date = new Date(time);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh时mm分ss秒");
        return df.format(date);
    }
}
