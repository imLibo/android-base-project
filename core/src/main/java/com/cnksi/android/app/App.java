package com.cnksi.android.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.CallSuper;

import com.cnksi.android.crash.SpiderMan;
import com.cnksi.android.utils.Utils;


/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/8/1
 * @since 1.0
 */
public class App extends Application {

    private static App mApp;

    public static App getApp() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initUtil(mApp = this);
    }

    @CallSuper
    protected void initUtil(Context context) {
        Utils.init(context);
        SpiderMan.getInstance()
                .init(context)
                //设置是否捕获异常，不弹出崩溃框
                .setEnable(true)
                //设置是否显示崩溃信息展示页面
                .showCrashMessage(true)
                //设置崩溃日志存放路径
                .setCrashLogFolder(getCrashLogFolder())
                //是否回调异常信息，友盟等第三方崩溃信息收集平台会用到,
                .setOnCrashListener(getCrashListener());
    }

    /**
     * 设置崩溃回调
     */
    protected SpiderMan.OnCrashListener getCrashListener() {
        return null;
    }

    /**
     * 设置崩溃日志存放文件夹
     *
     * @return
     */
    protected String getCrashLogFolder() {
        return null;
    }
}
