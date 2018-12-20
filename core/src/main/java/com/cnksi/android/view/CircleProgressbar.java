package com.cnksi.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.cnksi.android.R;
import com.cnksi.android.utils.DisplayUtil;

/**
 * 环形进度条
 */
public class CircleProgressbar extends View {
    private Paint mCirclePaint;
    private Paint mTextPaint;
    private Paint mArcPaint;
    private float mPercentProgress;
    private int mInnerColor;
    private int mOutColor;
    private int mInnerCircleWidth;
    private int mOutCircleWidth;
    private String text = "下载";

    public CircleProgressbar(Context context) {
        this(context, null);
    }

    public CircleProgressbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressbar);
        mInnerColor = typedArray.getColor(R.styleable.CircleProgressbar_cp_innerCircleColor, ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mOutColor = typedArray.getColor(R.styleable.CircleProgressbar_cp_outCircleColor, ContextCompat.getColor(getContext(), R.color.colorAccent));
        mInnerCircleWidth = (int) typedArray.getDimension(R.styleable.CircleProgressbar_cp_innerCircleWidth, DisplayUtil.dip2px(getContext(), 2));
        mOutCircleWidth = (int) typedArray.getDimension(R.styleable.CircleProgressbar_cp_innerCircleWidth, DisplayUtil.dip2px(getContext(), 2));
        typedArray.recycle();
        initPaint();

    }

    private void initPaint() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mInnerColor);
        mCirclePaint.setStrokeWidth(mInnerCircleWidth);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(DisplayUtil.dip2px(getContext(), 12));
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(mOutColor);
        mArcPaint.setStrokeWidth(mOutCircleWidth);
        mArcPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width > height ? height : width, width > height ? height : width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCircle(canvas);
        drawText(canvas);
        drawArc(canvas);
    }

    /**
     * 画圆
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        float cx = getWidth() / 2;
        float cy = getHeight() / 2;
        canvas.drawCircle(cx, cy, getWidth() / 2 - mOutCircleWidth / 2, mCirclePaint);
    }

    /**
     * 画文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), textBounds);
        int dx = getWidth() / 2 - textBounds.width() / 2;
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(text, dx, baseLine, mTextPaint);
    }

    /**
     * 画进度
     *
     * @param canvas
     */
    private void drawArc(Canvas canvas) {
        RectF rectF = new RectF(mOutCircleWidth / 2, mOutCircleWidth / 2,
                getWidth() - mOutCircleWidth / 2, getHeight() - mOutCircleWidth / 2);
        float sweepAngle = mPercentProgress * 360;
        canvas.drawArc(rectF, 0, sweepAngle, false, mArcPaint);
    }

    public synchronized void setCurrentProgress(float currentProgress) {
        this.mPercentProgress = currentProgress;
        if (mPercentProgress > 0) {
            text = (int) (mPercentProgress * 100) + "%";
        }
        invalidate();
    }

    public synchronized void setText(String text) {
        this.text = text;
        invalidate();
    }
}