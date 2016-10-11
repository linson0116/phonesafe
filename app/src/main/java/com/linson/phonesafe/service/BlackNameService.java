package com.linson.phonesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

import com.linson.phonesafe.db.dao.BlackNumberDao;
import com.linson.phonesafe.db.domain.BlackNumberInfo;

import static android.content.ContentValues.TAG;

public class BlackNameService extends Service {

    private BlackNameReceiver receiver;
    private BlackNumberDao dao;

    public BlackNameService() {
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: 黑名单服务已开启");
        //拦截短信
        receiver = new BlackNameReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(1000);
        registerReceiver(receiver, filter);
        //
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: 黑名单服务已关闭");
        unregisterReceiver(receiver);
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

    class BlackNameReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            for (Object object : objects) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);
                String originatingAddress = smsMessage.getOriginatingAddress();
                dao = BlackNumberDao.getInstance(context);
                BlackNumberInfo info = dao.findByPhone(originatingAddress);
                if (info == null) {
                    return;
                }
                if (info.mode.equals("1") || info.mode.equals("3")) {
                    abortBroadcast();
                    Log.i(TAG, "onReceive: 短信已拦截 " + info.phone);
                }
            }
        }
    }
}
