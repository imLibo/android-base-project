package com.cnksi.android.utils;

import android.content.Context;

import com.cnksi.android.BuildConfig;
import com.cnksi.android.log.KLog;

import es.dmoral.toasty.Toasty;

/**
 * Utils初始化相关
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/10/11
 * @since 1.0
 */
public class Utils {

    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
        PreferencesUtil.init(getContext());
        KLog.init(BuildConfig.DEBUG);
        Toasty.Config.getInstance().apply();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("u should init first");
    }
}