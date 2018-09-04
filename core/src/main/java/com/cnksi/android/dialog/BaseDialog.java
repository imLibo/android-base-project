package com.cnksi.android.dialog;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.cnksi.android.R;
import com.cnksi.android.databinding.CoreDialogLayoutBinding;
import com.cnksi.android.utils.ScreenUtil;
import com.cnksi.android.utils.StringUtil;

/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/8/2
 * @since 1.0
 */
public abstract class BaseDialog extends DialogFragment {

    public interface OnDialogClickListener {
        void onRightClick(View view);

        void onLeftClick(View view);
    }

    protected OnDialogClickListener mOnDialogClickListener;
    protected DialogInterface.OnDismissListener mOnDismissListener;

    /**
     * 控制button显示
     */
    public interface ButtonVisible {
        int LEFT = 1;
        int RIGHT = 2;
        int BOTH = 3;
        int GONE = 4;
    }

    protected CoreDialogLayoutBinding mRootBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootBinding = DataBindingUtil.inflate(inflater, R.layout.core_dialog_layout, container, false);
        mRootBinding.coreFlDialogContent.addView(getContentView(inflater, container));
        initView();
        return mRootBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            params.windowAnimations = R.style.CoreDialogAnim;
            window.setAttributes(getLayoutParams(params));
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int width = ScreenUtil.getScreenWidth(requireActivity());
            window.setLayout(width * 19 / 20, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        getDialog().setCanceledOnTouchOutside(isCanceledOnTouchOutside());
        getDialog().setCancelable(isBackCancelable());
        if (mOnDismissListener != null) {
            getDialog().setOnDismissListener(mOnDismissListener);
        }
    }

    @CallSuper
    protected void initView() {
        mRootBinding.coreIbtnClose.setOnClickListener(v -> dismiss());
        mRootBinding.coreTvDialogTitle.setText(StringUtil.fromNull(getTitle()));
        if (getButtonVisible() == ButtonVisible.GONE) {
            mRootBinding.coreBtnContainer.setVisibility(View.GONE);
        } else {
            mRootBinding.coreBtnDialogRight.setOnClickListener(this::onRightButtonClick);
            mRootBinding.coreBtnDialogLeft.setOnClickListener(this::onLeftButtonClick);
            mRootBinding.coreBtnDialogLeft.setText(StringUtil.fromNull(getLeftButtonText()));
            mRootBinding.coreBtnDialogRight.setText(StringUtil.fromNull(getRigthButtonText()));
            mRootBinding.coreBtnDialogLeft.setVisibility(getButtonVisible() == ButtonVisible.BOTH ?
                    View.VISIBLE : getButtonVisible() == ButtonVisible.LEFT ? View.VISIBLE : View.GONE);
            mRootBinding.coreBtnDialogRight.setVisibility(getButtonVisible() == ButtonVisible.BOTH ?
                    View.VISIBLE : getButtonVisible() == ButtonVisible.RIGHT ? View.VISIBLE : View.GONE);
            if (getButtonVisible() == ButtonVisible.RIGHT) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRootBinding.coreBtnDialogRight.getLayoutParams();
                params.leftMargin = 0;
                mRootBinding.coreBtnDialogRight.setLayoutParams(params);
            }
        }
    }

    protected abstract CharSequence getTitle();

    protected abstract CharSequence getLeftButtonText();

    protected abstract CharSequence getRigthButtonText();

    protected abstract View getContentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    protected void onRightButtonClick(View view) {
        if (mOnDialogClickListener != null) {
            mOnDialogClickListener.onRightClick(view);
        }
        dismiss();
    }

    protected void onLeftButtonClick(View view) {
        if (mOnDialogClickListener != null) {
            mOnDialogClickListener.onLeftClick(view);
        }
        dismiss();
    }

    protected int getButtonVisible() {
        return ButtonVisible.BOTH;
    }

    protected boolean isCanceledOnTouchOutside() {
        return false;
    }

    public boolean isBackCancelable() {
        return true;
    }

    protected WindowManager.LayoutParams getLayoutParams(WindowManager.LayoutParams params) {
        return params;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        this.mOnDismissListener = listener;
    }

    public void show(FragmentManager manager) {
        show(manager, this.getClass().getName());
    }

    public void setOnDialogClickListener(OnDialogClickListener listener){
        this.mOnDialogClickListener = listener;
    }
}
