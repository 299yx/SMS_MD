package com.android.org.sms_md;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.org.sms_md.db_helper.Name;
import com.android.org.sms_md.tool.*;

import com.android.org.sms_md.other_activity.configuration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements iView{
    String TAG = "测试";
    Presenter presenter;

    Handler handler;
    final int Swipe_what = 0x111;

    LayoutInflater inflater;//用于加载view

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;//用连接DrawerLayout和Toolbar
// 以下是ViewPaper需要的用变量
    TabLayout tabLayout;
    ViewPager viewPager;
    View view1;
    View view2;
    List<View> views = new ArrayList<>();
    String[] titles;
//主View的变量
    Switch replySwitch;//是否开启自动回复功能
    TextView replyTextView;//自动回复的显示TextView
    CheckBox checkBox1;//是否为企业号码1
    CheckBox checkBox2;//是否是全部的复选框
    CheckBox checkBox3;//是否为企业号码2
    TextInputEditText textInputEditText;//输入需要转发号码的输入框
    TextInputLayout textInputLayout;
    TextInputEditText textOutputEditText;//输出需要转发给号码输入框
    TextInputLayout textOutputLayout;
    Button start_service;//开始服务
//    副View的变量
    SwipeRefreshLayout swipeRefreshLayout;//下拉刷新布局
    RecyclerView recyclerView;//recycle布局
    MyRecycleAdapter myRecycleAdapter;//recycle的包装
    FloatingActionButton floatingActionButton;
    CoordinatorLayout coordinatorLayout;
//drawerView变量
    LinearLayout linearLayout;//整一个布局
    ImageButton imageButton;//退出按钮
    TextView textView;//selfName
    NavigationView navigationView;//配置布局

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        first_use();
        presenter = new Presenter(this,getApplicationContext());
//        初始化一些变量
        inflater = getLayoutInflater();
        titles = getResources().getStringArray(R.array.title);
//        findViewById方法获得控件实例********
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        linearLayout = (LinearLayout)findViewById(R.id.left_view);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                linearLayout.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        imageButton = (ImageButton)findViewById(R.id.close);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(presenter.getReplyState()||presenter.getServiceStatus()){
                    drawerLayout.closeDrawers();
                    Toast.makeText(getApplicationContext(),"请先停止正在运行的服务，避免发生异常",Toast.LENGTH_LONG).show();
                }else{
                    finish();
                }
            }
        });
        textView = (TextView)findViewById(R.id.self_number);
        textView.setText(presenter.getSelfNumber());
        navigationView = (NavigationView)findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.setting:
                        Intent intent = new Intent(MainActivity.this,configuration.class);
                        startActivity(intent);
                        break;
                    case R.id.about:
                        drawerLayout.closeDrawers();
                        about();
                        break;
                    case R.id.clear_all:
                        presenter.delete_all_data();
                        Toast.makeText(getApplication(),"已成功删除数据",Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });
