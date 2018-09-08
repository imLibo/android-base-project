package com.cnksi.android.crash;

import android.content.Context;
import android.content.Intent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 崩溃日志收集
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/9/8
 * @since 1.0
 */
public class SpiderMan implements Thread.UncaughtExceptionHandler {

    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    private Context mContext;
    private Thread.UncaughtExceptionHandler mExceptionHandler;

    private Builder mBuilder;

    private SpiderMan() {
    }

    private static class SingletonInstance {
        private static final SpiderMan INSTANCE = new SpiderMan();
    }

    public static SpiderMan getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public Builder init(Context context) {
        this.mContext = context.getApplicationContext();
        mExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mBuilder = new Builder();
        return mBuilder;
    }

    @Override
    public void uncaughtException(Thread t, Throwable ex) {
        if (mBuilder == null) {
            return;
        }
        CrashModel model = parseCrash(ex);
        if (mBuilder.mOnCrashListener != null) {
            mBuilder.mOnCrashListener.onCrash(t, ex, model);
        }
        if (mBuilder.mEnable) {
            handleException(model);
        } else {
            if (mExceptionHandler != null) {
                mExceptionHandler.uncaughtException(t, ex);
            }
        }
    }

    private void handleException(CrashModel model) {
        if (mBuilder.mEnable && mBuilder.mShowCrashMessage) {
            Intent intent = new Intent(mContext, CoreCrashActivity.class);
            intent.putExtra(CoreCrashActivity.CRASH_MODEL, model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public interface OnCrashListener {
        void onCrash(Thread t, Throwable ex, CrashModel model);
    }

    private CrashModel parseCrash(Throwable ex) {
        CrashModel model = new CrashModel();
        model.setEx(ex);
        model.setTime(System.currentTimeMillis());
        String exceptionMsg;
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        pw.flush();
        String exceptionType = ex.getClass().getName();
        exceptionMsg = ex.getMessage();
        if (ex.getStackTrace() != null && ex.getStackTrace().length > 0) {
            StackTraceElement element = ex.getStackTrace()[0];
            model.setExceptionMsg(exceptionMsg);
            model.setLineNumber(element.getLineNumber());
            model.setClassName(element.getClassName());
            model.setFileName(element.getFileName());
            model.setMethodName(element.getMethodName());
            model.setExceptionType(exceptionType);
        }
        model.setFullException(sw.toString());
        return model;
    }

    public static String getShareText(CrashModel model) {
        StringBuilder builder = new StringBuilder();

        builder.append("崩溃信息:")
                .append("\n")
                .append(model.getExceptionMsg())
                .append("\n");
        builder.append("\n");

        builder.append("类名:")
                .append(model.getFileName()).append("\n");
        builder.append("\n");

        builder.append("方法:").append(model.getMethodName()).append("\n");
        builder.append("\n");

        builder.append("行数:").append(model.getLineNumber()).append("\n");
        builder.append("\n");

        builder.append("类型:").append(model.getExceptionType()).append("\n");
        builder.append("\n");

        builder.append("时间").append(df.format(model.getTime())).append("\n");
        builder.append("\n");

        builder.append("设备名称:").append(model.getDevice().getModel()).append("\n");
        builder.append("\n");

        builder.append("设备厂商:").append(model.getDevice().getBrand()).append("\n");
        builder.append("\n");

        builder.append("系统版本:").append(model.getDevice().getVersion()).append("\n");
        builder.append("\n");

        builder.append("全部信息:")
                .append("\n")
                .append(model.getFullException()).append("\n");

        return builder.toString();
    }

    public class Builder {

        private boolean mEnable;
        private boolean mShowCrashMessage;
        private OnCrashListener mOnCrashListener;

        public Builder setEnable(boolean enable) {
            this.mEnable = enable;
            return this;
        }

        public Builder showCrashMessage(boolean show) {
            this.mShowCrashMessage = show;
            return this;
        }

        public void setOnCrashListener(OnCrashListener listener) {
            this.mOnCrashListener = listener;
        }
    }
}