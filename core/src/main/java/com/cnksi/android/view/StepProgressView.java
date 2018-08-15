package com.cnksi.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.cnksi.android.R;
import com.cnksi.android.utils.Colors;
import com.cnksi.android.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 步骤进度View
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/8/14
 * @since 1.0
 */
public class StepProgressView extends HorizontalScrollView {

    private static final int PADDING = 10;

    private static final int CURRENT_DOT_OUT_CIRCLE = 8;

    /**
     * 最小宽度
     */
    private float minStepWidth = 20;
    /**
     * 步骤描述
     */
    private List<CharSequence> mStepList = new ArrayList<>();
    /**
     * 当前进行到第几步
     */
    private int currentStep = 0;
    /**
     * 完成步骤颜色
     */
    private int completeColor = Colors.GREEN;
    /**
     * 当前步骤颜色
     */
    private int currentColor = Colors.RED;
    /**
     * 未完成步骤颜色
     */
    private int unCompleteColor = Colors.GRAY;
    /**
     * 步骤点的大小
     */
    private float stepDotRadius = 10;
    /**
     * 当前步骤点大小
     */
    private float currentDotRadius = stepDotRadius + CURRENT_DOT_OUT_CIRCLE;
    /**
     * 线的宽度
     */
    private float lineWidth = 5;
    /**
     * 文字描述画笔
     */
    private Paint mTextPaint;
    /**
     * 文字大小
     */
    private float textSize;
    /**
     * 文字高度
     */
    private float textHeight;
    /**
     * 第一个文字长度和最后一个文字长度
     */
    private float textFirstWidth, textLastWidth;
    /**
     * 步骤点画笔
     */
    private Paint mDotPaint;
    /**
     * 线画笔
     */
    private Paint mLinePaint;
    /**
     * 文字开始位置
     */
    private float textStartY;

    public StepProgressView(Context context) {
        this(context, null);
    }

