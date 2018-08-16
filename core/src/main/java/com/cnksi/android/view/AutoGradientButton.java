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
 * 背景渐变TextView
 *
 * @author lyongfly
 * @version 1.0
 * @date 2017/1/13 14:24
 * @copyRight 四川金信石信息技术有限公司
 * @since 1.0
 */
public class AutoGradientButton extends AppCompatTextView {

    public interface Orientation {
        int HORIZONTATAL = 0;
        int VERTICAL = 1;
    }

    private int startColor;
    private int endColor;
    private GradientDrawable.Orientation orientation;
    private int disableColor;
    private int radius;
    private int shape;
    private int textPressColor = 0;

    public AutoGradientButton(Context context) {
        super(context);
        init(null, 0, 0);
    }

    public AutoGradientButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public AutoGradientButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AutoGradientButton, defStyleAttr, defStyleRes);
        startColor = a.getColor(R.styleable.AutoGradientButton_agb_startColor, 0);
        endColor = a.getColor(R.styleable.AutoGradientButton_agb_endColor, 0);
        orientation = convertOrientation(a.getInt(R.styleable.AutoGradientButton_agb_orientation, 0));
        disableColor = a.getColor(R.styleable.AutoGradientButton_agb_disableColor, Color.GRAY);
        radius = a.getDimensionPixelSize(R.styleable.AutoGradientButton_agb_radius, 9);
        shape = convertShape(a.getInt(R.styleable.AutoGradientButton_agb_shape, 0));
        if (a.hasValue(R.styleable.AutoGradientButton_agb_textPressColor)) {
            textPressColor = a.getColor(R.styleable.AutoGradientButton_agb_textPressColor, 0);
        }
        a.recycle();
        updateBackground();
    }

    private void updateBackground() {
        //一定是先添加 state_pressed color
        StateListDrawable stateListDrawable = new StateListDrawable();

        GradientDrawable pressDrawable = new GradientDrawable();
        pressDrawable.setCornerRadius(radius);
        pressDrawable.setColors(new int[]{getPressColor(startColor), getPressColor(endColor)});
        pressDrawable.setOrientation(orientation);
        pressDrawable.setShape(shape);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressDrawable);

        GradientDrawable disableDrawable = new GradientDrawable();
        disableDrawable.setCornerRadius(radius);
        disableDrawable.setColor(disableColor);
        disableDrawable.setShape(shape);
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, disableDrawable);

        GradientDrawable normalDrawable = new GradientDrawable();
        normalDrawable.setCornerRadius(radius);
        normalDrawable.setColors(new int[]{startColor, endColor});
        normalDrawable.setOrientation(orientation);
        normalDrawable.setShape(shape);
        stateListDrawable.addState(new int[]{}, normalDrawable);

        this.setBackground(stateListDrawable);

        if (getGravity() == (Gravity.TOP | Gravity.START)) {
            setGravity(Gravity.CENTER);
        }
        if (textPressColor != 0) {
            ColorStateList textColor = getTextColors();
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

    private GradientDrawable.Orientation convertOrientation(int orientation) {
        if (orientation == Orientation.HORIZONTATAL) {
            return GradientDrawable.Orientation.LEFT_RIGHT;
        }
        return GradientDrawable.Orientation.TOP_BOTTOM;
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

    public void setColor(@ColorInt int startColor, @ColorInt int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
        updateBackground();
    }

    public void setOrientation(int orientation) {
        this.orientation = convertOrientation(orientation);
        updateBackground();
    }

    public void setTextPressColor(@ColorInt int textPressColor) {
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

    public int getStartColor() {
        return startColor;
    }

    public int getEndColor() {
        return endColor;
    }

    public int getOrientation() {
        if (orientation == GradientDrawable.Orientation.TOP_BOTTOM) {
            return Orientation.VERTICAL;
        }
        return Orientation.HORIZONTATAL;
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

    public int getTextPressColor() {
        return textPressColor;
    }
}
