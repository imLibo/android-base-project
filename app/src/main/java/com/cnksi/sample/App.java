package com.cnksi.sample;

import com.cnksi.android.log.KLog;

/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/7/31
 * @since 1.0
 */
public class App extends com.cnksi.android.app.App {
    @Override
    public void onCreate() {
        super.onCreate();
        KLog.init(BuildConfig.DEBUG);
    }
}
