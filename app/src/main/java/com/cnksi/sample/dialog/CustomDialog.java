package com.cnksi.sample.dialog;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnksi.android.base.BaseDialog;
import com.cnksi.sample.R;
import com.cnksi.sample.databinding.ContentMainBinding;

import es.dmoral.toasty.Toasty;

/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/8/2
 * @since 1.0
 */
public class CustomDialog extends BaseDialog {

    private ContentMainBinding mBinding;


    @Override
    protected CharSequence getTitle() {
        return "评价结果";
    }

    @Override
    protected CharSequence getLeftButtonText() {
        return "提交结果";
    }

    @Override
    protected CharSequence getRigthButtonText() {
        return "返回修改";
    }

    @Override
    protected View getContentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.content_main, container, false);
        return mBinding.getRoot();
    }

    @Override
    protected void onRightButtonClick(View view) {
        Toasty.info(getContext(), "Confirm").show();
    }

    @Override
    protected void onLeftButtonClick(View view) {
        Toasty.info(getContext(), "Confirm").show();
    }
}
