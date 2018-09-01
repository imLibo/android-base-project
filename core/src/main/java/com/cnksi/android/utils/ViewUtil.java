package com.cnksi.android.utils;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.annotation.ColorInt;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;

import com.cnksi.android.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 图片处理
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/8/13
 * @since 1.0
 */
public class ViewUtil {

    /**
     * 设置图片灰色蒙版
     *
     * @param imageView
     */
    public static void grayMaskView(ImageView imageView) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        imageView.setColorFilter(filter);
    }

    /**
     * 计算View的宽度
     *
     * @param widthMeasureSpec
     * @return
     */
    public static int measureWidth(int widthMeasureSpec) {
        int result = 0;
        int specMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int specSize = View.MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 计算View的高度
     *
     * @param heightMeasureSpec
     * @return
     */
    public static int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int specMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int specSize = View.MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 设置控件连续点击
     *
     * @param view
     * @param clickCount
     * @param mOnClickListener
     */
    public static void setFastClickView(View view, int clickCount, final View.OnClickListener mOnClickListener) {
        view.setTag(R.id.first_tag_key, 0L);
        view.setTag(R.id.second_tag_key, 0);
        view.setOnClickListener(v -> {
            long currentTime = Long.parseLong(v.getTag(R.id.first_tag_key).toString());
            int count = Integer.parseInt(v.getTag(R.id.second_tag_key).toString());
            if (currentTime == 0 || System.currentTimeMillis() - currentTime < 1000) {
                v.setTag(R.id.first_tag_key, System.currentTimeMillis());
                v.setTag(R.id.second_tag_key, ++count);
                if (count > clickCount) {
                    v.setTag(R.id.first_tag_key, 0);
                    v.setTag(R.id.second_tag_key, +0);
                    mOnClickListener.onClick(v);
                }
            } else {
                v.setTag(R.id.first_tag_key, 0);
                v.setTag(R.id.second_tag_key, +0);
            }
        });
    }

    /**
     * 设置控件连续点击
     *
     * @param view
     * @param mOnClickListener
     */
    public static void setFastClickView(View view, final View.OnClickListener mOnClickListener) {
        setFastClickView(view, 4, mOnClickListener);
    }

    /**
     * %s为替换符 values为补全的值。
     *
     * @param formatStr %s为替换符
     * @param color     颜色
     * @param values    替换的值
     * @return
     */
    public static CharSequence formatPartTextColor(String formatStr, @ColorInt int color, String... values) {
        String[] s = formatStr.split("%s");
        List<String> str = new ArrayList<>(Arrays.asList(s));
        if (formatStr.endsWith("%s")) {
            str.add("");
        }
        if (str.size() == 1) {
            return formatStr;
        }
        if (str.size() - 1 != values.length) {
            throw new IllegalArgumentException("formatStr需要的参数个数与对应的values不匹配");
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        for (int i = 0, count = str.size(); i < count; i++) {
            spannableStringBuilder.append(str.get(i));
            if (i < count - 1) {
                spannableStringBuilder.append(changeTextColor(values[i], color));
            }
        }
        return spannableStringBuilder;
    }

    /**
     * 更改字体颜色
     *
     * @param content
     * @param color
     * @return
     */
    public static CharSequence changeTextColor(CharSequence content, @ColorInt int color) {
        if (content.length() > 0) {
            return getPartColorBuilder(content, color, 0, content.length());
        } else {
            return "";
        }
    }

    /**
     * 改变部分字体的颜色
     *
     * @param content     需要改变的内容
     * @param color       颜色值
     * @param startOffset 开始位置
     * @param endOffset   结束位置
     * @return
     */
    public static SpannableStringBuilder getPartColorBuilder(CharSequence content, @ColorInt int color, int startOffset, int endOffset) {
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(new ForegroundColorSpan(color), startOffset, endOffset, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

}
