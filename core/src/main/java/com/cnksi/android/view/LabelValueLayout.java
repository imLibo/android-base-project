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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnksi.android.R;


/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/6/27
 * @since 1.0
 */
public class LabelValueLayout extends LinearLayout {

    private Context mContext;
    private TextView mTvLabel, mTvValue;
    /**
     * label和value之间的间隔
     */
    private int spacing;
    private int labelTextSize = 45;
    private int valueTextSize;
    private int labelTextColor = Color.parseColor("#d6d7d9");
    private int valueTextColor;
    private int lineHeight;
    private int lineWidth;
    private int lineMarginLeft;
    private int lineMarginRight;
    private int lineMarginTop;
    private int lineMarginBottom;
    private int lineColor = Color.parseColor("#e1e4e4");

    private Paint mPaint;
    private Rect mUnderRect = new Rect();
    private Rect mLeftRect = new Rect();

    public LabelValueLayout(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public LabelValueLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public LabelValueLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.mContext = context;
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LabelValueLayout, defStyleAttr, defStyleRes);
        spacing = a.getDimensionPixelSize(R.styleable.LabelValueLayout_lvl_spacing, 0);
        labelTextSize = a.getDimensionPixelSize(R.styleable.LabelValueLayout_lvl_labelTextSize, labelTextSize);
        valueTextSize = a.getDimensionPixelSize(R.styleable.LabelValueLayout_lvl_valueTextSize, labelTextSize);
        labelTextColor = a.getColor(R.styleable.LabelValueLayout_lvl_labelTextColor, labelTextColor);
        valueTextColor = a.getColor(R.styleable.LabelValueLayout_lvl_valueTextColor, labelTextColor);
        lineHeight = a.getDimensionPixelSize(R.styleable.LabelValueLayout_lvl_lineHeight, 0);
        lineWidth = a.getDimensionPixelSize(R.styleable.LabelValueLayout_lvl_lineWidth, 0);
        lineMarginLeft = a.getDimensionPixelSize(R.styleable.LabelValueLayout_lvl_lineMarginLeft, 0);
        lineMarginRight = a.getDimensionPixelSize(R.styleable.LabelValueLayout_lvl_lineMarginRight, 0);
        lineMarginTop = a.getDimensionPixelSize(R.styleable.LabelValueLayout_lvl_lineMarginTop, 0);
        lineMarginBottom = a.getDimensionPixelSize(R.styleable.LabelValueLayout_lvl_lineMarginBottom, 0);
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
        mTvLabel = new TextView(mContext);
        mTvLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelTextSize);
        mTvLabel.setTextColor(labelTextColor);

        mTvValue = new TextView(mContext);
        mTvValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, valueTextSize);
        mTvValue.setTextColor(valueTextColor);

        setSpacing(spacing);
        this.addView(mTvLabel);
        this.addView(mTvValue);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (lineHeight > 0) {
            mUnderRect.set(lineMarginLeft, getHeight() - lineHeight, getWidth() - lineMarginRight, getHeight());
        }
        if (lineWidth > 0) {
            mLeftRect.set(0, lineMarginTop, lineWidth, getHeight() - lineMarginBottom);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (!mUnderRect.isEmpty()) {
            canvas.drawRect(mUnderRect, mPaint);
        }
        if (!mLeftRect.isEmpty()) {
            canvas.drawRect(mLeftRect, mPaint);
        }
        super.dispatchDraw(canvas);
    }

    public TextView getLabelView() {
        return mTvLabel;
    }

    public TextView getValueView() {
        return mTvValue;
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
        LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        if (getOrientation() == LinearLayout.HORIZONTAL) {
            params.leftMargin = spacing;
        } else {
            params.topMargin = spacing;
        }
        mTvValue.setLayoutParams(params);
    }

    public int getLabelTextSize() {
        return labelTextSize;
    }

    public void setLabelTextSize(int labelTextSize) {
        this.labelTextSize = labelTextSize;
        this.mTvLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelTextSize);
    }

    public int getValueTextSize() {
        return valueTextSize;

    }

    public void setValueTextSize(int valueTextSize) {
        this.valueTextSize = valueTextSize;
        this.mTvValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, valueTextSize);
    }

    public int getLabelTextColor() {
        return labelTextColor;
    }

    public void setLabelTextColor(@ColorInt int labelTextColor) {
        this.mTvLabel.setTextColor(labelTextColor);
    }

    public int getValueTextColor() {
        return valueTextColor;
    }

    public void setValueTextColor(@ColorInt int valueTextColor) {
        this.mTvValue.setTextColor(valueTextColor);
    }

    /**
     * 设置label文字
     *
     * @param charSequence
     */
    public void setLabelText(CharSequence charSequence) {
        this.mTvLabel.setText(charSequence);
    }

    /**
     * 设置value文字
     *
     * @param charSequence
     */
    public void setValueText(CharSequence charSequence) {
        this.mTvValue.setText(charSequence);
    }

    public CharSequence getLabelText() {
        return this.mTvLabel.getText();
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

    private void invalidateLeftRect() {
        if (lineWidth <= 0) {
            mLeftRect.setEmpty();
        } else {
            mLeftRect.set(0, lineMarginTop, lineWidth, getHeight() - lineMarginBottom);
        }
        invalidate();
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
        invalidateUnderRect();
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        invalidateLeftRect();
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

    public int getLineMarginTop() {
        return lineMarginTop;
    }

    public void setLineMarginTop(int lineMarginTop) {
        this.lineMarginTop = lineMarginTop;
        invalidateLeftRect();
    }

    public int getLineMarginBottom() {
        return lineMarginBottom;
    }

    public void setLineMarginBottom(int lineMarginBottom) {
        this.lineMarginBottom = lineMarginBottom;
        invalidateLeftRect();
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