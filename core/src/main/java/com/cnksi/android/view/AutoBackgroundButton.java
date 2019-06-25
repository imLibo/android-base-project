package com.cnksi.android.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import com.cnksi.android.R;


/**
 * @author Wastrel
 * @version 1.0
 * @date 2017/1/13 14:24
 * @copyRight 四川金信石信息技术有限公司
 * @since 1.0
 */
public class AutoBackgroundButton extends AppCompatTextView {

    private int pressColor;
    private int textPressColor;
    private int normalColor;
    private int disableColor;
    private int radius;
    private int shape;

    public AutoBackgroundButton(Context context) {
        super(context);
        init(null, 0, 0);
    }

    public AutoBackgroundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public AutoBackgroundButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AutoBackgroundButton, defStyleAttr, defStyleRes);
        normalColor = a.getColor(R.styleable.AutoBackgroundButton_abb_normalColor, 0);
        pressColor = a.getColor(R.styleable.AutoBackgroundButton_abb_pressColor, getPressColor(normalColor));
        textPressColor = a.getColor(R.styleable.AutoBackgroundButton_abb_textPressColor, 0);
        pressColor = a.getColor(R.styleable.AutoBackgroundButton_abb_pressColor, getPressColor(normalColor));
        disableColor = a.getColor(R.styleable.AutoBackgroundButton_abb_disableColor, Color.GRAY);
        radius = a.getDimensionPixelSize(R.styleable.AutoBackgroundButton_abb_radius, 9);
        shape = convertShape(a.getInt(R.styleable.AutoBackgroundButton_abb_shape, 0));
        a.recycle();
        updateBackground();
    }

    private void updateBackground() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        GradientDrawable pressDrawable = new GradientDrawable();
        pressDrawable.setCornerRadius(radius);
        pressDrawable.setColor(pressColor);
        pressDrawable.setShape(shape);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, pressDrawable);
        GradientDrawable disableDrawable = new GradientDrawable();
        disableDrawable.setCornerRadius(radius);
        disableDrawable.setColor(disableColor);
        disableDrawable.setShape(shape);
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, disableDrawable);
        GradientDrawable normalDrawable = new GradientDrawable();
        normalDrawable.setCornerRadius(radius);
        normalDrawable.setColor(normalColor);
        normalDrawable.setShape(shape);
        stateListDrawable.addState(new int[]{}, normalDrawable);
        if (getGravity() == (Gravity.TOP | Gravity.START)) {
            setGravity(Gravity.CENTER);
        }
        setPadding(getPaddingLeft(), 0, getPaddingRight(), 0);
        this.setBackground(stateListDrawable);
        if (textPressColor != 0) {
            ColorStateList textColor = getTextColors();
            //默认颜色
            int txtColor = textColor.getDefaultColor();
            setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_pressed}, {android.R.attr.state_selected}, {}}, new int[]{textPressColor, textPressColor, txtColor}));
        }
    }

    private int convertShape(int shape) {
        if (shape == 1) {
            return GradientDrawable.OVAL;
        }
        return GradientDrawable.RECTANGLE;
    }

    @ColorInt
    private int getPressColor(@ColorInt int color) {
        if (color == 0) {
            return color;
        }
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        float a = Color.alpha(color) * 1.0f / 255;
        //Mask is #33000000
        int maskR = 0, maskG = 0, maskB = 0;
        float maskA = 0.2f;

        int rsR = (int) (r * a * (1 - maskA) + maskA * maskR);
        int rsB = (int) (b * a * (1 - maskA) + maskA * maskB);
        int rsG = (int) (g * a * (1 - maskA) + maskA * maskG);
        float rsA = 1 - (1 - a) * (1 - maskA);
        return Color.argb((int) (rsA * 255), rsR, rsG, rsB);
    }

    public void setNormalColor(@ColorInt int normalColor) {
        this.normalColor = normalColor;
        this.pressColor = getPressColor(this.normalColor);
        updateBackground();
    }

    public void setPressColor(int pressColor) {
        this.pressColor = pressColor;
        updateBackground();
    }

    public void setTextPressColor(int textPressColor) {
        this.textPressColor = textPressColor;
        updateBackground();
    }

    public void setDisableColor(int disableColor) {
        this.disableColor = disableColor;
        updateBackground();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        updateBackground();
    }

    public void setShape(int shape) {
        this.shape = convertShape(shape);
        updateBackground();
    }

    public int getPressColor() {
        return pressColor;
    }

    public int getTextPressColor() {
        return textPressColor;
    }

    public int getNormalColor() {
        return normalColor;
    }

    public int getDisableColor() {
        return disableColor;
    }

    public int getRadius() {
        return radius;
    }

    public int getShape() {
        return shape;
    }
}
