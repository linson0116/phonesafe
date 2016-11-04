package com.linson.phonesafe.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/9/7.
 */

public class ToolsUtils {
    public static String md5Encode(String psd) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            psd += "com.linson.phonesafe";
            byte[] bytes = digest.digest(psd.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : bytes) {
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void asset2app(Context ctx, String fileName) {
        try {
            InputStream is = ctx.getAssets().open(fileName);
            File file = new File(ctx.getFilesDir(), fileName);
            OutputStream os = new FileOutputStream(file);
            int len = 0;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
            }
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
