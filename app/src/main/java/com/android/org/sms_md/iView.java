package com.android.org.sms_md;

/**
 * Created by Administrator on 2017/10/25 0025.
 *用于存放MVP的V层的接口
 */

interface iView {

//用于处理是否全部转发
    void Show_SMS_All_View();//展示输入框
    void Hide_SMS_All_View();//隐藏输入框

//    以下为展示输入EditView的错误提示信息
    void show_TextInputLayout_error1();
    void show_TextInputLayout_error2();
    void show_TextInputLayout_error3();
    void clear_TextInputLayout_error();
    void show_TextInputLayout2_error1();
    void show_TextInputLayout2_error2();
    void show_TextInputLayout2_error3();
    void show_TextInputLayout2_error4();
    void clear_TextInputLayout2_error();
}
