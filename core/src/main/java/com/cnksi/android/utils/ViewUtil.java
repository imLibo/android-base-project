package com.cnksi.android.utils;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.widget.ImageView;

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

}
