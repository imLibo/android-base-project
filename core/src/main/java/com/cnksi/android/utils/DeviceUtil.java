package com.cnksi.android.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.cnksi.android.app.App;
import com.cnksi.android.log.KLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
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
public class DeviceUtil {

    /**
     * 返回设备的唯一Id DeviceId + AndroidID + SerialNumber + InstalltionId
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
        public String lastvist = DateUtil.getCurrentLongTime();

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


    /**
     * 判断设备是否root
     *
     * @return the boolean{@code true}: 是<br>{@code false}: 否
     */
    public static boolean isDeviceRooted() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/", "/system/bin/failsafe/",
                "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取设备系统版本号
     *
     * @return 设备系统版本号
     */
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }


    /**
     * 获取设备AndroidID
     *
     * @return AndroidID
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidID() {
        return Settings.Secure.getString(Utils.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取设备MAC地址
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     *
     * @return MAC地址
     */
    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    public static String getMacAddress() {
        String macAddress = getMacAddressByWifiInfo();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        macAddress = getMacAddressByNetworkInterface();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        macAddress = getMacAddressByFile();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        return "please open wifi";
    }

    /**
     * 获取设备MAC地址
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
     *
     * @return MAC地址
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    private static String getMacAddressByWifiInfo() {
        try {
            WifiManager wifi = (WifiManager) Utils.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                WifiInfo info = wifi.getConnectionInfo();
                if (info != null) {
                    return info.getMacAddress();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 获取设备MAC地址
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     *
     * @return MAC地址
     */
    private static String getMacAddressByNetworkInterface() {
        try {
            List<NetworkInterface> nis = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : nis) {
                if (!ni.getName().equalsIgnoreCase("wlan0")) {
                    continue;
                }
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02x:", b));
                    }
                    return res1.deleteCharAt(res1.length() - 1).toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 获取设备MAC地址
     *
     * @return MAC地址
     */
    private static String getMacAddressByFile() {
        ShellUtil.CommandResult result = ShellUtil.execCmd("getprop wifi.interface", false);
        if (result.result == 0) {
            String name = result.successMsg;
            if (name != null) {
                result = ShellUtil.execCmd("cat /sys/class/net/" + name + "/address", false);
                if (result.result == 0) {
                    if (result.successMsg != null) {
                        return result.successMsg;
                    }
                }
            }
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 获取设备厂商
     * <p>如Xiaomi</p>
     *
     * @return 设备厂商
     */

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取设备型号
     * <p>如MI2SC</p>
     *
     * @return 设备型号
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * 关机
     * <p>需要root权限或者系统权限 {@code <android:sharedUserId="android.uid.system"/>}</p>
     */
    public static void shutdown() {
        ShellUtil.execCmd("reboot -p", true);
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getContext().startActivity(intent);
    }

    /**
     * 重启
     * <p>需要root权限或者系统权限 {@code <android:sharedUserId="android.uid.system"/>}</p>
     */
    public static void reboot() {
        ShellUtil.execCmd("reboot", true);
        Intent intent = new Intent(Intent.ACTION_REBOOT);
        intent.putExtra("nowait", 1);
        intent.putExtra("interval", 1);
        intent.putExtra("window", 0);
        Utils.getContext().sendBroadcast(intent);
    }

    /**
     * 重启
     * <p>需系统权限 {@code <android:sharedUserId="android.uid.system"/>}</p>
     *
     * @param reason 传递给内核来请求特殊的引导模式，如"recovery"
     */
    public static void reboot(String reason) {
        PowerManager mPowerManager = (PowerManager) Utils.getContext().getSystemService(Context.POWER_SERVICE);
        try {
            mPowerManager.reboot(reason);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重启到recovery
     * <p>需要root权限</p>
     */
    public static void reboot2Recovery() {
        ShellUtil.execCmd("reboot recovery", true);
    }

    /**
     * 重启到bootloader
     * <p>需要root权限</p>
     */
    public static void reboot2Bootloader() {
        ShellUtil.execCmd("reboot bootloader", true);
    }
}
