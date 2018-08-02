package com.cnksi.android.base;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.cnksi.android.R;
import com.cnksi.android.databinding.CoreDialogLayoutBinding;
import com.cnksi.android.utils.StringUtil;

/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/8/2
 * @since 1.0
 */
public abstract class BaseDialog extends DialogFragment {

    /**
     * 控制button显示
     */
    public interface ButtonVisible {
        int LEFT = 1;
        int RIGHT = 2;
        int BOTH = 3;
    }

    protected CoreDialogLayoutBinding mRootBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootBinding = DataBindingUtil.inflate(inflater, R.layout.core_dialog_layout, container, false);
        initView();
        mRootBinding.coreFlDialogContent.addView(getContentView(inflater, container));
        return mRootBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    protected void initView() {
        mRootBinding.coreBtnDialogRight.setOnClickListener(this::onRightButtonClick);
        mRootBinding.coreBtnDialogLeft.setOnClickListener(this::onLeftButtonClick);
        mRootBinding.coreTvDialogTitle.setText(StringUtil.fromNull(getTitle()));
        mRootBinding.coreBtnDialogLeft.setText(StringUtil.fromNull(getLeftButtonText()));
        mRootBinding.coreBtnDialogRight.setText(StringUtil.fromNull(getRigthButtonText()));
        mRootBinding.coreBtnDialogLeft.setVisibility(getButtonVisible() == ButtonVisible.BOTH ?
                View.VISIBLE : getButtonVisible() == ButtonVisible.LEFT ? View.VISIBLE : View.GONE);
        mRootBinding.coreBtnDialogRight.setVisibility(getButtonVisible() == ButtonVisible.BOTH ?
                View.VISIBLE : getButtonVisible() == ButtonVisible.RIGHT ? View.VISIBLE : View.GONE);
    }

    protected abstract CharSequence getTitle();

    protected abstract CharSequence getLeftButtonText();

    protected abstract CharSequence getRigthButtonText();

    protected abstract View getContentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    protected abstract void onRightButtonClick(View view);

    protected abstract void onLeftButtonClick(View view);

    protected int getButtonVisible() {
        return ButtonVisible.BOTH;
    }
}
