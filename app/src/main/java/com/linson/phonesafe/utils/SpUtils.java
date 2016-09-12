package com.linson.phonesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/9/6.
 */

public class SpUtils {
    public static String getPsd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ConstantValues.PSD_XML, Context.MODE_PRIVATE);
        String psd = preferences.getString(ConstantValues.PSD, "");
        return psd;
    }

    public static void setPsd(Context context,String psd) {
        SharedPreferences preferences = context.getSharedPreferences(ConstantValues.PSD_XML, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(ConstantValues.PSD, psd);
        edit.commit();
    }
    public static String getSIMNumber(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ConstantValues.PSD_XML, Context.MODE_PRIVATE);
        String sim_number = preferences.getString(ConstantValues.SIM_NUMBER, "");
        return sim_number;
    }
}