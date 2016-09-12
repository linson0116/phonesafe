package com.linson.phonesafe.utils;

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

}
