package com.linson.phonesafe.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class SmsReceiver extends BroadcastReceiver {
    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        for (Object pdu : pdus) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
            String address = smsMessage.getOriginatingAddress();
            String body = smsMessage.getMessageBody();
            Log.i(TAG, "onReceive: " + address + " -- " + body);
        }
    }
}
