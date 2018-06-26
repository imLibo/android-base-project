package com.cnksi.android.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.cnksi.android.log.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * App相关的类
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/6/26
 * @since 1.0
 */
public final class AppUtils {

    private AppUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取系统安装的所有应用程序的
     *
     * @param context 上下文
     * @return 所有的AppInfo
     */
    public static List<AppInfo> getInstalledAppInfo(Context context) {
        ArrayList<AppInfo> appList = new ArrayList<>();
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            AppInfo tmpInfo = new AppInfo();
            tmpInfo.appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
            tmpInfo.packageName = packageInfo.packageName;
            tmpInfo.versionName = packageInfo.versionName;
            tmpInfo.versionCode = packageInfo.versionCode;
            tmpInfo.activities = packageInfo.activities;
            tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(context.getPackageManager());
            appList.add(tmpInfo);
        }
        return appList;
    }

    /**
     * 获取应用程序名称
     *
     * @param context 上下文
     * @return 应用名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            Logger.e(e);
        }
        return "";
    }

    /**
     * 获取应用程序版本名称信息
     *
     * @param context 上下文
     * @return 当前应用的版本名称 versionName
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            Logger.e(e);
        }
        return "";
    }


    /**
     * 获取应用程序版本号
     *
     * @param context 上下文
     * @return 当前应用的版本号 versionCode
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            Logger.e(e);
        }
        return 0;
    }

    /**
     * 获取APK包的信息
     *
     * @param context 上下文
     * @param file    apk文件
     * @return apk的PackageInfo信息
     */
    public static PackageInfo getApkPackageInfo(Context context, File file) {
        PackageInfo packageInfo = null;
        try {
            if (file != null && file.exists()) {
                packageInfo = context.getPackageManager().getPackageArchiveInfo(file.getAbsolutePath(), 0);
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        return packageInfo;
    }

    /**
     * 获取本程序的程序信息
     *
     * @param context 上下文
     * @return 本程序的PackageInfo
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            Logger.e(e);
        }
        return packageInfo;
    }

    /**
     * 获取已安装的apk的packageInfo
     *
     * @param context     上下文
     * @param packageName 应用程序包名
     * @return 安装程序的PackageInfo
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            Logger.e(e);
        }
        return packageInfo;
    }

    /**
     * 检查某个APP是否安装
     *
     * @param context     上下文
     * @param packageName 应用程序包名
     * @return true or false
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (NameNotFoundException e) {
            Logger.e(e);
            installed = false;
        }
        return installed;
    }


    /**
     * 判断某个Service是否正在运行
     *
     * @param context          上下文
     * @param serviceClassName service的类名
     * @return true 正在运行 false 未运行
     */
    public static boolean isServiceRunning(Context context, String serviceClassName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<RunningServiceInfo> runningService = activityManager.getRunningServices(Integer.MAX_VALUE);
            for (RunningServiceInfo runServiceInfo : runningService) {
                if (serviceClassName.equals(runServiceInfo.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    static class AppInfo {
        String appName = "";
        String packageName = "";
        String versionName = "";
        int versionCode = 0;
        ActivityInfo[] activities;
        Drawable appIcon;
    }

    /**
     * 验证apk签名是否和本程序签名一致
     *
     * @param context  上下文
     * @param filePath apk文件绝对路径
     * @return true 一致  false 不一致
     */
    public static boolean verSignature(Context context, String filePath) {
        boolean isVer = false;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            int hashode = sign.hashCode();
            String md5 = com.cnksi.android.utils.MD5.getMessageDigest(sign.toByteArray());

            packageInfo = context.getPackageManager().getPackageArchiveInfo(filePath, PackageManager.GET_SIGNATURES);
            signs = packageInfo.signatures;
            sign = signs[0];
            int hashCodeApk = sign.hashCode();
            String md5Str = com.cnksi.android.utils.MD5.getMessageDigest(sign.toByteArray());
            isVer = md5.equals(md5Str) && (hashode == hashCodeApk);
        } catch (Exception e) {
            Logger.e(e);
        }
        return isVer;
    }

    /**
     * 获取第三方应用的签名信息
     *
     * @param context     上下文
     * @param packageName 应用程序包名
     * @return 应用程序签名
     */
    public static String getSignature(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            Logger.e("获取签名失败，包名为 null");
            return "";
        }
        PackageManager localPackageManager = context.getPackageManager();
        PackageInfo localPackageInfo;
        try {
            localPackageInfo = localPackageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            if (localPackageInfo == null) {
                Logger.e("信息为 null, 包名=" + packageName);
                return "";
            }
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            Logger.e("包名没有找到...");
            return "";
        }
        Signature[] arrayOfSignature = localPackageInfo.signatures;
        if ((arrayOfSignature == null) || (arrayOfSignature.length == 0)) {
            Logger.e("signature is null");
            return "";
        }
        return com.cnksi.android.utils.MD5.getMessageDigest(arrayOfSignature[0].toByteArray());
    }

}