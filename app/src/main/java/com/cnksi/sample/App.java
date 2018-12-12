package com.cnksi.sample;

import android.os.Environment;

import com.cnksi.android.https.glide.OkHttpLibraryGlideModule;
import com.cnksi.sample.utils.Utils;

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
        OkHttpLibraryGlideModule.mOkHttpClient = Utils.INSTANCE.getOkHttpClient(this);
//
    }

    @Override
    protected String getCrashLogFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }


}
