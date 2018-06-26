package com.cnksi.android.utils;

import com.cnksi.android.log.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

/**
 * md5相关类
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/4/10
 * @since 1.0
 */
public final class MD5 {

    /**
     * 获取字节数组的MD5签名
     *
     * @param paramArrayOfByte 字节数组
     * @return md5
     */
    public static String getMessageDigest(byte[] paramArrayOfByte) {
        char[] arrayOfChar1 = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            int i = arrayOfByte.length;
            char[] arrayOfChar2 = new char[i * 2];
            int j = 0;
            int k = 0;
            while (true) {
                if (j >= i) {
                    return new String(arrayOfChar2);
                }
                int m = arrayOfByte[j];
                int n = k + 1;
                arrayOfChar2[k] = arrayOfChar1[(0xF & m >>> 4)];
                k = n + 1;
                arrayOfChar2[n] = arrayOfChar1[(m & 0xF)];
                j++;
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        return "";
    }

    /**
     * 计算md5后所产生的哈希值的字节数组
     *
     * @param paramArrayOfByte 字节数组
     * @return 产生的哈希值的字节数组
     */
    public static byte[] getRawDigest(byte[] paramArrayOfByte) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            return localMessageDigest.digest();
        } catch (Exception e) {
            Logger.e(e);
        }
        return null;
    }

    /**
     * 获取文件的MD5值
     *
     * @param file 文件
     * @return md5值
     */
    public static String getFileMd5(File file) throws Exception {
        // 缓冲区大小
        int bufferSize = 256 * 1024;
        FileInputStream fileInputStream = null;
        DigestInputStream digestInputStream = null;
        try {
            // 拿到一个MD5转换器（同样，这里可以换成SHA1）
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 使用DigestInputStream
            fileInputStream = new FileInputStream(file);
            digestInputStream = new DigestInputStream(fileInputStream, messageDigest);
            // read的过程中进行MD5处理，直到读完文件
            byte[] buffer = new byte[bufferSize];
            while (digestInputStream.read(buffer) > 0) {
                // 获取最终的MessageDigest
                messageDigest = digestInputStream.getMessageDigest();
            }
            // 拿到结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            // 同样，把字节数组转换成字符串
            return byteArrayToHex(resultByteArray);
        } finally {
            IOUtils.closeQuietly(digestInputStream);
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    /**
     * 用于将字节数组换成成16进制的字符串
     *
     * @param byteArray 字节数组
     * @return 16进制字符串
     */
    public static String byteArrayToHex(byte[] byteArray) {
        String hs = "", stmp;
        for (int n = 0; n < byteArray.length; n++) {
            stmp = (Integer.toHexString(byteArray[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs.concat("0").concat(stmp);
            } else {
                hs = hs.concat(stmp);
            }
            if (n < byteArray.length - 1) {
                hs = hs.concat("");
            }
        }
        return hs;
    }
}