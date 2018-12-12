package com.cnksi.android.encrypt.weixin.aes;

import com.cnksi.android.BuildConfig;
import com.cnksi.android.log.KLog;

import java.util.Random;

/**
 * 微信Aes加解密工具
 *
 * @author Oliver
 * @version 1.0
 * @date 2017/4/8 11:51
 * @since 1.0
 */
public class WxAes {

    public static final String MP_APP_ID = reverse(BuildConfig.MP_APP_ID, 6, 2);
    public static final String MP_TOKEN = reverse(BuildConfig.MP_TOKEN, 8, 4);
    public static final String MP_ENCODINGAES_KEY = reverse(BuildConfig.MP_ENCODINGAES_KEY, 12, 6);

    /**
     * 得到加密后的消息字符串
     *
     * @param message
     * @return
     */
    public static String getEncryptMsg(String message) {
        return getEncryptMsg(message, WxAes.MP_TOKEN, WxAes.MP_ENCODINGAES_KEY, WxAes.MP_APP_ID);
    }

    /**
     * 得到加密后的消息字符串
     *
     * @param message
     * @param token
     * @param encodingAesKey
     * @param appId
     * @return
     */
    public static String getEncryptMsg(String message, String token, String encodingAesKey, String appId) {
        String encryptMsg = "";
        try {
            //消息体加密
            final String nonce = getRandomString(16);
            WXBizMsgCrypt crypt = new WXBizMsgCrypt(token, encodingAesKey, appId);
            encryptMsg = crypt.encrypt(nonce, message);
        } catch (AesException e) {
            KLog.e("WxAES", e.getMessage());
        }
        return encryptMsg;
    }


    /**
     * 解密消息
     *
     * @param message
     * @return
     */
    public static String getDecryptMsg(String message) {
        String decryptMsg = "";
        try {
            WXBizMsgCrypt crypt = new WXBizMsgCrypt(WxAes.MP_TOKEN, WxAes.MP_ENCODINGAES_KEY, WxAes.MP_APP_ID);
            decryptMsg = crypt.decrypt(message);
        } catch (AesException e) {
            KLog.e("WxAES", e.getMessage());
        }
        return decryptMsg;
    }

    /**
     * 解密消息
     *
     * @param message
     * @param token
     * @param encodingAesKey
     * @param appId
     * @return
     */
    public static String getDecryptMsg(String message, String token, String encodingAesKey, String appId) {
        String decryptMsg = "";
        try {
            WXBizMsgCrypt crypt = new WXBizMsgCrypt(token, encodingAesKey, appId);
            decryptMsg = crypt.decrypt(message);
        } catch (AesException e) {
            KLog.e("WxAES", e.getMessage());
        }
        return decryptMsg;
    }

    /**
     * 获取随机字符串
     *
     * @param length
     * @return
     */
    private static String getRandomString(int length) {
        //含有字符和数字的字符串
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        //随机类初始化
        Random random = new Random();
        //StringBuffer类生成，为了拼接字符串
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            // [0,62)
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 解密
     *
     * @param content
     * @param index
     * @param length
     * @return
     */
    private static String reverse(String content, int index, int length) {
        String resultString = "";
        char[] charArray = content.toCharArray();
        for (int i = charArray.length - 1; i >= 0; i--) {
            resultString += charArray[i];
        }
        String str1 = resultString.substring(0, index);
        String str2 = resultString.substring(index + length, resultString.length() - length - 1);
        resultString = str1 + str2;
        return resultString;
    }

}
