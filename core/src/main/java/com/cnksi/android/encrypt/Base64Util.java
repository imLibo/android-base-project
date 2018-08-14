package com.cnksi.android.encrypt;


import android.text.TextUtils;
import android.util.Base64;

import com.cnksi.android.log.KLog;
import com.cnksi.android.utils.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Base64 工具类
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/7/30
 * @since 1.0
 */
public class Base64Util {
    /**
     * Base64加密
     *
     * @param string 加密字符串
     * @return 加密结果字符串
     */
    public static String base64EncodeStr(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        return Base64.encodeToString(string.getBytes(), Base64.DEFAULT);
    }

    /**
     * Base64解密
     *
     * @param string 解密字符串
     * @return 解密结果字符串
     */
    public static String base64DecodedStr(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        return new String(Base64.decode(string, Base64.DEFAULT));
    }

    /**
     * Base64加密
     *
     * @param file 加密文件
     * @return 加密结果字符串
     */
    public static String base64EncodeFile(File file) {
        if (null == file) {
            return "";
        }
        FileInputStream inputFile = null;
        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            if (inputFile.read(buffer) > 0) {
                return Base64.encodeToString(buffer, Base64.DEFAULT);
            }
        } catch (IOException e) {
            KLog.e(e);
        } finally {
            IOUtil.closeQuietly(inputFile);
        }
        return "";
    }

    /**
     * Base64解密
     *
     * @param filePath 解密文件路径
     * @param code     解密文件编码
     * @return 解密结果文件
     */
    public static File base64DecodedFile(String filePath, String code) {
        if (TextUtils.isEmpty(filePath) || TextUtils.isEmpty(code)) {
            return null;
        }
        FileOutputStream fos = null;
        File desFile = new File(filePath);
        try {
            byte[] decodeBytes = Base64.decode(code.getBytes(), Base64.DEFAULT);
            fos = new FileOutputStream(desFile);
            fos.write(decodeBytes);
        } catch (Exception e) {
            KLog.e(e);
        } finally {
            IOUtil.closeQuietly(fos);
        }
        return desFile;
    }
}