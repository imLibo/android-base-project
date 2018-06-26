package com.cnksi.android.log;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @since 1.0
 * created at 2018/4/10
 */
class JsonLog extends BaseLog {

    /**
     * 打印json格式的日志
     *
     * @param tag        TAG
     * @param msg        日志内容
     * @param headString 日志头
     */
    static void printJson(String tag, String msg, String headString) {
        String message;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(com.cnksi.android.log.Logger.JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(com.cnksi.android.log.Logger.JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }
        printLine(tag, true);
        message = headString + com.cnksi.android.log.Logger.LINE_SEPARATOR + message;
        String[] lines = message.split(com.cnksi.android.log.Logger.LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(tag, "║ " + line);
        }
        printLine(tag, false);
    }
}
