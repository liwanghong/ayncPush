package com.sevenga.push.utils;

/**
 * Created by lizi on 15/9/7.
 */

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;

public class StringUtils {
    private static final String[] hexDigits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    public StringUtils() {
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();

        for(int i = 0; i < b.length; ++i) {
            resultSb.append(byteToHexString(b[i]));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if(b < 0) {
            n = 256 + b;
        }

        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String toMD5(String origin) {
        String resultString = null;

        try {
            resultString = new String(origin);
            MessageDigest ex = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(ex.digest(resultString.getBytes()));
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return resultString;
    }

    public static String encodeParam(String param) {
        String encodeParam = null;

        try {
            encodeParam = URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
        }

        return encodeParam;
    }

    public static String arrayToString(String[] values) {
        if(null == values) {
            return "";
        } else {
            StringBuffer buffer = new StringBuffer(values.length);

            for(int i = 0; i < values.length; ++i) {
                buffer.append(values[i]).append(",");
            }

            return buffer.length() > 0?buffer.toString().substring(0, buffer.length() - 1):"";
        }
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isTrimedEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static boolean isNotEmpty(String s) {
        return s != null && s.length() > 0;
    }
}

