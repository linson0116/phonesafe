package com.linson.phonesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Administrator on 2016/10/21.
 */
public class ProcessUtils {
    public static int getProcessNum(Context ctx) {
        ActivityManager am = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        if (runningAppProcesses != null) {
            return runningAppProcesses.size();
        }
        return 0;
    }
}
