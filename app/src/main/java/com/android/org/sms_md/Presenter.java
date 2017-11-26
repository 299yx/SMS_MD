package com.android.org.sms_md;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;

import com.android.org.sms_md.core_function_2.ReplyService;
import com.android.org.sms_md.db_helper.*;
import com.android.org.sms_md.core_functions.ListenService;
import com.android.org.sms_md.javabean.javabean;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
/**
 * Created by Administrator on 2017/10/25 0025.
 * 控制层
 */
public class Presenter implements iPresenter {
    String TAG = "测试";

    private Model model;
    private iView view;
    private Context context;
    private boolean checkBox1 = false;    //是否是企业号码_1
    private boolean checkBox2 = false;     //是否转发全部
    private boolean checkBox3 = false;      //是否是企业号码_2
    private boolean serviceStatus = false;  //开始键的状态
    private boolean replySwitch = false;    //自动监听的状态
    //***get和set****
    public boolean getReplySwitch() {
        return replySwitch;
    }

    public void setReplySwitch() {
        model.setConfiguration_Boolean(Name.REPLY_SWITCH,replySwitch);
    }

    public void setCheckBox1(){
        model.setConfiguration_Boolean(Name.CHECK_FIRM1,checkBox1);
    }
    public boolean getCheckBox1(){
        return checkBox1;
    }

    public void setCheckBox2(){
        model.setConfiguration_Boolean(Name.CHECK2_ALL,checkBox2);
    }
    public boolean getCheckBox2(){
        return checkBox2;
    }

    public void setCheckBox3(){
        model.setConfiguration_Boolean(Name.CHECK_FIRM2,checkBox3);
    }
    public boolean getCHeckBox3(){
        return checkBox3;
    }

    public void setServiceStatus(){
        model.setConfiguration_Boolean(Name.ServiceSTATUS, serviceStatus);
    }
    public boolean getServiceStatus(){
        return serviceStatus;
    }
    public String getListen(){
        return model.getConfiguration_String(Name.Listen);
    }
    public String getForwarding(){
        return model.getConfiguration_String(Name.Forwarding);
    }

    public boolean getReplyState(){
        return model.getConfiguration_Boolean(Name.REPLY_START);
    }
    //**************


    //    构造方法
    public Presenter(iView view, Context context){
        this.view = view;
        this.context = context;
        model = new Model(context,this);
        checkBox1 = model.getConfiguration_Boolean(Name.CHECK_FIRM1);
        checkBox2 = model.getConfiguration_Boolean(Name.CHECK2_ALL);
        checkBox3 = model.getConfiguration_Boolean(Name.CHECK_FIRM2);
        replySwitch = model.getConfiguration_Boolean(Name.REPLY_SWITCH);
        serviceStatus = model.getConfiguration_Boolean(Name.ServiceSTATUS);
    }

    @Override
    public void hide_in_view_ALL() {
        view.Hide_SMS_All_View();
        checkBox2 = true;
    }

    @Override
    public void show_in_view_NoAll() {
        view.Show_SMS_All_View();
        checkBox2 = false;
    }

    @Override
    public void setCheckBox1(boolean bool) {
        checkBox1 = bool;
    }

    @Override
    public void setCheckBox3(boolean bool) {
        checkBox3 = bool;
    }

    @Override
    public void setReplySwitch(boolean bool) {
        replySwitch = bool;
    }

    @Override
    public void intent_service(String Listen,boolean ListenFirm ,String Forwarding,boolean ForwardingFirm,long StartTime,boolean AllListen) {
        Intent intent = new Intent(context,ListenService.class);
        model.set_service_configuration(Listen,ListenFirm,Forwarding,ForwardingFirm,StartTime,AllListen);
        context.startService(intent);
    }

    @Override
    public void intent_service_start(String L,String R) {
        serviceStatus = true;
        long time = System.currentTimeMillis();
        setServiceStatus();
        intent_service(L,checkBox1,R,checkBox3,time,checkBox2);
    }

    @Override
    public void intent_service_stop() {
        serviceStatus = false;
        setServiceStatus();
        Intent intent = new Intent(context,ListenService.class);
        context.stopService(intent);
    }

    @Override
    public void intent_reply_start() {
        model.setConfiguration_Boolean(Name.REPLY_START,true);
        model.set_reply_service_configuration(System.currentTimeMillis());
        context.startService(new Intent(context,ReplyService.class));
    }

    @Override
    public void intent_reply_stop() {
        model.setConfiguration_Boolean(Name.REPLY_START,false);
        Intent intent = new Intent(context,ReplyService.class);
        context.stopService(intent);
    }

    @Override
    public boolean judge_Listen(String L) {
        if (!checkBox2){
            if (!(L.isEmpty())){
                if (checkBox1){
                    return true;
                }
                String regex = "\\d{11}";
                if (L.matches(regex)){
                    String regex1 = "1[3|4|5|8]\\d{9}";
                    if (L.matches(regex1)){
                        view.clear_TextInputLayout_error();
                        return true;
                    }
                    view.show_TextInputLayout_error1();
                    return false;
                }
                view.show_TextInputLayout_error2();
                return false;
            }
            view.show_TextInputLayout_error3();
            return false;
        }
        view.clear_TextInputLayout_error();
        return true;
    }

    @Override
    public boolean judge_Forwarding(String L,String F) {
        if (!(F.isEmpty())){
            if (!(L.equals(F))){
                if (checkBox3){
                    view.clear_TextInputLayout2_error();
                    return true;
                }
                String regex = "\\d{11}";
                if (F.matches(regex)){
                    String regex1 = "1[3|4|5|8]\\d{9}";
                    if (F.matches(regex1)){
                        view.clear_TextInputLayout2_error();
                        return true;
                    }
                    view.show_TextInputLayout2_error1();
                    return false;
                }
                view.show_TextInputLayout2_error2();
                return false;
            }
            view.show_TextInputLayout2_error4();
            return false;
        }
        view.show_TextInputLayout2_error3();
        return false;
    }

    @Override
    public String getSelfNumber() {
        TelephonyManager mTelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getDeviceId();

    }

    @Override
    public String getConfiguration(String key) {
        return model.get_preference_configuration_string(key);
    }

    @Override
    public void about() {
        // TODO: 2017/10/28 0028 可以访问本网站的网址 ，百度这是用于测试
        Uri uri = Uri.parse("http://www.baidu.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public List<javabean> getAllData() {
        return model.getAllData();
    }

    @Override
    public void delete_all_data() {
        model.delete_all_data();
    }
}
