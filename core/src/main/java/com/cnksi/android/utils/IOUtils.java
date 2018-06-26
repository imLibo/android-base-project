package com.cnksi.android.utils;

import com.cnksi.android.log.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
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
public class IOUtils {

    private IOUtils() {
    }

    /**
     * 关闭流
     *
     * @param closeable 流
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable ignored) {
                Logger.e(ignored);
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
            byte[] buf = new byte[4096];
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
    public static String readStr(InputStream in) throws IOException {
        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        Reader reader = new InputStreamReader(in, "UTF-8");
        StringBuilder sb = new StringBuilder();
        char[] buf = new char[4096];
        int len;
        while ((len = reader.read(buf)) >= 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString().trim();
    }
}
