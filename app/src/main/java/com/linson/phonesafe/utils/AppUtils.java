package com.linson.phonesafe.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2016/10/17.
 */

public class AppUtils {
    public static List getAppInfoList(Context ctx) {
        PackageManager packageManager = ctx.getPackageManager();
        List<ApplicationInfo> infoList = packageManager.getInstalledApplications(0);
        for (ApplicationInfo info : infoList) {
            String packageName = info.packageName;
            Log.i(TAG, "getAppInfoList: " + packageName);
        }
        return null;
    }
}
