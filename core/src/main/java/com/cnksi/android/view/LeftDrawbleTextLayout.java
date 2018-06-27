package com.cnksi.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnksi.android.R;


/**
 *
 * @author lyongfly
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/6/27
 * @version 1.0
 * @since 1.0
 */
public class LeftDrawbleTextLayout extends LinearLayout {

    private Context mContext;
    private TextView mTvValue;
    /**
     * label和value之间的间隔
     */
    private int spacing;
    private int valueTextSize;
    private int valueTextColor = Color.parseColor("#d6d7d9");
    private int lineHeight;
    private int lineMarginLeft;
    private int lineMarginRight;
    private int lineColor = Color.parseColor("#e1e4e4");

    private Paint mPaint;
    private Rect mUnderRect = new Rect();

    public LeftDrawbleTextLayout(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public LeftDrawbleTextLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public LeftDrawbleTextLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.mContext = context;
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LabelValueLayout, defStyleAttr, defStyleRes);
        spacing = a.getDimensionPixelSize(R.styleable.LabelValueLayout_lvl_spacing, 0);
        valueTextSize = a.getDimensionPixelSize(R.styleable.LabelValueLayout_lvl_valueTextSize, valueTextSize);
        valueTextColor = a.getColor(R.styleable.LabelValueLayout_lvl_valueTextColor, valueTextSize);
        lineHeight = a.getDimensionPixelSize(R.styleable.LabelValueLayout_lvl_lineHeight, 0);
        lineMarginLeft = a.getDimensionPixelSize(R.styleable.LabelValueLayout_lvl_lineMarginLeft, 0);
        lineMarginRight = a.getDimensionPixelSize(R.styleable.LabelValueLayout_lvl_lineMarginRight, 0);
        lineColor = a.getColor(R.styleable.LabelValueLayout_lvl_lineColor, lineColor);
        a.recycle();
        initPaint();
        initView();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(lineColor);
    }

    /**
     * 初始化View
     */
    private void initView() {
        mTvValue = new TextView(mContext);
        mTvValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, valueTextSize);
        mTvValue.setTextColor(valueTextColor);

        setSpacing(spacing);
        this.addView(mTvValue);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (lineHeight > 0) {
            mUnderRect.set(lineMarginLeft, getHeight() - lineHeight, getWidth() - lineMarginRight, getHeight());
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (!mUnderRect.isEmpty()) {
            canvas.drawRect(mUnderRect, mPaint);
        }
        super.dispatchDraw(canvas);
    }

    public TextView getValueView() {
        return mTvValue;
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (getOrientation() == LinearLayout.HORIZONTAL) {
            params.leftMargin = spacing;
        } else {
            params.topMargin = spacing;
        }
        mTvValue.setLayoutParams(params);
    }

    public int getValueTextSize() {
        return valueTextSize;
    }

    public void setValueTextSize(int valueTextSize) {
        this.valueTextSize = valueTextSize;
        this.mTvValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, valueTextSize);
    }

    public int getValueTextColor() {
        return valueTextColor;
    }

    public void setValueTextColor(@ColorInt int valueTextColor) {
        this.mTvValue.setTextColor(valueTextColor);
    }

    /**
     * 设置value文字
     *
     * @param charSequence
     */
    public void setValueText(CharSequence charSequence) {
        this.mTvValue.setText(charSequence);
    }

    public CharSequence getValueText() {
        return this.mTvValue.getText();
    }

    public int getLineHeight() {
        return lineHeight;
    }

    private void invalidateUnderRect() {
        if (lineHeight <= 0) {
            mUnderRect.setEmpty();
        } else {
            mUnderRect.set(lineMarginLeft, getHeight() - lineHeight, getWidth() - lineMarginRight, getHeight());
        }
        invalidate();
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
        invalidateUnderRect();
    }

    public int getLineMarginLeft() {
        return lineMarginLeft;
    }

    public void setLineMarginLeft(int lineMarginLeft) {
        this.lineMarginLeft = lineMarginLeft;
        invalidateUnderRect();
    }

    public int getLineMarginRight() {
        return lineMarginRight;
    }

    public void setLineMarginRight(int lineMarginRight) {
        this.lineMarginRight = lineMarginRight;
        invalidateUnderRect();
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        this.mPaint.setColor(lineColor);
        invalidate();
    }
}
