package com.cnksi.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.cnksi.android.R;


/**
 * @author Wastrel
 * @version 1.0
 * @auth wastrel
 * @date 2017/4/12 14:07
 * @copyRight 四川金信石信息技术有限公司
 * @since 1.0
 */
public class UnderLineLinearLayout extends LinearLayout {

    private boolean drawUnderLine = true;
    private int marginLeft;
    private int marginRight;
    private int lineColor = Color.parseColor("#e1e4e4");
    private int lineHeight;
    private int normalColor;
    private int pressColor;
    private Paint linePaint;
    private Rect lineRect = new Rect();

    public UnderLineLinearLayout(Context context) {
        super(context);
        init(null, 0, 0);
    }

    public UnderLineLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public UnderLineLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UnderLineLinearLayout, defStyleAttr, defStyleRes);
        lineColor = a.getColor(R.styleable.UnderLineLinearLayout_ull_lineColor, lineColor);
        marginLeft = a.getDimensionPixelSize(R.styleable.UnderLineLinearLayout_ull_marginLeft, 0);
        marginRight = a.getDimensionPixelSize(R.styleable.UnderLineLinearLayout_ull_marginRight, 0);
        lineHeight = a.getDimensionPixelSize(R.styleable.UnderLineLinearLayout_ull_lineHeight, 2);
        normalColor = a.getColor(R.styleable.UnderLineLinearLayout_ull_normalColor, 0);
        pressColor = a.getColor(R.styleable.UnderLineLinearLayout_ull_pressColor, getPressColor(normalColor));
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(lineColor);
        a.recycle();
        updateBackground();
    }

    private void updateBackground() {
        if (normalColor != 0) {
            StateListDrawable stateListDrawable = new StateListDrawable();
            GradientDrawable pressDrawable = new GradientDrawable();
            pressDrawable.setColor(pressColor);
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressDrawable);
            stateListDrawable.addState(new int[]{android.R.attr.state_selected}, pressDrawable);
            GradientDrawable normalDrawable = new GradientDrawable();
            normalDrawable.setColor(normalColor);
            stateListDrawable.addState(new int[]{}, normalDrawable);
            this.setBackground(stateListDrawable);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        lineRect.set(marginLeft, getHeight() - lineHeight, getWidth() - marginRight, getHeight());
    }

    @ColorInt
    private int getPressColor(@ColorInt int unPressColor) {
        int r = Color.red(unPressColor);
        int g = Color.green(unPressColor);
        int b = Color.blue(unPressColor);
        float a = Color.alpha(unPressColor) * 1.0f / 255;
        //Mask is #33000000
        int maskR = 0, maskG = 0, maskB = 0;
        float maskA = 0.2f;

        int rsR = (int) (r * a * (1 - maskA) + maskA * maskR);
        int rsB = (int) (b * a * (1 - maskA) + maskA * maskB);
        int rsG = (int) (g * a * (1 - maskA) + maskA * maskG);
        float rsA = 1 - (1 - a) * (1 - maskA);
        return Color.argb((int) (rsA * 255), rsR, rsG, rsB);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!lineRect.isEmpty() && drawUnderLine) {
            canvas.drawRect(lineRect, linePaint);
        }
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
        invalidateRect();
    }

    public void setDrawUnderLine(boolean drawUnderLine) {
        this.drawUnderLine = drawUnderLine;
        invalidateRect();
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
        invalidate();
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        linePaint.setColor(lineColor);
        invalidateRect();
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
        invalidateRect();
    }

    private void invalidateRect() {
        if (lineHeight <= 0) {
            lineRect.setEmpty();
        } else {
            lineRect.set(marginLeft, getHeight() - lineHeight, getWidth() - marginRight, getHeight());
        }
        invalidate();
    }
}
