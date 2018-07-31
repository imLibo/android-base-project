package com.cnksi.sample;

import android.app.Application;

import com.cnksi.android.X;
import com.cnksi.android.log.KLog;

/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/7/31
 * @since 1.0
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        X.Ext.init(this);
        KLog.init(BuildConfig.DEBUG);
    }
}
