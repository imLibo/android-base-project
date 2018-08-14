package com.cnksi.android.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.Gravity;

import com.cnksi.android.R;


/**
 * @author Wastrel
 * @version 1.0
 * @auth wastrel
 * @date 2017/1/13 14:24
 * @copyRight 四川金信石信息技术有限公司
 * @since 1.0
 */
public class BorderLineTagView extends AppCompatRadioButton {

    protected Context mContext;

    private int checkedColor;
    private int radius;
    private int shape;
    private int strokeWidth;
    private boolean isRadioButton;


    public BorderLineTagView(Context context) {
        super(context);
        mContext = context;
        init(null, 0);
    }

    public BorderLineTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs, 0);
    }

    public BorderLineTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BorderLineTagView, defStyleAttr, 0);
        checkedColor = a.getColor(R.styleable.BorderLineTagView_blt_checkedColor, 0);
        radius = a.getDimensionPixelSize(R.styleable.BorderLineTagView_blt_radius, 9);
        strokeWidth = a.getDimensionPixelOffset(R.styleable.BorderLineTagView_blt_strokeWidth, 2);
        shape = convertShape(a.getInt(R.styleable.BorderLineTagView_blt_shape, 0));
        isRadioButton = a.getBoolean(R.styleable.BorderLineTagView_blt_isRadioButton, false);
        a.recycle();
        updateBackground();
    }

    private void updateBackground() {
        //取消checkbox方框
        setButtonDrawable(null);
        if (getGravity() == (Gravity.TOP | Gravity.START)) {
            setGravity(Gravity.CENTER);
        }

        StateListDrawable stateListDrawable = new StateListDrawable();
        GradientDrawable drawable = getDrawable(checkedColor);
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, drawable);

        ColorStateList textColor = getTextColors();
        int txtColor = textColor.getDefaultColor();
        stateListDrawable.addState(new int[]{}, getDrawable(txtColor));
        this.setBackground(stateListDrawable);

        setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_checked}, {}}, new int[]{checkedColor, txtColor}));
    }

    private GradientDrawable getDrawable(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radius);
        drawable.setStroke(strokeWidth, color);
        drawable.setShape(shape);
        drawable.setColor(getBackgroundColor());
        return drawable;
    }

    private int getBackgroundColor() {
        //获取背景颜色
        int backgroundColor = getResources().getColor(R.color.core_white_color_FFFFFF);
        Drawable background = getBackground();
        if (background instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) background;
            backgroundColor = colorDrawable.getColor();
        }
        return backgroundColor;
    }

    private int convertShape(int shape) {
        if (shape == 1) {
            return GradientDrawable.OVAL;
        }
        return GradientDrawable.RECTANGLE;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (shape == 1) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            int max = Math.max(width, height);
            setMeasuredDimension(max, max);
        }
    }

    public int getCheckedColor() {
        return checkedColor;
    }

    public void setCheckedColor(int checkedColor) {
        this.checkedColor = checkedColor;
        updateBackground();
        invalidate();
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        updateBackground();
        invalidate();
    }

    public int getShape() {
        return shape;
    }

    public void setShape(int shape) {
        this.shape = shape;
        requestLayout();
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        updateBackground();
        invalidate();
    }

    public boolean isRadioButton() {
        return isRadioButton;
    }

    @Override
    public void toggle() {
        if (isRadioButton) {
            super.toggle();
        } else {
            setChecked(!isChecked());
        }
    }
}
