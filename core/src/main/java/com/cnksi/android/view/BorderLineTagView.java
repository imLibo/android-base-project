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
import android.widget.RadioGroup;

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

    private int checkedTextColor;
    private int checkedBackgroundColor;
    private int radius;
    private int shape;
    private int strokeWidth;
    private boolean isRadioButton;
    private boolean isUnCheckedShowBorder;
    private boolean allowNoCheck = false;
    /**
     * 选中后是否填充背景
     */
    private boolean isFillBackground;


    public BorderLineTagView(Context context) {
        super(context);
        init(null, 0);
    }

    public BorderLineTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BorderLineTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BorderLineTagView, defStyleAttr, 0);
        checkedTextColor = a.getColor(R.styleable.BorderLineTagView_blt_checkedTextColor, 0);
        checkedBackgroundColor = a.getColor(R.styleable.BorderLineTagView_blt_checkedBackgroundColor, 0);
        radius = a.getDimensionPixelSize(R.styleable.BorderLineTagView_blt_radius, 90);
        strokeWidth = a.getDimensionPixelOffset(R.styleable.BorderLineTagView_blt_strokeWidth, 3);
        shape = convertShape(a.getInt(R.styleable.BorderLineTagView_blt_shape, 0));
        isRadioButton = a.getBoolean(R.styleable.BorderLineTagView_blt_isRadioButton, false);
        allowNoCheck = a.getBoolean(R.styleable.BorderLineTagView_blt_allowNoCheck, false);
        isFillBackground = a.getBoolean(R.styleable.BorderLineTagView_blt_isFillBackground, false);
        isUnCheckedShowBorder = a.getBoolean(R.styleable.BorderLineTagView_blt_isUnCheckedShowBorder, true);
        a.recycle();
        updateBackground();
    }

    private void updateBackground() {
        ColorStateList textColor = getTextColors();
        int txtColor = textColor.getDefaultColor();

        //取消checkbox方框
        setButtonDrawable(null);
        if (getGravity() == (Gravity.TOP | Gravity.START)) {
            setGravity(Gravity.CENTER);
        }

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_checked},
                isFillBackground ? getFillBackgroundDrawable(checkedBackgroundColor) : getDrawable(checkedBackgroundColor, strokeWidth));
        stateListDrawable.addState(new int[]{}, getDrawable(getBackgroundColor(), isUnCheckedShowBorder ? strokeWidth : 0));
        this.setBackground(stateListDrawable);

        setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_checked}, {}}, new int[]{checkedTextColor, txtColor}));
    }

    private GradientDrawable getDrawable(int color, int strokeWidth) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radius);
        drawable.setShape(shape);
        drawable.setStroke(strokeWidth, color);
        return drawable;
    }

    private GradientDrawable getFillBackgroundDrawable(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radius);
        drawable.setShape(shape);
        drawable.setColor(color);
        return drawable;
    }

    private int getBackgroundColor() {
        //获取背景颜色
        int backgroundColor = getResources().getColor(R.color.core_white_FFFFFF);
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

    public int getCheckedTextColor() {
        return checkedTextColor;
    }

    public void setCheckedTextColor(int checkedTextColor) {
        this.checkedTextColor = checkedTextColor;
        updateBackground();
        invalidate();
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        updateBackground();
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
    }

    public boolean isFillBackground() {
        return isFillBackground;
    }

    public void setFillBackground(boolean fillBackground) {
        isFillBackground = fillBackground;
        updateBackground();
    }

    public boolean isRadioButton() {
        return isRadioButton;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
        if (allowNoCheck && !isChecked() && getParent() != null) {
            ((RadioGroup) getParent()).clearCheck();
        }
    }

    public boolean isUnCheckedShowBorder() {
        return isUnCheckedShowBorder;
    }

    public void setUnCheckedShowBorder(boolean unCheckedShowBorder) {
        isUnCheckedShowBorder = unCheckedShowBorder;
        updateBackground();
    }
}
