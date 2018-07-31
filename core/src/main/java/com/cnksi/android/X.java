package com.cnksi.android;

import android.app.Application;
import android.content.Context;

import com.cnksi.android.utils.PreferencesUtil;

import java.lang.reflect.Method;


/**
 * 需要在在application的onCreate中初始化: X.Ext.init(this);
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/7/31
 * @since 1.0
 */
public final class X {

    private X() {
    }

    public static boolean isDebug() {
        return Ext.debug;
    }

    public static Application App() {
        if (Ext.app == null) {
            try {
                // 在IDE进行布局预览时使用
                Class<?> renderActionClass = Class.forName("com.android.layoutlib.bridge.impl.RenderAction");
                Method method = renderActionClass.getDeclaredMethod("getCurrentContext");
                Context context = (Context) method.invoke(null);
                Ext.app = new MockApplication(context);
            } catch (Throwable ignored) {
                throw new RuntimeException("please invoke X.Ext.init(app) on Application#onCreate()" + " and register your Application in manifest.");
            }
        }
        return Ext.app;
    }

    public static class Ext {
        private static boolean debug;
        private static Application app;

        private Ext() {
        }

        public static void init(Application app) {
            if (Ext.app == null) {
                Ext.app = app;
            }
            PreferencesUtil.init(app,false);
        }

        public static void setDebug(boolean debug) {
            Ext.debug = debug;
        }
    }

    private static class MockApplication extends Application {
        public MockApplication(Context baseContext) {
            this.attachBaseContext(baseContext);
        }
    }


}
