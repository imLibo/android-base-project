package com.cnksi.android.utils;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.view.Window;
import android.view.WindowManager;

/**
 * 手机状态工具类 主要包括网络、蓝牙、屏幕亮度、飞行模式、音量等
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/7/30
 * @since 1.0
 */
public class DeviceStatusUtil {

    private DeviceStatusUtil() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * 获取系统屏幕亮度模式的状态，需要WRITE_SETTINGS权限
     *
     * @param context 上下文
     * @return System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC：自动；System.
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC
     * ：手动；默认：System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
     */
    public static int getScreenBrightnessModeState(Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    /**
     * 判断系统屏幕亮度模式是否是自动，需要WRITE_SETTINGS权限
     *
     * @param context 上下文
     * @return true：自动；false：手动；默认：true
     */
    public static boolean isScreenBrightnessModeAuto(Context context) {
        return getScreenBrightnessModeState(context) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
    }

    /**
     * 设置系统屏幕亮度模式，需要WRITE_SETTINGS权限
     *
     * @param context 上下文
     * @param auto    自动
     * @return 是否设置成功
     */
    public static boolean setScreenBrightnessMode(Context context, boolean auto) {
        boolean result = true;
        if (isScreenBrightnessModeAuto(context) != auto) {
            result = Settings.System.putInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    auto ? Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
                            : Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        }
        return result;
    }

    /**
     * 获取系统亮度，需要WRITE_SETTINGS权限
     *
     * @param context 上下文
     * @return 亮度，范围是0-255；默认255
     */
    public static int getScreenBrightness(Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, 255);
    }

    /**
     * 设置系统亮度（此方法只是更改了系统的亮度属性，并不能看到效果。要想看到效果可以使用setWindowBrightness()方法设置窗口的亮度），
     * 需要WRITE_SETTINGS权限
     *
     * @param context          上下文
     * @param screenBrightness 亮度，范围是0-255
     * @return 设置是否成功
     */
    public static boolean setScreenBrightness(Context context, int screenBrightness) {
        int brightness = screenBrightness;
        if (screenBrightness < 1) {
            brightness = 1;
        } else if (screenBrightness > 255) {
            brightness = screenBrightness % 255;
            if (brightness == 0) {
                brightness = 255;
            }
        }
        return Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
    }

    /**
     * 设置给定Activity的窗口的亮度（可以看到效果，但系统的亮度属性不会改变）
     *
     * @param activity         要通过此Activity来设置窗口的亮度
     * @param screenBrightness 亮度，范围是0-255
     */
    public static void setWindowBrightness(Activity activity, float screenBrightness) {
        float brightness = screenBrightness;
        if (screenBrightness < 1) {
            brightness = 1;
        } else if (screenBrightness > 255) {
            brightness = screenBrightness % 255;
            if (brightness == 0) {
                brightness = 255;
            }
        }
        Window window = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = window.getAttributes();
        localLayoutParams.screenBrightness = brightness / 255;
        window.setAttributes(localLayoutParams);
    }

    /**
     * 设置系统亮度并实时可以看到效果，需要WRITE_SETTINGS权限
     *
     * @param activity         要通过此Activity来设置窗口的亮度
     * @param screenBrightness 亮度，范围是0-255
     * @return 设置是否成功
     */
    public static boolean setScreenBrightnessAndApply(Activity activity, int screenBrightness) {
        boolean result = setScreenBrightness(activity, screenBrightness);
        if (result) {
            setWindowBrightness(activity, screenBrightness);
        }
        return result;
    }

    /**
     * 获取屏幕休眠时间，需要WRITE_SETTINGS权限
     *
     * @param context 上下文
     * @return 屏幕休眠时间，单位毫秒，默认30000
     */
    public static int getScreenDormantTime(Context context) {
        return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 30000);
    }

    /**
     * 设置屏幕休眠时间，需要WRITE_SETTINGS权限
     *
     * @param context 上下文
     * @return 设置是否成功
     */
    public static boolean setScreenDormantTime(Context context, int millis) {
        return Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, millis);
    }

    /**
     * 获取飞行模式的状态，需要WRITE_APN_SETTINGS权限
     *
     * @param context 上下文
     * @return 1：打开；0：关闭；默认：关闭
     */
    public static int getAirplaneModeState(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0);
    }

    /**
     * 判断飞行模式是否打开，需要WRITE_APN_SETTINGS权限
     *
     * @param context 上下文
     * @return true：打开；false：关闭；默认关闭
     */
    public static boolean isAirplaneModeOpen(Context context) {
        return getAirplaneModeState(context) == 1;
    }

    /**
     * 设置飞行模式的状态，需要WRITE_APN_SETTINGS权限
     *
     * @param context 上下文
     * @param enable  飞行模式的状态
     * @return 设置是否成功
     */
    public static boolean setAirplaneMode(Context context, boolean enable) {
        boolean result = true;
        // 如果飞行模式当前的状态与要设置的状态不一样
        if (isAirplaneModeOpen(context) != enable) {
            result = Settings.Global.putInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, enable ? 1 : 0);
            // 发送飞行模式已经改变广播
            context.sendBroadcast(new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED));
        }
        return result;
    }

    /**
     * 获取蓝牙的状态
     *
     * @return 取值为BluetoothAdapter的四个静态字段：STATE_OFF, STATE_TURNING_OFF,
     * STATE_ON, STATE_TURNING_ON
     * @throws Exception 没有找到蓝牙设备
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public static int getBluetoothState() throws Exception {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            throw new Exception("bluetooth device not found!");
        } else {
            return bluetoothAdapter.getState();
        }
    }

    /**
     * 判断蓝牙是否打开
     *
     * @return true：已经打开或者正在打开；false：已经关闭或者正在关闭
     * 没有找到蓝牙设备
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public static boolean isBluetoothOpen() throws Exception {
        int bluetoothStateCode = getBluetoothState();
        return bluetoothStateCode == BluetoothAdapter.STATE_ON || bluetoothStateCode == BluetoothAdapter.STATE_TURNING_ON;
    }

    /**
     * 设置蓝牙状态
     *
     * @param enable 打开
     *               没有找到蓝牙设备
     */
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN})
    public static void setBluetooth(boolean enable) throws Exception {
        // 如果当前蓝牙的状态与要设置的状态不一样
        if (isBluetoothOpen() != enable) {
            // 如果是要打开就打开，否则关闭
            if (enable) {
                BluetoothAdapter.getDefaultAdapter().enable();
            } else {
                BluetoothAdapter.getDefaultAdapter().disable();
            }
        }
    }

    /**
     * 获取铃声音量，需要WRITE_APN_SETTINGS权限
     *
     * @param context 上下文
     * @return 铃声音量，取值范围为0-7；默认为0
     */
    public static int getRingVolume(Context context) {
        return ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).getStreamVolume(AudioManager.STREAM_RING);
    }

    /**
     * 获取媒体音量
     *
     * @param context 上下文
     * @return 媒体音量，取值范围为0-7
     */
    public static void setRingVolume(Context context, int ringVloume) {
        if (ringVloume < 0) {
            ringVloume = 0;
        } else if (ringVloume > 7) {
            ringVloume = ringVloume % 7;
            if (ringVloume == 0) {
                ringVloume = 7;
            }
        }
        AudioManager manager = ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE));
        if (manager != null) {
            manager.setStreamVolume(AudioManager.STREAM_RING, ringVloume, AudioManager.FLAG_PLAY_SOUND);
        }
    }
}
