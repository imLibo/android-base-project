package com.cnksi.android.executor;

import android.app.Activity;

import java.util.Stack;

/**
 * activity管理工具类
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/9/10
 * @since 1.0
 */
public class ActivityManager {
    private static Stack<Activity> stack;

    private ActivityManager() {
        stack = new Stack<>();
    }

    private static class SingletonInstance {
        private static final ActivityManager INSTANCE = new ActivityManager();
    }

    public static ActivityManager instance() {
        return SingletonInstance.INSTANCE;
    }

    /**
     * 退出栈顶Activity
     */
    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            stack.remove(activity);
        }
    }

    /**
     * 获得当前栈顶Activity
     */
    public Activity currentActivity() {
        Activity activity = null;
        if (stack != null && stack.size() > 0) {
            activity = stack.lastElement();
        }
        return activity;
    }

    /**
     * 将当前Activity推入栈中
     */
    public void pushActivity(Activity activity) {
        if (stack == null) {
            stack = new Stack<>();
        }
        stack.add(activity);
    }

    /**
     * 退出栈中所有Activity
     */
    public void popAllActivityExcept(Class<?> cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }

    /**
     * 栈中是否存在某个activity
     */
    public boolean hasActivity(Class<?> cls) {
        boolean flag = false;
        if (stack != null && !stack.isEmpty()) {
            for (Activity tempActivity : stack) {
                if (tempActivity.getClass().equals(cls)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 弹出某个activity
     */
    public void popActivity(Class<?> cls) {
        if (stack != null && !stack.isEmpty()) {
            for (Activity activity : stack) {
                if (cls.equals(activity.getClass())) {
                    popActivity(activity);
                    break;
                }
            }
        }
    }

    /**
     * 弹出几个actvity
     */
    public void popActivityList(Class<?>... clsList) {
        if (stack != null && !stack.isEmpty()) {
            for (Class<?> cls : clsList) {
                popActivity(cls);
            }
        }
    }
}