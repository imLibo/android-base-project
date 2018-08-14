package com.cnksi.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.cnksi.android.R;
import com.cnksi.android.utils.Colors;

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
public class StepProgressView extends View {

    private int width;

    /**
     * 最小宽度
     */
    private int minStepWidth = 20;
    /**
     * 步骤描述
     */
    private List<CharSequence> mStepList = new ArrayList<>();
    /**
     * 当前进行到第几步
     */
    private int currentStep = 3;
    /**
     * 是否全部完成
     */
    private boolean isAllComplete;

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
    private int stepDotRadius = 10;
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
     * 步骤点画笔
     */
    private Paint mDotPaint;
    /**
     * 线画笔
     */
    private Paint mLinePaint;
    /**
     * 起点
     */
    private int startX = stepDotRadius;
    private int startY = stepDotRadius;

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
        lineWidth = a.getDimensionPixelSize(R.styleable.StepProgressView_spv_lineWidth, 5);
        a.recycle();
        init(context);
    }

    private void init(Context context) {

        mStepList.add("施工单位");
        mStepList.add("监理单位");
        mStepList.add("业主单位");
        mStepList.add("建设部备案");

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(textSize);

        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(lineWidth);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        int count = mStepList.size() - 1;
        minStepWidth = Math.max(width / count, minStepWidth);
        setMeasuredDimension((minStepWidth * count + stepDotRadius * 2), height);
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

    public List<CharSequence> getStepList() {
        return mStepList;
    }

    public void setStepList(List<CharSequence> stepList) {
        mStepList = stepList;
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0, count = mStepList.size() - 1; i < count; i++) {
            int endX = startX + minStepWidth;
            if (i <= currentStep) {
                mLinePaint.setColor(completeColor);
                if (i == currentStep) {

                }
            } else {
                mLinePaint.setColor(unCompleteColor);
            }
            //画点
            canvas.drawOval(startX-stepDotRadius,startY-stepDotRadius,startX + stepDotRadius,startY + stepDotRadius,mDotPaint);
            //画线
            canvas.drawLine(startX, startY, endX, startY, mLinePaint);
            startX = endX;
        }
        //TODO: 画最后一个点

    }

    /**
     * 画点
     *
     * @param canvas
     */
    private void drawDot(Canvas canvas) {

    }
}
