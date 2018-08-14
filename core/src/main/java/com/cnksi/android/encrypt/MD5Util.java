package com.cnksi.android.encrypt;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cnksi.android.log.KLog;
import com.cnksi.android.utils.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 工具类
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/7/30
 * @since 1.0
 */
public class MD5Util {

    /**
     * MD5加密
     *
     * @param string 加密字符串
     * @return 加密结果字符串
     * @see #md5(String, String)
     */
    public static String md5(@NonNull String string) {
        return TextUtils.isEmpty(string) ? "" : md5(string, "");
    }

    /**
     * MD5加密(加盐)
     *
     * @param string 加密字符串
     * @param slat   加密盐值key
     * @return 加密结果字符串
     */
    public static String md5(@NonNull String string, String slat) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest((string + slat).getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * MD5加密(多次)
     *
     * @param string 加密字符串
     * @param times  重复加密次数
     * @return 加密结果字符串
     */
    public static String md5(@NonNull String string, int times) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        String md5 = string;
        for (int i = 0; i < times; i++) {
            md5 = md5(md5);
        }
        return md5;
    }


    /**
     * MD5加密(文件)
     * 可用于文件校验。
     *
     * @param file 加密文件
     * @return md5 数值
     */
    public static String md5(@NonNull File file) {
        if (!file.isFile()) {
            return "";
        }
        MessageDigest digest;
        FileInputStream in = null;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            IOUtil.closeQuietly(in);
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 获取签名Md5
     *
     * @param paramArrayOfByte
     * @return
     */
    public static String md5(byte[] paramArrayOfByte) {
        char[] asciiTable = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
        try {
            MessageDigest md5MessageDigest = MessageDigest.getInstance("MD5");
            md5MessageDigest.update(paramArrayOfByte);
            byte[] tempByte = md5MessageDigest.digest();
            int i = tempByte.length;
            char[] tempChar = new char[i * 2];
            int j = 0;
            int k = 0;
            while (true) {
                if (j >= i) {
                    return new String(tempChar);
                }
                int m = tempByte[j];
                int n = k + 1;
                tempChar[k] = asciiTable[(0xF & m >>> 4)];
                k = n + 1;
                tempChar[n] = asciiTable[(m & 0xF)];
                j++;
            }
        } catch (Exception e) {
            KLog.e(e);
        }
        return null;
    }
}
