package com.linson.phonesafe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.linson.phonesafe.R;
import com.linson.phonesafe.activity.RocketBackground;

import static android.content.ContentValues.TAG;

public class RocketService extends Service {
    private View mRocketView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int height = (int) msg.obj;
            mParams.y = mParams.y - height;
            mWindowManager.updateViewLayout(mRocketView,mParams);
            Log.i(TAG, "handleMessage: " + mParams.y);
            super.handleMessage(msg);
        }
    };
    public RocketService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        initWindowManager();
        initParams();
        mRocketView = View.inflate(getApplicationContext(), R.layout.rocket_layout, null);
        //启动火箭动画
        ImageView iv_rocket = (ImageView) mRocketView.findViewById(R.id.iv_rocket);
        AnimationDrawable drawable = (AnimationDrawable) iv_rocket.getBackground();
        drawable.start();
        //火箭拖拽
        mRocketView.setOnTouchListener(new View.OnTouchListener() {
            int beginX = 0;
            int beginY = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (event.getRawY() > 600) {
                            sendRocket();
                            //rocketBackgroud
                            Intent intent = new Intent(getApplicationContext(), RocketBackground.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        beginX = (int) event.getRawX();
                        beginY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        int disX = moveX - beginX;
                        int disY = moveY - beginY;
                        mParams.x = mParams.x + disX;
                        mParams.y = mParams.y + disY;
                        mWindowManager.updateViewLayout(mRocketView, mParams);
                        beginX = (int) event.getRawX();
                        beginY = (int) event.getRawY();
                        break;
                }
                return true;
            }


        });
        mWindowManager.addView(mRocketView, mParams);
        super.onCreate();
    }

    private void sendRocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10 ; i++) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = Message.obtain();
                    message.obj = 50;
                    mHandler.sendMessage(message);
                }
            }
        }).start();
    }

    private void initParams() {
        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mParams.gravity = Gravity.LEFT + Gravity.TOP;
    }

    private void initWindowManager() {
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mRocketView != null && mWindowManager != null) {
            mWindowManager.removeViewImmediate(mRocketView);
        }
        super.onDestroy();
    }
}
