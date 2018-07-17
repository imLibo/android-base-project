package com.cnksi.android.log;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

/**
 * Log统一管理类 借鉴 zhaokaiqiang github https://github.com/ZhaoKaiQiang/KLog 日志功能
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/4/10
 * @since 1.0
 */
public final class KLog {

    private KLog() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String NULL_TIPS = "Log with null object";
    private static final String DEFAULT_MESSAGE = "execute";
    private static final String PARAM = "Param";
    private static final String NULL = "null";
    private static final String TAG_DEFAULT = "KLog";
    private static final String SUFFIX = ".java";

    public static final int JSON_INDENT = 4;
    public static final int V = 0x1;

    public static final int D = 0x2;
    public static final int I = 0x3;
    public static final int W = 0x4;
    public static final int E = 0x5;
    public static final int A = 0x6;

    private static final int JSON = 0x7;
    private static final int STACK_TRACE_INDEX = 5;

    private static String mGlobalTag;
    private static boolean mIsGlobalTagEmpty = true;
    private static boolean IS_SHOW_LOG = true;

    /**
     * 初始化
     *
     * @param isShowLog 是否打印日志 默认打印
     */
    public static void init(boolean isShowLog) {
        IS_SHOW_LOG = isShowLog;
    }

    /**
     * 初始化
     *
     * @param isShowLog 是否打印日志 默认打印
     * @param tag       自定义TAG
     */
    public static void init(boolean isShowLog, @Nullable String tag) {
        IS_SHOW_LOG = isShowLog;
        mGlobalTag = tag;
        mIsGlobalTagEmpty = TextUtils.isEmpty(mGlobalTag);
    }

    public static void v() {
        printLog(V, null, DEFAULT_MESSAGE);
    }

    public static void v(Object msg) {
        printLog(V, null, msg);
    }

    /**
     * 打印 VERBOSE 级别的日志
     *
     * @param tag     自定义TAG
     * @param objects 需要打印的对象
     */
    public static void v(String tag, Object... objects) {
        printLog(V, tag, objects);
    }

    public static void d() {
        printLog(D, null, DEFAULT_MESSAGE);
    }

    public static void d(Object msg) {
        printLog(D, null, msg);
    }

    /**
     * 打印 DEBUG 级别的日志
     *
     * @param tag     自定义TAG
     * @param objects 需要打印的对象
     */
    public static void d(String tag, Object... objects) {
        printLog(D, tag, objects);
    }

    public static void i() {
        printLog(I, null, DEFAULT_MESSAGE);
    }

    public static void i(Object msg) {
        printLog(I, null, msg);
    }

    /**
     * 打印 INFO 级别的日志
     *
     * @param tag     自定义TAG
     * @param objects 需要打印的对象
     */
    public static void i(String tag, Object... objects) {
        printLog(I, tag, objects);
    }

    public static void w() {
        printLog(W, null, DEFAULT_MESSAGE);
    }

    public static void w(Object msg) {
        printLog(W, null, msg);
    }

    /**
     * 打印 WARN 级别的日志
     *
     * @param tag     自定义TAG
     * @param objects 需要打印的对象
     */
    public static void w(String tag, Object... objects) {
        printLog(W, tag, objects);
    }

    public static void e() {
        printLog(E, null, DEFAULT_MESSAGE);
    }

    public static void e(Object msg) {
        printLog(E, null, msg);
    }

    /**
     * 打印 ERROR 级别的日志
     *
     * @param tag     自定义TAG
     * @param objects 需要打印的对象
     */
    public static void e(String tag, Object... objects) {
        printLog(E, tag, objects);
    }

    public static void a() {
        printLog(A, null, DEFAULT_MESSAGE);
    }

    public static void a(Object msg) {
        printLog(A, null, msg);
    }

    /**
     * what a terrible failure 可怕的失败：报告一个永远不可能发生的情况
     *
     * @param tag     自定义TAG
     * @param objects 需要打印的对象
     */
    public static void a(String tag, Object... objects) {
        printLog(A, tag, objects);
    }

    public static void json(String jsonFormat) {
        printLog(JSON, null, jsonFormat);
    }

    public static void json(String tag, String jsonFormat) {
        printLog(JSON, tag, jsonFormat);
    }

    public static void file(File targetDirectory, Object msg) {
        printFile(null, targetDirectory, null, msg);
    }

    public static void file(String tag, File targetDirectory, Object msg) {
        printFile(tag, targetDirectory, null, msg);
    }

    public static void file(String tag, File targetDirectory, String fileName, Object msg) {
        printFile(tag, targetDirectory, fileName, msg);
    }

    private static void printLog(int type, String tagStr, Object... objects) {
        if (!IS_SHOW_LOG) {
            return;
        }

        String[] contents = wrapperContent(tagStr, objects);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];

        switch (type) {
            case V:
            case D:
            case I:
            case W:
            case E:
            case A:
                BaseLog.printDefault(type, tag, headString + msg);
                break;
            case JSON:
                JsonLog.printJson(tag, msg, headString);
                break;
            default:
                break;
        }
    }

    private static void printFile(String tagStr, File targetDirectory, String fileName, Object objectMsg) {
        if (!IS_SHOW_LOG) {
            return;
        }
        String[] contents = wrapperContent(tagStr, objectMsg);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];

        FileLog.printFile(tag, targetDirectory, fileName, headString, msg);
    }

    private static String[] wrapperContent(String tagStr, Object... objects) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement targetElement = stackTrace[STACK_TRACE_INDEX];
        String className = targetElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1] + SUFFIX;
        }

        if (className.contains("$")) {
            className = className.split("\\$")[0] + SUFFIX;
        }

        String methodName = targetElement.getMethodName();
        int lineNumber = targetElement.getLineNumber();

        if (lineNumber < 0) {
            lineNumber = 0;
        }
        String methodNameShort = methodName.substring(0, 1).toUpperCase(Locale.CHINA) + methodName.substring(1);
        String tag = (tagStr == null ? className : tagStr);

        if (mIsGlobalTagEmpty && TextUtils.isEmpty(tag)) {
            tag = TAG_DEFAULT;
        } else if (!mIsGlobalTagEmpty) {
            tag = mGlobalTag;
        }

        String msg = (objects == null) ? NULL_TIPS : getObjectsString(objects);
        String headString = "[ (" + className + ":" + lineNumber + ")#" + methodNameShort + " ] ";

        return new String[]{tag, msg, headString};
    }

    private static String getObjectsString(Object... objects) {
        if (objects.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object == null) {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(NULL).append("\n");
                } else {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(object.toString()).append("\n");
                }
            }
            return stringBuilder.toString();
        } else {
            Object object = objects[0];
            return object == null ? NULL : object.toString();
        }
    }

    private static HashMap<String, Long> timeMap = new HashMap<>();

    /**
     * 记录开始时间
     *
     * @param tag
     */
    public static void startRecordTime(String tag) {
        long startTime = System.currentTimeMillis();
        timeMap.put(tag, startTime);
    }

    /**
     * 记录结束时间
     *
     * @param tag
     */
    public static void endRecordTime(String tag) {
        long endTime = System.currentTimeMillis();
        long wasteTime = endTime - timeMap.get(tag);
        timeMap.remove(tag);
        e(tag, "耗时:" + wasteTime / 1000 + "秒" + endTime % 1000);
    }
}