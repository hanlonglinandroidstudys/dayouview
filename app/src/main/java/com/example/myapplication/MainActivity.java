package com.example.myapplication;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myapplication.inneractivitys.Activity1;
import com.example.myapplication.inneractivitys.Activity2;
import com.example.myapplication.inneractivitys.Activity3;

public class MainActivity extends ActivityGroup {

    /**
     * TAG
     */
    private String TAG = "MainActivity";

    //==========状态标识==========
    /**
     * type1标识
     */
    public static final int TYPE_1 = 1;
    /**
     * type2标识
     */
    public static final int TYPE_2 = 2;
    /**
     * type3标识
     */
    public static final int TYPE_3 = 3;

    //==========使用模式==========
    /**
     * 当前使用的模式
     * 1 普通
     * 2 wifi
     */
    private double MODE = MODE_INIT;

    /**
     * init模式
     */
    private static final int MODE_INIT = 1;
    /**
     * wifi模式
     */
    private static final int MODE_WIFI = 2;

    //==========轮训次数==========
    /**
     * type1轮训次数
     */
    private int type1_num = 0;
    /**
     * type2轮训次数
     */
    private int type2_num = 0;

    /**
     * type1轮训最大次数
     */
    private static final int type1_max_num = 3;
    /**
     * type2轮训最大次数
     */
    private static final int type2_max_num = 30;

    //==========执行结果状态==========
    /**
     * type1 状态是否ok
     */
    boolean IS_TYPE1_OK = false;
    /**
     * type2状态是否ok
     */
    boolean IS_TYPE2_OK = false;


    //==========view控件==========
    FrameLayout frlayout;
    RadioGroup radioGroup;
    Button btn_goto_web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setCrashHandler();

        try {
            changeMode(MODE_WIFI);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //initData();

        //testCrashInMainThread();
        testCrashInChildThread();

        initView();
    }

    private void initView() {
        frlayout=(FrameLayout) findViewById(R.id.frlayout);
        radioGroup=(RadioGroup)findViewById(R.id.ra_group);
        btn_goto_web=findViewById(R.id.btn_goto_web);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.ra_ac1:
                        forward(Activity1.class);
                        break;
                    case R.id.ra_ac2:
                        forward(Activity2.class);
                        break;
                    case R.id.ra_ac3:
                        forward(Activity3.class);
                        break;
                }
            }
        });
        btn_goto_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,WebActivity.class));
            }
        });
    }



    /**
     * 子线程中测试异常
     */
    private void testCrashInChildThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                testCrashInMainThread();
            }
        }).start();
    }

    /**
     * 主线程中测试异常
     */
    private void testCrashInMainThread() {
        int a = 0;
        int b = 10;
        int c = b / a;
    }

    /**
     * 设置app异常捕获
     *
     * 区分主线程和子线程处理2中情况
     * 主线程：记录日志，关闭进程
     * 子线程：记录日志，不关闭进程
     */

    private void setCrashHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    // 当前异常出现在主线程中
                    Log.e("MainActivity", t + "主线程出现异常！" + e.getMessage());
                    // 退出程序
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                } else {
                    // 异常出现在子线程中
                    Log.e("MainActivity", t + "子线程出现异常！" + e.getMessage());
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        type1_num = 0;
        type2_num = 0;
        if (MODE == MODE_INIT) {
            sendInitMessage();
        } else if (MODE == MODE_WIFI) {
            sendWifiMessage();
        }
    }

    /**
     * 切换模式
     *
     * @param mode
     * @throws Exception
     */
    private void changeMode(int mode) throws Exception {
        if (mode != MODE_INIT
                && mode != MODE_WIFI) {
            throw new Exception("mode不合法！");
        }
        MODE = mode;
    }

    /**
     * 执行定时器，处理结果
     */
    public Handler MyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TYPE_1:
                    if (!IS_TYPE1_OK) {
                        if (type1_num < type1_max_num) {
                            //init轮训
                            sendInitMessage();
                            type1_num++;
                            Log.e(TAG, "type1 轮训：" + type1_num);
                        } else {
                            //init超时
                            Toast.makeText(MainActivity.this, "Type1 超时", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //init状态ok
                        Toast.makeText(MainActivity.this, "Type1 成功", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case TYPE_2:
                    if (!IS_TYPE2_OK) {
                        if (type2_num < type2_max_num) {
                            //wifi轮训
                            sendWifiMessage();
                            type2_num++;
                            Log.e(TAG, "type2 轮训：" + type2_num);
                        } else {
                            //wifi超时
                            Toast.makeText(MainActivity.this, "Type2 超时", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //wifi成功
                        Toast.makeText(MainActivity.this, "Type2 成功", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case TYPE_3:
                    break;
            }
        }
    };


    /**
     * 发送普通消息
     * 循环3次 每隔2s
     */
    private void sendInitMessage() {
        Message msg = Message.obtain();
        msg.what = TYPE_1;
        MyHandler.sendMessageDelayed(msg, 2000);
    }

    /**
     * 发送wifi消息
     * 循环30次 每隔1s
     */
    private void sendWifiMessage() {
        Message msg = Message.obtain();
        msg.what = TYPE_2;
        MyHandler.sendMessageDelayed(msg, 1000);
    }


    /**
     * 跳转页面
     * @param activityClass
     */
    private void forward(Class activityClass) {
        frlayout.removeAllViews();
        frlayout.addView(getLocalActivityManager().startActivity(activityClass.getName(),
                new Intent(MainActivity.this,activityClass)).getDecorView());
    }
}
