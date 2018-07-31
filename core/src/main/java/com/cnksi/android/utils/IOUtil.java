package com.cnksi.android.utils;

import android.text.TextUtils;

import com.cnksi.android.log.KLog;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * 文件流操作工具类
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/4/10
 * @since 1.0
 */
public class IOUtil {

    private static final int BUFFER_SIZE = 4096;

    private IOUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 关闭流
     *
     * @param closeables 流
     */
    public static void closeQuietly(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                try {
                    closeable.close();
                } catch (Throwable ignored) {
                    KLog.e(ignored);
                }
            }
        }
    }

    /**
     * 将输入流读取的字符数组中
     *
     * @param in 输入流
     * @return 字符数组
     * @throws IOException
     */
    public static byte[] readBytes(InputStream in) throws IOException {
        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            byte[] buf = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        } finally {
            closeQuietly(out);
        }
        return out.toByteArray();
    }

    /**
     * 将输入流读取到字符串中并返回 默认编码为 UTF-8
     *
     * @param in 字符串
     * @return 返回读取到的字符串
     * @throws IOException
     */
    public static String readString(InputStream in) throws IOException {
        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        Reader reader = new InputStreamReader(in, "UTF-8");
        StringBuilder sb = new StringBuilder();
        char[] buf = new char[BUFFER_SIZE];
        int len;
        while ((len = reader.read(buf)) >= 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString().trim();
    }

    /**
     * 写文件到SdCard上
     *
     * @param filePath 文件的绝对路径
     * @param content  要写入的内容
     * @return
     */
    public static boolean writeStr2File(String filePath, String content) {
        return writeStr2File(filePath, content, false);
    }

    /**
     * 写文件到SdCard上
     *
     * @param filePath 文件的绝对路径
     * @param content  要写入的内容
     * @param isAppend 是否在源文件内容基础上继续添加
     * @return
     */
    public static boolean writeStr2File(String filePath, String content, boolean isAppend) {
        boolean isSuccess = false;
        if (!TextUtils.isEmpty(content) && !"null".equalsIgnoreCase(content)) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(filePath, isAppend);
                StringBuffer sb = new StringBuffer();
                sb.append(isAppend ? content + "\n" : content);
                fos.write(sb.toString().getBytes("utf-8"));
                fos.flush();
                isSuccess = true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeQuietly(fos);
            }
        }
        return isSuccess;
    }

    /**
     * 从文件中读取字符串
     *
     * @param filePath 文件的绝对路径
     * @return 读取到的字符串
     * @throws IOException
     */
    public static String readStrFormFile(String filePath) {
        String result = "";
        if (FileUtil.isFileExists(filePath)) {
            FileInputStream fin = null;
            try {
                fin = new FileInputStream(filePath);
                int length = fin.available();
                byte[] buffer = new byte[length];
                fin.read(buffer);
                result = new String(buffer, "UTF-8");
            } catch (IOException e) {
                KLog.e(e);
            } finally {
                closeQuietly(fin);
            }
        }
        return result;
    }
}
