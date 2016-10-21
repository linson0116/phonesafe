package com.linson.phonesafe.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.linson.phonesafe.db.domain.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */

public class AppUtils {
    public static List<AppInfo> getAppInfoList(Context ctx) {
        PackageManager packageManager = ctx.getPackageManager();
        List<PackageInfo> infoList = packageManager.getInstalledPackages(0);
        List<AppInfo> list = new ArrayList<AppInfo>();
        for (PackageInfo packageInfo : infoList) {
            AppInfo appInfo = new AppInfo();
            appInfo.packageName = packageInfo.packageName;
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            appInfo.icon = applicationInfo.loadIcon(packageManager);
            appInfo.appName = applicationInfo.loadLabel(packageManager).toString();
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                appInfo.isSystem = true;
            } else {
                appInfo.isCustomer = false;
            }
            if ((applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
                appInfo.isSystem = false;
            } else {
                appInfo.isCustomer = true;
            }
            list.add(appInfo);
        }
        return list;
    }
}
