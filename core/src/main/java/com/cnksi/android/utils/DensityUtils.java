package com.cnksi.android.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.support.annotation.NonNull;
import android.widget.TextView;

/**
 * 常用单位转换的辅助类
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/7/17
 * @since 1.0
 */
public class DensityUtils {

    private DensityUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param context
     * @param pxValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(@NonNull Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param context  上下文
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(@NonNull Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context 上下文
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(@NonNull Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context 上下文
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(@NonNull Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 得到字体高度
     *
     * @param paint
     * @return
     */
    public static float getFontHeight(@NonNull Paint paint) {
        FontMetrics fm = paint.getFontMetrics();
        return (float) Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * 得到字体高度
     *
     * @param textView 文本框
     * @return
     */
    public static float getFontHeight(@NonNull TextView textView) {
        Paint paint = textView.getPaint();
        FontMetrics fm = paint.getFontMetrics();
        return (float) Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * 得到文字的长度
     *
     * @param paint   画笔
     * @param content 内容
     * @return
     */
    public static float getFontWidth(@NonNull Paint paint, @NonNull String content) {
        return paint.measureText(content);
    }

    /**
     * 得到文字的长度
     *
     * @param textView 文本框
     * @return
     */
    public static float getFontWidth(@NonNull TextView textView) {
        Paint paint = textView.getPaint();
        return paint.measureText(textView.getText().toString());
    }
}
