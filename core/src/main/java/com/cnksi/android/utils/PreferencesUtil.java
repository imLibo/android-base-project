package com.cnksi.android.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.cnksi.android.BuildConfig;
import com.cnksi.android.encrypt.DESUtil;
import com.cnksi.android.encrypt.MD5Util;

import java.util.HashSet;
import java.util.Set;

import javax.crypto.Cipher;

/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/7/31
 * @since 1.0
 */
public class PreferencesUtil {

    private static final String SECURITY_KEY = "cnksi.com";

    private static String defaultName = PreferencesUtil.class.getSimpleName();

    private static SharedPreferences mPreferences;

    private static boolean isEncrypt = !BuildConfig.DEBUG;

    public static void init(Context mContext) {
        init(mContext, defaultName, true);
    }

    public static void init(Context mContext, boolean encrypt) {
        init(mContext, defaultName, encrypt);
    }

    public static void init(Context mContext, String name, boolean encrypt) {
        isEncrypt = encrypt;
        mPreferences = mContext.getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /*********************************getValue start****************************************/

    public static boolean get(String key, boolean defValue) {
        return Boolean.parseBoolean(String.valueOf(getValue(key, defValue)));
    }

    public static int get(String key, int defValue) {
        return Integer.parseInt(String.valueOf(getValue(key, defValue)));
    }

    public static float get(String key, float defValue) {
        return Float.parseFloat(String.valueOf(getValue(key, defValue)));
    }

    public static long get(String key, long defValue) {
        return Long.parseLong(String.valueOf(getValue(key, defValue)));
    }

    public static String get(String key, String defValue) {
        return String.valueOf(getValue(key, defValue));
    }

    public static Set<String> get(String key, Set<String> defValue) {
        return (Set<String>) getValue(key, defValue);
    }

    private static Object getValue(String key, Object defValue) {
        try {
            if (defValue instanceof Set) {
                Set<String> valueSet = mPreferences.getStringSet(isEncrypt ? MD5Util.md5(key) : key, (Set<String>) defValue);
                Set<String> tempValueSet = new HashSet<>();
                for (String s : valueSet) {
                    tempValueSet.add(DESUtil.des(s, SECURITY_KEY, Cipher.DECRYPT_MODE));
                }
                return tempValueSet;
            } else {
                String value = mPreferences.getString(isEncrypt ? MD5Util.md5(key) : key, String.valueOf(defValue));
                if (value.equals(String.valueOf(defValue))) {
                    return value;
                } else {
                    return (isEncrypt ? DESUtil.des(value, SECURITY_KEY, Cipher.DECRYPT_MODE) : value);
                }
            }
        } catch (Exception e) {
            return defValue;
        }
    }

    /*********************************getValue end****************************************/


    /**********************************putValue start******************************************/

    public static void put(String key, boolean value) {
        putValue(key, value);
    }

    public static void put(String key, int value) {
        putValue(key, value);
    }

    public static void put(String key, float value) {
        putValue(key, value);
    }

    public static void put(String key, long value) {
        putValue(key, value);
    }

    public static void put(String key, String value) {
        putValue(key, value);
    }

    public static void put(final String key, Set<String> value) {
        putValue(key, value);
    }

    private static void putValue(String key, Object value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        key = isEncrypt ? MD5Util.md5(key) : key;
        if (isEncrypt && !(value instanceof Set)) {
            editor.putString(key, DESUtil.des(String.valueOf(value), SECURITY_KEY, Cipher.ENCRYPT_MODE));
        } else {
            if (value instanceof Boolean) {
                editor.putBoolean(key, Boolean.parseBoolean(String.valueOf(value)));
            } else if (value instanceof Float) {
                editor.putFloat(key, Float.parseFloat(String.valueOf(value)));
            } else if (value instanceof Integer) {
                editor.putInt(key, Integer.parseInt(String.valueOf(value)));
            } else if (value instanceof Long) {
                editor.putLong(key, Long.parseLong(String.valueOf(value)));
            } else if (value instanceof String) {
                editor.putString(key, String.valueOf(value));
            } else if (value instanceof Set) {
                if (isEncrypt) {
                    Set<String> sets = (Set<String>) value;
                    Set<String> tempSets = new HashSet<>();
                    for (String s : sets) {
                        tempSets.add(DESUtil.des(String.valueOf(s), SECURITY_KEY, Cipher.ENCRYPT_MODE));
                    }
                    editor.putStringSet(key, tempSets);
                } else {
                    editor.putStringSet(key, (Set<String>) value);
                }
            } else {
                throw new IllegalArgumentException("Value type is not support!");
            }
        }
        editor.apply();
    }

    /**********************************putValue end******************************************/


    /**********************************remove start******************************************/

    public static void remove(String key) {
        mPreferences.edit().remove(isEncrypt ? MD5Util.md5(key) : key).apply();
    }

    public static void clear() {
        mPreferences.edit().clear().apply();
    }
    /**********************************remove end******************************************/
}