//        ********************************************
        initActivity();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Swipe_what){
                    swipeRefreshLayout.setRefreshing(false);//停止刷新
                }
            }
        };
        before_start();
    }

    //用于第一次使用该软件方法
    private void first_use(){
        SharedPreferences sharedPreferences = getSharedPreferences(Name.CONFI, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Name.FIRST_USE,true)){
            SharedPreferences.Editor editor = getSharedPreferences(Name.CONFI,Context.MODE_PRIVATE).edit();
            editor.putBoolean(Name.FIRST_USE,false).apply();
//            以下为第一次使用该软件的时的发生的事件
            SharedPreferences.Editor editor1 = getSharedPreferences(Name.PREFERENCE,Context.MODE_PRIVATE).edit();
            editor1.putString(Name.Reply,"已收到").apply();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.setCheckBox1();
        presenter.setCheckBox2();
        presenter.setCheckBox3();
        presenter.setReplySwitch();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void initActivity(){
//        初始化toolbar和drawerLayout
        toolbar.setTitle("");
        setSupportActionBar(toolbar);//将toolbar设为标题栏
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        将drawerLayout和toolbar关联
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close){
//可以实现打开和关闭的回调方法
        };
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        初始化ViewPaper
        view1 = inflater.inflate(R.layout.yi,null);
        view2 = inflater.inflate(R.layout.er,null);
//        实例化view1的控件变量
        textInputEditText = view1.findViewById(R.id.SMS_in);
        textInputLayout = view1.findViewById(R.id.SMS_in_view);
        textOutputEditText = view1.findViewById(R.id.SMS_Out);
        textOutputLayout = view1.findViewById(R.id.SMS_Out_view);
        if (presenter.getServiceStatus()){
            if (!presenter.getCheckBox2()){
                textInputEditText.setText(presenter.getListen());
            }
            textOutputEditText.setText(presenter.getForwarding());
        }

        checkBox1 = view1.findViewById(R.id.check_button1);
        if (presenter.getCheckBox1()){
            checkBox1.setChecked(true);
        }
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    presenter.setCheckBox1(true);
                }else {
                    presenter.setCheckBox1(false);
                }
            }
        });

        checkBox2 = view1.findViewById(R.id.check_button2);
        if (presenter.getCheckBox2()){
            checkBox2.setChecked(true);
            presenter.hide_in_view_ALL();
        }
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
              if (b){
                  presenter.hide_in_view_ALL();
              }else {
                  presenter.show_in_view_NoAll();
              }
            }
        });

        checkBox3 = view1.findViewById(R.id.check_button3);
        if (presenter.getCHeckBox3()){
            checkBox3.setChecked(true);
        }
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    presenter.setCheckBox3(true);
                }else {
                    presenter.setCheckBox3(false);
                }
            }
        });

        start_service = view1.findViewById(R.id.start_service);
        if (presenter.getServiceStatus()){
            start_service.setText(R.string.stop_service);
        }
        start_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (presenter.getServiceStatus()){
//                    当服务进行中状态
                    start_service.setText(R.string.start_service);
                    start_service.setTextColor(getResources().getColor(R.color.black));
                    presenter.intent_service_stop();
                }else{
//                    当服务未开启状态
                    if (presenter.judge_Listen(textInputEditText.getText().toString())
                            &&presenter.judge_Forwarding(textInputEditText.getText().toString(),textOutputEditText.getText().toString())) {
                        show_ALERT_start();
                    }
                }
                }
        });
        replyTextView = view1.findViewById(R.id.Reply);
        replySwitch = view1.findViewById(R.id.autoReply);
        replySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    if(!presenter.getReplyState()){
                        before_reply_service();
                    }
                }else{
                    presenter.setReplySwitch(false);
                    replyTextView.setText(R.string.yi_textView_show);
                    presenter.intent_reply_stop();


                }
            }
        });
        if (presenter.getReplySwitch()){
            replySwitch.setChecked(true);
            replyTextView.setText(R.string.yi_textView_show1);
        }
//        实例化view2控件****************
        swipeRefreshLayout = view2.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view2.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRecycleAdapter = new MyRecycleAdapter();//包装类的初始化
        recyclerView.setAdapter(myRecycleAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,RecyclerView.VERTICAL));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int topRowVerticalPosition =(recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myRecycleAdapter.notifyDataSetChanged();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(Swipe_what);
                    }
                }).start();
            }
        });
        floatingActionButton = view2.findViewById(R.id.floatingActionButton);
        coordinatorLayout = view2.findViewById(R.id.coordinatorLayout);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
                Snackbar.make(coordinatorLayout,"已跳到首行",Snackbar.LENGTH_SHORT).show();
            }
        });
//        ****************
        views.add(view1);
        views.add(view2);
        PagerAdapter pagerAdapter = new PagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

//以下放置对话框*************************************

//点击开始自动回复后，确认是否开始和确认自动回复的内容
    protected void before_reply_service(){
        String s = "自动回复的内容为：\n"+presenter.getConfiguration(Name.Reply)+"（在设置中修改）";
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(s).setNegativeButton("前往修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        replySwitch.setChecked(false);
                        startActivity( new Intent(MainActivity.this,configuration.class));
                    }
                }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.intent_reply_start();
                        presenter.setReplySwitch(true);
                        replyTextView.setText(R.string.yi_textView_show1);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
    //    在开始之前提示服务正在运行中
    protected void before_start(){
        if (presenter.getServiceStatus()||presenter.getReplyState()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("提示").setMessage("服务正在进行中").setPositiveButton("知道了",null);
            builder.create().show();
        }

    }

