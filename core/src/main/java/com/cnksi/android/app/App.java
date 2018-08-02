package com.cnksi.android.app;

import android.app.Application;

import com.cnksi.android.utils.PreferencesUtil;

/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/8/1
 * @since 1.0
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initUtil();
    }

    protected void initUtil() {
        PreferencesUtil.init(this.getApplicationContext());
    }
}