    public StepProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public StepProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StepProgressView, defStyleAttr, 0);
        minStepWidth = a.getDimensionPixelSize(R.styleable.StepProgressView_spv_minStepWidth, 20);
        textSize = a.getDimensionPixelSize(R.styleable.StepProgressView_spv_textSize, 20);
        completeColor = a.getColor(R.styleable.StepProgressView_spv_completeColor, Colors.GREEN);
        currentColor = a.getColor(R.styleable.StepProgressView_spv_currentColor, Colors.RED);
        unCompleteColor = a.getColor(R.styleable.StepProgressView_spv_unCompleteColor, Colors.GRAY);
        stepDotRadius = a.getDimensionPixelOffset(R.styleable.StepProgressView_spv_stepDotRadius, 20);
        currentDotRadius = stepDotRadius + CURRENT_DOT_OUT_CIRCLE;
        lineWidth = a.getDimensionPixelSize(R.styleable.StepProgressView_spv_lineWidth, 5);
        a.recycle();
        this.addView(new ChildView(context));
    }

    private class ChildView extends View {

        public ChildView(Context context) {
            this(context, null);
        }

        public ChildView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public ChildView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            this(context, attrs, defStyleAttr, 0);
        }

        public ChildView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            init();
        }

        private void init() {
            mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setTextSize(textSize);
            textHeight = DisplayUtil.getFontHeight(mTextPaint);
            calculateTextWidth();
            textStartY = textHeight + PADDING + currentDotRadius * 2;

            mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLinePaint.setStrokeWidth(lineWidth);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = measureWidth(widthMeasureSpec);
            int height = measureHeight(heightMeasureSpec);
            int count = mStepList.size() - 1;
            minStepWidth = Math.max((width - textFirstWidth / 2 - textLastWidth / 2 - PADDING * 2) / count, minStepWidth);
            height = (int) Math.max(height, currentDotRadius * 2 + PADDING * 2 + textHeight + PADDING);
            setMeasuredDimension((int) (minStepWidth * count + textLastWidth / 2 + textFirstWidth / 2 + PADDING * 2), height);
        }

        private int measureWidth(int widthMeasureSpec) {
            int result = 0;
            int specMode = MeasureSpec.getMode(widthMeasureSpec);
            int specSize = MeasureSpec.getSize(widthMeasureSpec);
            if (specMode == MeasureSpec.EXACTLY) {
                result = specSize;
            } else {
                if (specMode == MeasureSpec.AT_MOST) {
                    result = Math.min(result, specSize);
                }
            }
            return result;
        }

        private int measureHeight(int heightMeasureSpec) {
            int result = 0;
            int specMode = MeasureSpec.getMode(heightMeasureSpec);
            int specSize = MeasureSpec.getSize(heightMeasureSpec);
            if (specMode == MeasureSpec.EXACTLY) {
                result = specSize;
            } else {
                if (specMode == MeasureSpec.AT_MOST) {
                    result = Math.min(result, specSize);
                }
            }
            return result;
        }


        @Override
        protected void onDraw(Canvas canvas) {
            //设置起点
            float startX = PADDING + textFirstWidth / 2;
            float startY = PADDING + currentDotRadius;
            for (int i = 0, count = mStepList.size(); i < count; i++) {
                CharSequence text = mStepList.get(i).toString();
                float endX = startX + minStepWidth;
                float textStartX = startX - DisplayUtil.getFontWidth(mTextPaint, String.valueOf(text)) / 2;
                //设置颜色
                if (i <= currentStep) {
                    mLinePaint.setColor(completeColor);
                    mDotPaint.setColor(completeColor);
                    mTextPaint.setColor(completeColor);
                    if (i == currentStep) {
                        mLinePaint.setColor(unCompleteColor);
                        mDotPaint.setColor(currentColor);
                        mTextPaint.setColor(currentColor);
                    }
                } else {
                    mLinePaint.setColor(unCompleteColor);
                    mDotPaint.setColor(unCompleteColor);
                    mTextPaint.setColor(unCompleteColor);
                }
                //画线
                if (i < count - 1) {
                    canvas.drawLine(startX, startY, endX, startY, mLinePaint);
                }

                //画当前点
                if (mDotPaint.getColor() == currentColor) {
                    mDotPaint.setAlpha(150);
                    canvas.drawOval((startX - currentDotRadius), (startY - currentDotRadius), (startX + currentDotRadius), (startY + currentDotRadius), mDotPaint);
                    mDotPaint.setAlpha(255);
                    //画文字
                    canvas.drawText(text, 0, text.length(), textStartX, textStartY, mTextPaint);
                } else {
                    //画文字
                    canvas.drawText(text, 0, text.length(), textStartX, textStartY, mTextPaint);
                }
                //画点
                canvas.drawOval((startX - stepDotRadius), (startY - stepDotRadius), (startX + stepDotRadius), (startY + stepDotRadius), mDotPaint);

                startX = endX;
            }
        }
    }

    /**
     * 计算文字宽度
     */
    private void calculateTextWidth() {
        if (mStepList != null && !mStepList.isEmpty()) {
            textFirstWidth = DisplayUtil.getFontWidth(mTextPaint, String.valueOf(mStepList.get(0)));
            textLastWidth = DisplayUtil.getFontWidth(mTextPaint, String.valueOf(mStepList.get(mStepList.size() - 1)));
        }
    }

    public void initData(List<CharSequence> stepList, int currentStep) {
        this.currentStep = currentStep - 1;
        setStepList(stepList);
    }

    public List<CharSequence> getStepList() {
        return mStepList;
    }

    public void setStepList(List<CharSequence> stepList) {
        mStepList = stepList;
        calculateTextWidth();
        requestLayout();
    }

    public int getCurrentStep() {
        return currentStep + 1;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep - 1;
        requestLayout();
    }
}
