package com.cnksi.sample.dialog;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.cnksi.android.dialog.BaseDialog;
import com.cnksi.sample.R;
import com.cnksi.sample.databinding.StepProgressViewLayoutBinding;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/8/2
 * @since 1.0
 */
public class StepProgressViewDialog extends BaseDialog {

    private StepProgressViewLayoutBinding mBinding;

    @Override
    protected void initView() {
        super.initView();
        final ArrayList<CharSequence> list = new ArrayList<>();
        list.add("施工单位");
        list.add("监理单位");
        list.add("业主单位");
        list.add("建设部备案");

        mBinding.stepView.initData(list, 3);
        mBinding.btnChange.setOnClickListener(v -> {
            String number = mBinding.etStep.getText().toString();
            mBinding.stepView.setCurrentStep(Integer.parseInt(TextUtils.isEmpty(number) ? "0" : number));
        });
        mBinding.btnAdd.setOnClickListener(v -> {
            list.add("建设部备案2");
            list.add("建设部备案3");
            mBinding.stepView.setStepList(list);
        });
    }

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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.step_progress_view_layout, container, false);
        return mBinding.getRoot();
    }

    @Override
    protected void onRightButtonClick(View view) {
        Toasty.info(getContext(), "Right").show();
        dismiss();
    }

    @Override
    protected void onLeftButtonClick(View view) {
        Toasty.info(getContext(), "Left").show();
        dismiss();
    }

    @Override
    public boolean isBackCancelable() {
        return false;
    }

    @Override
    protected int getButtonVisible() {
        return ButtonVisible.BOTH;
    }

    @Override
    protected WindowManager.LayoutParams getLayoutParams(WindowManager.LayoutParams params) {
//        params.windowAnimations = R.style.dialog_animation;
        return params;
    }
}
