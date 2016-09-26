package com.linson.phonesafe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.linson.phonesafe.R;

import static android.content.ContentValues.TAG;

public class PhoneStatusService extends Service {

    private TelephonyManager mTM;
    private MyPhoneStateListener myPhoneStateListener;

    public PhoneStatusService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: 服务已启动");
        myPhoneStateListener = new MyPhoneStateListener();
        mTM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTM.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i(TAG, "onCallStateChanged: 空闲");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i(TAG, "onCallStateChanged: 响铃");

                    WindowManager.LayoutParams params = new WindowManager.LayoutParams();

                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    params.format = PixelFormat.TRANSLUCENT;
                    //在响铃的时候显示吐司,和电话类型一致
                    Log.i(TAG, "openToast: " + params.type);
                    params.type = WindowManager.LayoutParams.TYPE_PHONE;

                    //指定吐司的所在位置(将吐司指定在左上角)
                    params.gravity = Gravity.CENTER;

                    WindowManager mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
                    View viewToast = View.inflate(getApplicationContext(), R.layout.toast_phone, null);
                    mWM.addView(viewToast,params);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i(TAG, "onCallStateChanged: 摘机");
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }
    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: 服务已销毁");
        mTM.listen(myPhoneStateListener,PhoneStateListener.LISTEN_NONE);
        super.onDestroy();
    }
}
