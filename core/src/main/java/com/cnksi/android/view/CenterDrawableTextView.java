package com.cnksi.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * drawableLeft与文本一起居中显示
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/6/28
 * @since 1.0
 */
public class CenterDrawableTextView extends AutoBackgroundButton {

    private static final int DEFAULT_SIZE = 80;

    public CenterDrawableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CenterDrawableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CenterDrawableTextView(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawableLeft = drawables[0];
        float textWidth = getPaint().measureText(getText().toString());
        if (drawableLeft != null && textWidth <= 0) {
            int size = Math.max(Math.max(width, height), DEFAULT_SIZE);
            setMeasuredDimension(size, size);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables[0] != null) {
            setGravity(Gravity.CENTER_VERTICAL);
            setPadding(0, 0, 0, 0);
            //文本的宽度
            float textWidth = getPaint().measureText(getText().toString());
            //drawablePadding值
            int drawablePadding = getCompoundDrawablePadding();
            //icon的宽度
            int drawableWidth = drawables[0].getIntrinsicWidth();
            // icon和文字都居中
            float bodyWidth = textWidth + drawableWidth + drawablePadding;
            //平移
            float translate = (getWidth() - bodyWidth) / 2;
            canvas.translate(translate, 0);
        } else {
            setGravity(Gravity.CENTER);
        }
        super.onDraw(canvas);
    }

    public void setDrawableResource(@DrawableRes int resId) {
        setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(resId), null, null, null);
    }

    public void setDrawable(Drawable drawable) {
        setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

}
