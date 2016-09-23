package com.linson.phonesafe.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class ReBootReceiver extends BroadcastReceiver {
    public ReBootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "ReBootReceiver onReceive: +接收到启动完毕广播");
        //得到位置
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        
    }
}
