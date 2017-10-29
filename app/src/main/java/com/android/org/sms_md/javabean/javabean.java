package com.android.org.sms_md.javabean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/29 0029.
 *用于储存数据的包装类
 */

public class javabean implements Serializable{

    private String Listen;
    private String baby;
    private String Forwarding;
    private Long time;

    public javabean() {
    }

    public String getListen() {
        return Listen;
    }

    public void setListen(String listen) {
        Listen = listen;
    }

    public String getBaby() {
        return baby;
    }

    public void setBaby(String baby) {
        this.baby = baby;
    }

    public String getForwarding() {
        return Forwarding;
    }

    public void setForwarding(String forwarding) {
        Forwarding = forwarding;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
