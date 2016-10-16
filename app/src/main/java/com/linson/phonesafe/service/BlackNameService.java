package com.linson.phonesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.linson.phonesafe.db.dao.BlackNumberDao;
import com.linson.phonesafe.db.domain.BlackNumberInfo;

import java.lang.reflect.Method;

import static android.content.ContentValues.TAG;

public class BlackNameService extends Service {

    private BlackNameReceiver mReceiver;
    private BlackNumberDao mDao;
    private TelephonyManager mTm;
    private PhoneStateListener mPhoneStateListener;
    private PhoneRecordContentObserver mObserver;

    public BlackNameService() {
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: 黑名单服务已开启");
        mDao = BlackNumberDao.getInstance(this);
        //拦截短信
        mReceiver = new BlackNameReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(1000);
        registerReceiver(mReceiver, filter);
        //拦截电话
        mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mPhoneStateListener = new BlackNamePhoneStateListener();
        mTm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: 黑名单服务已关闭");
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        if (mTm != null && mPhoneStateListener != null) {
            mTm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        if (mObserver != null) {
            getContentResolver().unregisterContentObserver(mObserver);
        }
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void endCall(String incomingNumber) {
        //ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
        Log.i(TAG, "endCall: ");
        try {
            //1,获取ServiceManager字节码文件
            Class<?> clazz = Class.forName("android.os.ServiceManager");
            //2,获取方法
            Method method = clazz.getMethod("getService", String.class);
            //3,反射调用此方法
            IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
            //4,调用获取aidl文件对象方法
            ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
            //5,调用在aidl中隐藏的endCall方法
            iTelephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //监听通话记录改变
        mObserver = new PhoneRecordContentObserver(new Handler(), incomingNumber);
        getContentResolver().registerContentObserver(Uri.parse("content://call_log/calls"), true, mObserver);

    }

    class PhoneRecordContentObserver extends ContentObserver {
        String number = "";

        public PhoneRecordContentObserver(Handler handler, String incomingNumber) {
            super(handler);
            number = incomingNumber;
        }

        @Override
        public void onChange(boolean selfChange) {
            //删除通话记录
            getContentResolver().delete(Uri.parse("content://call_log/calls"), "number = ?", new String[]{number});
            super.onChange(selfChange);
        }
    }
    class BlackNameReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mDao = BlackNumberDao.getInstance(getApplicationContext());
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            for (Object object : objects) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);
                String originatingAddress = smsMessage.getOriginatingAddress();

                BlackNumberInfo info = mDao.findByPhone(originatingAddress);
                if (info == null) {
                    return;
                }
                if (info.mode.equals("1") || info.mode.equals("3")) {
                    //4.4以上失效
                    abortBroadcast();
                    Log.i(TAG, "onReceive: 短信已拦截 " + info.phone);
                }
            }
        }
    }

    class BlackNamePhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i(TAG, "onCallStateChanged: " + incomingNumber);
                    BlackNumberInfo info = mDao.findByPhone(incomingNumber);
                    if (info != null) {
                        String mode = info.mode;
                        if (mode.equals("2") || mode.equals("3")) {
                            //挂断电话
                            Log.i(TAG, "onCallStateChanged: mode=" + mode);
                            endCall(incomingNumber);
                        }
                    }
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }
}