//    点击开始后，提示不要清理后台
    public void show_ALERT_start(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(R.string.tip).setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        start_service.setText(R.string.stop_service);
                            start_service.setTextColor(getResources().getColor(R.color.red));
                        presenter.intent_service_start(textInputEditText.getText().toString(),textOutputEditText.getText().toString());
                    }
                });
        builder.create().show();
    }
//**********************************************************

//    菜单关于
    public void about(){
        presenter.about();
    }


    @Override
    public void Show_SMS_All_View() {
        textInputEditText.findFocus();
        textInputEditText.setFocusableInTouchMode(true);
        textInputEditText.setFocusable(true);
        textInputEditText.setAlpha(1f);
        textInputEditText.setText("");
    }

    @Override
    public void Hide_SMS_All_View() {
        textInputEditText.clearFocus();
        textInputEditText.setFocusableInTouchMode(false);
        textInputEditText.setFocusable(false);
        textInputEditText.setAlpha(0.5f);
        textInputEditText.setText("已勾选：转发所有短信");
    }

    @Override
    public void show_TextInputLayout_error1() {
        if (!textInputLayout.isErrorEnabled()){
            textInputLayout.setErrorEnabled(true);
        }
        textInputLayout.setError(this.getString(R.string.error1));
    }

    @Override
    public void show_TextInputLayout_error2() {
        if (!textInputLayout.isErrorEnabled()){
            textInputLayout.setErrorEnabled(true);
        }
        textInputLayout.setError(this.getString(R.string.error2));
    }

    @Override
    public void show_TextInputLayout_error3() {
        if (!textInputLayout.isErrorEnabled()){
            textInputLayout.setErrorEnabled(true);
        }
        textInputLayout.setError(this.getString(R.string.error3));
    }

    @Override
    public void clear_TextInputLayout_error() {
        if (textInputLayout.isErrorEnabled()){
            textInputLayout.setErrorEnabled(false);
        }
    }

    @Override
    public void show_TextInputLayout2_error1() {
        if (!textOutputLayout.isErrorEnabled()){
            textOutputLayout.setErrorEnabled(true);
        }
        textOutputLayout.setError(this.getString(R.string.error1));
    }

    @Override
    public void show_TextInputLayout2_error2() {
        if (!textOutputLayout.isErrorEnabled()){
            textOutputLayout.setErrorEnabled(true);
        }
        textOutputLayout.setError(this.getString(R.string.error2));
    }

    @Override
    public void show_TextInputLayout2_error3() {
        if (!textOutputLayout.isErrorEnabled()){
            textOutputLayout.setErrorEnabled(true);
        }
        textOutputLayout.setError(this.getString(R.string.error3));
    }

    @Override
    public void show_TextInputLayout2_error4() {
        if (!textOutputLayout.isErrorEnabled()){
            textOutputLayout.setErrorEnabled(true);
        }
        textOutputLayout.setError(this.getString(R.string.error4));
    }

    @Override
    public void clear_TextInputLayout2_error() {
        if (textOutputLayout.isErrorEnabled()) {
            textOutputLayout.setErrorEnabled(false);
        }
    }

    /***
     * 用包装ViewPaper
     */
    private class PagerAdapter extends android.support.v4.view.PagerAdapter{

        PagerAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            container.removeView(views.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
    /**
     * 用于包装RecycleView
     */
    private class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.myHolder>{
        @Override
        public myHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new  myHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.recycle_view,parent,false));
        }

        @Override
        public void onBindViewHolder(myHolder holder, int position) {
            holder.textView1.setText(presenter.getAllData().get(position).getListen());
            holder.textView2.setText(presenter.getAllData().get(position).getBaby());
            holder.textView3.setText(presenter.getAllData().get(position).getForwarding());
            holder.textView4.setText(Uitls.data_L_to_S(presenter.getAllData().get(position).getTime()));
        }

        @Override
        public int getItemCount() {
            return presenter.getAllData().size();
        }

        class myHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        myHolder(View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.Listen);
            textView2 = itemView.findViewById(R.id.body);
            textView3 = itemView.findViewById(R.id.Forwarding);
            textView4 = itemView.findViewById(R.id.Listen_time);

        }
    }
    }
}
