package com.cnksi.android.safe;

import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.EditText;

/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/9/5
 * @since 1.0
 */
public class SafeUtil {
    /**
     * 隐藏电话号码
     *
     * @param phoneNum
     * @return
     */
    public static String hidePhoneNum(String phoneNum) {
        if (!TextUtils.isEmpty(phoneNum)) {
            return phoneNum.replaceAll("(\\d{3})\\d{4}(\\w{4})", "$1****$2");
        }
        return phoneNum;
    }

    /**
     * 隐藏身份证号码
     *
     * @param idcard
     * @return
     */
    public static String hidePhoneIdCard(String idcard) {
        if (!TextUtils.isEmpty(idcard)) {
            idcard = idcard.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1*****$2");
        }
        return idcard;
    }

    /**
     * 允许输入 字母 数字 汉字
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigitOrChinese(String str) {
        return isLetterDigitOrChinese(str, "");
    }

    /**
     * 允许输入 字母 数字 汉字
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigitOrChinese(String str, String regex) {
        if (TextUtils.isEmpty(regex)) {
            regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";
        }
        return str.matches(regex);
    }

    /**
     * 允许输入 字符串
     *
     * @param str
     * @return
     */
    public static boolean allowSymbol(String str) {
        return allowSymbol(str, "");
    }

    /**
     * 允许输入 字符串
     *
     * @param str
     * @return
     */
    public static boolean allowSymbol(String str, String regex) {
        if (TextUtils.isEmpty(regex)) {
            regex = "[，、。：“”！？（） \\- . ]";
        }
        return str.matches(regex);
    }

    public static void setEditTextFilter(EditText mEditText) {
        setEditTextFilter(mEditText, "", "");
    }

    /**
     * 设置文本框过滤器
     *
     * @param mEditText 文本框
     */
    public static void setEditTextFilter(EditText mEditText, String symbolRegex, String textRegex) {
        InputFilter[] filters = mEditText.getFilters();
        InputFilter[] inputFilters = new InputFilter[filters.length + 1];
        System.arraycopy(filters, 0, inputFilters, 0, filters.length);

        inputFilters[filters.length] = (source, start, end, dest, dstart, dend) -> {
            boolean isAllowSymbol = SafeUtil.allowSymbol(source.toString(), symbolRegex);
            boolean isAllowLetter = SafeUtil.isLetterDigitOrChinese(source.toString(), textRegex);

            if (isAllowSymbol || isAllowLetter) {
                return null;
            } else {
                return "";
            }
        };
        mEditText.setFilters(inputFilters);
    }
}
