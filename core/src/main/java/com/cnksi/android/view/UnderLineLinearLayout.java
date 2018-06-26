package com.cnksi.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
    private boolean drawUnderLine;
    private int marginLeft;
    private int marginRight;
    private int lineColor = Color.parseColor("#e1e4e4");
    private int lineHeight;
    private Paint linePaint;

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
        lineColor = a.getColor(R.styleable.UnderLineLinearLayout_lineColor, lineColor);
        marginLeft = a.getDimensionPixelSize(R.styleable.UnderLineLinearLayout_marginLeft, 0);
        marginRight = a.getDimensionPixelSize(R.styleable.UnderLineLinearLayout_marginRight, 0);
        lineHeight = a.getDimensionPixelSize(R.styleable.UnderLineLinearLayout_lineHeight, 2);
        drawUnderLine = a.getBoolean(R.styleable.UnderLineLinearLayout_drawUnderLine, false);
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(lineHeight);
        linePaint.setColor(lineColor);
        a.recycle();
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (drawUnderLine && lineHeight > 0) {
            canvas.drawLine(marginLeft, getHeight() - lineHeight, getWidth() - marginRight, getHeight() - lineHeight, linePaint);
        }
    }

    private Rect invalidateRect = new Rect();

    private void calcInvalidateRect() {
        invalidateRect.left = 0;
        invalidateRect.right = getWidth();
        invalidateRect.bottom = getHeight();
        invalidateRect.top = getHeight() - lineHeight;

    }

    public void setDrawUnderLine(boolean drawUnderLine) {
        this.drawUnderLine = drawUnderLine;
        calcInvalidateRect();
        invalidate(invalidateRect);
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
        calcInvalidateRect();
        invalidate(invalidateRect);
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
        calcInvalidateRect();
        invalidate(invalidateRect);
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        calcInvalidateRect();
        invalidate(invalidateRect);
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
        calcInvalidateRect();
        invalidate(invalidateRect);
    }

    public void setLinePaint(Paint linePaint) {
        this.linePaint = linePaint;
        calcInvalidateRect();
        invalidate(invalidateRect);
    }
}
