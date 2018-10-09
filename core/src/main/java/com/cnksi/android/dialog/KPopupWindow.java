package com.cnksi.android.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.cnksi.android.R;
import com.cnksi.android.databinding.CorePopupwindowLayoutBinding;

/**
 * 封装的PopupWindow
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/10/9
 * @since 1.0
 */
public class KPopupWindow {

    public interface OnClickListener {
        void onLeftClick(View view);

        void onRightClick(View view);
    }

    public interface OnInitEventListener {
        void onInitEvent(View contentView);
    }

    private CorePopupwindowLayoutBinding mBinding;
    private final Context mContext;
    private final int width;
    private final int height;
    private final int animationStyle;
    private PopupWindow mPopupWindow;
    private View mContentView;

    private KPopupWindow(Builder builder) {
        width = builder.width;
        height = builder.height;
        animationStyle = builder.animationStyle;
        mContext = builder.mContext;
        LayoutInflater inflater = LayoutInflater.from(builder.mContext);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.core_popupwindow_layout, null, false);
        mContentView = inflater.inflate(builder.layoutResId, mBinding.coreFlPopContent);
        mBinding.coreBtnContainer.setVisibility(builder.isShowButton ? View.VISIBLE : View.GONE);
    }

    /**
     * 创建PopupWindow
     *
     * @return
     */
    private void create() {
        mPopupWindow = new PopupWindow(mBinding.getRoot(), width, height, true);
        // 设置背景为透明(不设置无法正常获取焦点)
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (animationStyle > 0) {
            // 设置动画
            mPopupWindow.setAnimationStyle(animationStyle);
        }
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setTouchable(true);
        // 更新设置
        mPopupWindow.update();
    }

    /**
     * 初始化事件
     *
     * @param onInitEventListener
     */
    public KPopupWindow initEvent(OnInitEventListener onInitEventListener) {
        onInitEventListener.onInitEvent(mBinding.getRoot());
        return this;
    }

    public KPopupWindow setOnClickListener(OnClickListener onClickListener) {
        return setOnClickListener(mContext.getString(R.string.core_cancel_text), mContext.getString(R.string.core_confirm_text), onClickListener);
    }

    public KPopupWindow setOnClickListener(String leftText, String rightText, OnClickListener onClickListener) {
        mBinding.coreBtnPopRight.setText(rightText);
        mBinding.coreBtnPopLeft.setText(leftText);
        mBinding.coreBtnPopLeft.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onLeftClick(v);
            }
            mPopupWindow.dismiss();
        });
        mBinding.coreBtnPopRight.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onRightClick(v);
            }
            mPopupWindow.dismiss();
        });
        return this;
    }

    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    /**
     * @param anchor        锚点
     * @param layoutGravity 位置
     * @param xOffset       x 偏移量
     * @param yOffset       y 偏移量
     */
    public void showAsAnchor(View anchor, LayoutGravity layoutGravity, int xOffset, int yOffset) {
        int[] offset = layoutGravity.getOffset(anchor, mPopupWindow);
        mPopupWindow.showAsDropDown(anchor, offset[0] + xOffset, offset[1] + yOffset);
    }

    public static class LayoutGravity {
        private int layoutGravity;
        public static final int ALIGN_LEFT = 0x1;
        public static final int ALIGN_ABOVE = 0x2;
        public static final int ALIGN_RIGHT = 0x4;
        public static final int ALIGN_BOTTOM = 0x8;
        public static final int TO_LEFT = 0x10;
        public static final int TO_ABOVE = 0x20;
        public static final int TO_RIGHT = 0x40;
        public static final int TO_BOTTOM = 0x80;
        public static final int CENTER_HORI = 0x100;
        public static final int CENTER_VERT = 0x200;

        public LayoutGravity(int gravity) {
            layoutGravity = gravity;
        }

        public int getLayoutGravity() {
            return layoutGravity;
        }

        public void setLayoutGravity(int gravity) {
            layoutGravity = gravity;
        }

        public void setHorizontalGravity(int gravity) {
            layoutGravity &= (0x2 + 0x8 + 0x20 + 0x80 + 0x200);
            layoutGravity |= gravity;
        }

        public void setVerticalGravity(int gravity) {
            layoutGravity &= (0x1 + 0x4 + 0x10 + 0x40 + 0x100);
            layoutGravity |= gravity;
        }

        public boolean isParamFit(int param) {
            return (layoutGravity & param) > 0;
        }

        public int getHorizontalParam() {
            for (int i = 0x1; i <= 0x100; i = i << 2) {
                if (isParamFit(i)) {
                    return i;
                }
            }
            return ALIGN_LEFT;
        }

        public int getVerticalParam() {
            for (int i = 0x2; i <= 0x200; i = i << 2) {
                if (isParamFit(i)) {
                    return i;
                }
            }
            return TO_BOTTOM;
        }

        public int[] getOffset(View anchor, PopupWindow window) {
            int anchWidth = anchor.getWidth();
            int anchHeight = anchor.getHeight();

            int winWidth = window.getWidth();
            int winHeight = window.getHeight();
            View view = window.getContentView();
            if (winWidth <= 0) {
                winWidth = view.getWidth();
            }
            if (winHeight <= 0) {
                winHeight = view.getHeight();
            }

            int xoff = 0;
            int yoff = 0;

            switch (getHorizontalParam()) {
                case ALIGN_LEFT:
                    xoff = 0;
                    break;
                case ALIGN_RIGHT:
                    xoff = anchWidth - winWidth;
                    break;
                case TO_LEFT:
                    xoff = -winWidth;
                    break;
                case TO_RIGHT:
                    xoff = anchWidth;
                    break;
                case CENTER_HORI:
                    xoff = (anchWidth - winWidth) / 2;
                    break;
                default:
                    break;
            }
            switch (getVerticalParam()) {
                case ALIGN_ABOVE:
                    yoff = -anchHeight;
                    break;
                case ALIGN_BOTTOM:
                    yoff = -winHeight;
                    break;
                case TO_ABOVE:
                    yoff = -anchHeight - winHeight;
                    break;
                case TO_BOTTOM:
                    yoff = 0;
                    break;
                case CENTER_VERT:
                    yoff = (-winHeight - anchHeight) / 2;
                    break;
                default:
                    break;
            }
            return new int[]{xoff, yoff};
        }
    }

    public static class Builder {
        private Context mContext;
        private int layoutResId;
        private int width = 0;
        private int height = 0;
        private int animationStyle = R.style.CorePopupWindowAnim;
        private boolean isShowButton = false;

        public Builder with(@NonNull Context mContext) {
            this.mContext = mContext.getApplicationContext();
            return this;
        }

        public Builder layoutResId(@LayoutRes int layoutResId) {
            this.layoutResId = layoutResId;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder animationStyle(int animationStyle) {
            this.animationStyle = animationStyle;
            return this;
        }

        public Builder showButton(boolean isShowButton) {
            this.isShowButton = isShowButton;
            return this;
        }

        public KPopupWindow build() {
            KPopupWindow kPopupWindow = new KPopupWindow(this);
            kPopupWindow.create();
            return kPopupWindow;
        }
    }
}
