package com.cnksi.android.constants;

import android.os.Environment;

/**
 * 常量类
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/7/17
 * @since 1.0
 */
public interface Constants {

    /**
     * 手机存储目录
     */
    String PHONE_DIRECTORY = Environment.getDataDirectory().getAbsolutePath();
    /**
     * SDCard目录
     */
    String SDCARD_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath();
    /**
     * 音频后缀
     */
    String AAC_POSTFIX = ".aac";
    /**
     *
     */
    String MP3_POSTFIX = ".mp3";
    /**
     * jpg图片后缀名 .jpg
     */
    String JPG_POSTFIX = ".jpg";
    /**
     * png图片后缀名 .png
     */
    String PNG_POSTFIX = ".png";
    /**
     * 日志后缀名 .txt
     */
    String TXT_POSTFIX = ".txt";
    /**
     * apk后缀名 .apk
     */
    String APK_POSTFIX = ".apk";
}
