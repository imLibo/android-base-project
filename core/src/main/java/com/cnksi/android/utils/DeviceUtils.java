package com.cnksi.android.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.cnksi.android.log.KLog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

/**
 * 获取设备信息的工具类
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/7/17
 * @since 1.0
 */
public class DeviceUtils {

    /**
     * &#x83b7;&#x5f97;&#x552f;&#x4e00;&#x7684;id &#x4e3a;DeviceId + AndroidID + SerialNumber + InstalltionId
     *
     * @param context
     * @return DeviceId + AndroidID + SerialNumber + InstalltionId
     */
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static String getDeviceUUID(Context context) {
        StringBuilder sbUUID = new StringBuilder();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            sbUUID.append(TextUtils.isEmpty(getDeviceId(context)) ? "" : getDeviceId(context));
            sbUUID.append("_");
            sbUUID.append(TextUtils.isEmpty(getAndroidId(context)) ? "" : getAndroidId(context));
            sbUUID.append("_");
            sbUUID.append(TextUtils.isEmpty(getSerialNumber(context)) ? "" : getSerialNumber(context));
        }
        return sbUUID.toString();
    }

    /**
     * 获取手机信息
     *
     * @param context
     * @return
     */
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static DeviceInfor getPhoneInfor(Context context) {
        DeviceInfor deviceInfor = new DeviceInfor();
        // 应用的版本名称和版本号
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            KLog.e(e);
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            deviceInfor.deviceid = getDeviceUUID(context).toUpperCase(Locale.CHINA);
        }
        // 终端程序版本
        deviceInfor.ver = pi == null ? "" : pi.versionName;
        // android版本号
        deviceInfor.android = Build.VERSION.RELEASE + "_" + Build.VERSION.SDK_INT;
        // 手机制造商
        deviceInfor.factory = Build.MANUFACTURER + "_" + Build.PRODUCT;
        // 手机型号
        deviceInfor.model = Build.MODEL;
        // 手机定制商
        deviceInfor.brand = Build.BRAND;
        // 得到系统内存大小
        deviceInfor.memory = getTotalMemorys(context);
        //获取宽高
        String[] widthHeight = getWidthAndHeight(context);
        // 屏幕分辨率
        deviceInfor.resolution = (widthHeight == null || widthHeight.length < 2) ? "" : widthHeight[0];
        // 屏幕密度
        deviceInfor.density = (widthHeight == null || widthHeight.length < 2) ? "" : widthHeight[1];
        // 手机号码
        deviceInfor.phone = getPhoneNum(context);
        return deviceInfor;
    }

    /**
     * 获取手机屏幕高度
     *
     * @param context
     * @return
     */
    public static String[] getWidthAndHeight(Context context) {
        String[] widthHeight = new String[2];
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        float density = dm.density;
        widthHeight[0] = String.valueOf(width) + "*" + String.valueOf(height);
        widthHeight[1] = String.valueOf(density);
        return widthHeight;
    }

    /**
     * 获取手机的内存大小
     *
     * @param context
     * @return
     */
    public static String getTotalMemorys(Context context) {
        long totalMemory = 0L;
        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            totalMemory = Integer.valueOf(arrayOfString[1]) * 1024;
            localBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Formatter.formatFileSize(context, totalMemory);
    }

    /**
     * 获取设备的deviceId
     *
     * @param context
     * @return
     */
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
                return tm.getImei();
            } else {
                return tm.getDeviceId();
            }
        } else {
            throw new RuntimeException("没有读取手机状态的权限");
        }
    }

    /**
     * 获取手机号码
     *
     * @param context
     * @return
     */
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static String getPhoneNum(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            return tm.getLine1Number();
        } else {
            throw new RuntimeException("没有读取手机状态的权限");
        }
    }

    /**
     * 获取AndroidId
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase(Locale.CHINA);
    }

    /**
     * 得到Serial Number android版本2.3及以上
     *
     * @param context
     * @return
     */
    public static String getSerialNumber(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
                return Build.getSerial();
            } else {
                return Build.SERIAL;
            }
        } else {
            throw new RuntimeException("没有读取手机状态的权限");
        }
    }


    public static class DeviceInfor {
        /**
         * 终端设备ID
         */
        public static final String DEVICE_ID = "client.deviceid";
        public String deviceid;

        /**
         * 终端设备品牌
         */
        public static final String BRAND = "client.brand";
        public String brand;

        /**
         * 终端运行内存
         */
        public static final String MEMORY = "client.memory";
        public String memory;

        /**
         * 终端设备型号
         */
        public static final String MODEL = "client.model";
        public String model;

        /**
         * 终端程序版本
         */
        public static final String VER = "client.ver";
        public String ver;

        /**
         * 最后访问时间
         */
        public static final String LASTVIST = "client.lastvist";
        public String lastvist = DateUtils.getCurrentLongTime();

        /**
         * android系统版本
         */
        public static final String ANDROID = "client.android";
        public String android;

        /**
         * 手机厂商
         */
        public static final String FACTORY = "client.factory";
        public String factory;

        /**
         * 电话号码
         */
        public static final String PHONE_NUM = "client.phone";
        public String phone;

        /**
         * 像素密度
         */
        public static final String DENSITY = "client.density";
        public String density;

        /**
         * 屏幕分辨率
         */
        public static final String RESOLUTION = "client.resolution";
        public String resolution;

        /**
         * 经度
         */
        public static final String LONGITUDE = "client.longitude";
        public String longitude;

        /**
         * 纬度
         */
        public static final String LATITUDE = "client.latitude";
        public String latitude;

        /**
         * 位置
         */
        public static final String PLACE = "client.place";
        public String place;
    }

}
