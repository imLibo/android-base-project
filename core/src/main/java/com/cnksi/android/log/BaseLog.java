package com.cnksi.android.log;

import android.text.TextUtils;
import android.util.Log;

/**
 *
 * @author lyongfly
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/6/26
 * @version 1.0
 * @since 1.0
 */
class BaseLog {

    /**
     * 打印日志
     *
     * @param type 类型
     * @param tag  Tag
     * @param msg  日志内容
     */
    static void printDefault(int type, String tag, String msg) {
        int index = 0;
        int maxLength = 4000;
        int countOfSub = msg.length() / maxLength;

        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + maxLength);
                printSub(type, tag, sub);
                index += maxLength;
            }
            printSub(type, tag, msg.substring(index, msg.length()));
        } else {
            printSub(type, tag, msg);
        }
    }

    private static void printSub(int type, String tag, String sub) {
        switch (type) {
            case com.cnksi.android.log.Logger.V:
                Log.v(tag, sub);
                break;
            case com.cnksi.android.log.Logger.D:
                Log.d(tag, sub);
                break;
            case com.cnksi.android.log.Logger.I:
                Log.i(tag, sub);
                break;
            case com.cnksi.android.log.Logger.W:
                Log.w(tag, sub);
                break;
            case com.cnksi.android.log.Logger.E:
                Log.e(tag, sub);
                break;
            case com.cnksi.android.log.Logger.A:
                Log.wtf(tag, sub);
                break;
            default:
                break;
        }
    }

    static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || line.equals("\n") || line.equals("\t") || TextUtils.isEmpty(line.trim());
    }

    static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }

}
