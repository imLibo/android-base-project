package com.cnksi.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;

import com.cnksi.android.log.KLog;
import com.cnksi.android.utils.DisplayUtil;
import com.cnksi.android.utils.ViewUtil;

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

    private Context mContext;

    private Drawable[] drawables;

    private float textWidth = 0f;
    private float textHeight = 0f;

    public CenterDrawableTextView(Context context) {
        this(context, null);
    }

    public CenterDrawableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CenterDrawableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    private void init() {
        drawables = getCompoundDrawables();
        if (drawables[0] != null) {
            setGravity(Gravity.CENTER_VERTICAL);
        } else {
            setGravity(Gravity.CENTER);
        }
        //文本的宽度
        textWidth = DisplayUtil.getFontWidth(getPaint(), getText().toString());
        textHeight = DisplayUtil.getFontHeight(getPaint());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (drawables != null && drawables[0] != null) {
            int width = ViewUtil.measureWidth(widthMeasureSpec);
            int height = ViewUtil.measureHeight(heightMeasureSpec);
            width = Math.max(drawables[0].getIntrinsicWidth() + (int) textWidth + getCompoundDrawablePadding() + getPaddingLeft() + getPaddingRight(), width);
            height = Math.max(Math.max(drawables[0].getIntrinsicHeight(), (int) textHeight) + getPaddingTop() + getPaddingBottom(), height);
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawables[0] != null && textWidth > 0) {
            //drawablePadding值
            int drawablePadding = getCompoundDrawablePadding();
            //icon的宽度
            int drawableWidth = drawables[0].getIntrinsicWidth();
            // icon和文字都居中
            float bodyWidth = textWidth + drawableWidth + drawablePadding;
            float padding = getPaddingLeft() + getPaddingRight();
            KLog.d("width->" + getWidth() + "  hight->" + getHeight() + " drawableWidth->" + drawableWidth);
            //平移
            float translate = (getWidth() - bodyWidth - padding) / 2;
            canvas.translate(translate, 0);
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
