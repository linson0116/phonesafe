package com.linson.phonesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.linson.phonesafe.db.dao.BlackNumberDao;
import com.linson.phonesafe.db.domain.BlackNumberInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.content.ContentValues.TAG;

public class BlackNameService extends Service {

    private BlackNameReceiver receiver;
    private BlackNumberDao mDao;
    private TelephonyManager mTm;
    private PhoneStateListener phoneStateListener;

    public BlackNameService() {
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: 黑名单服务已开启");
        mDao = BlackNumberDao.getInstance(this);
        //拦截短信
        receiver = new BlackNameReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(1000);
        registerReceiver(receiver, filter);
        //拦截电话
        mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        phoneStateListener = new BlackNamePhoneStateListener();
        mTm.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: 黑名单服务已关闭");
        unregisterReceiver(receiver);
        mTm.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
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

    private void endCall() {
        //ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
        Log.i(TAG, "endCall: ");
        try {
            Class clazz = Class.forName("android.os.ServiceManager");
            Method method = clazz.getMethod("getService", String.class);
            IBinder iBinder = (IBinder) method.invoke(null, Context.TELECOM_SERVICE);
            ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
            iTelephony.endCall();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
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
                    String mode = info.mode;
                    if (mode.equals("2") || mode.equals("3")) {
                        //挂断电话
                        Log.i(TAG, "onCallStateChanged: mode=" + mode);
                        endCall();
                    }
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }
}
