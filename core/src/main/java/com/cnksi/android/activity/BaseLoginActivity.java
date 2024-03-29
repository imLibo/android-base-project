package com.cnksi.android.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.View;

import com.cnksi.android.R;
import com.cnksi.android.databinding.CoreActivityLoginBinding;
import com.cnksi.android.utils.ViewUtil;

/**
 * 通用登录界面
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/9/4
 * @since 1.0
 */
public abstract class BaseLoginActivity extends BaseCoreActivity {

    protected CoreActivityLoginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(mActivity, R.layout.core_activity_login);
        initView();
        initData();
    }

    @CallSuper
    protected void initView() {
        mBinding.tvAppname.setText(getAppName());
        mBinding.btnLogin.setText(getLoginButtonText());
        mBinding.btnLeft.setText(getLeftButtonText());
        mBinding.tvVersionInfo.setText(getVersionText());
        mBinding.btnLogin.setOnClickListener(this::onLoginButtonClick);
        mBinding.btnLeft.setOnClickListener(this::onLeftButtonClick);
        ViewUtil.setFastClickView(mBinding.tvVersionInfo, this::onVersionFastClick);
    }

    protected abstract void initData();

    protected abstract CharSequence getAppName();

    protected CharSequence getLoginButtonText() {
        return getString(R.string.login_button_text);
    }

    protected CharSequence getLeftButtonText() {
        return getString(R.string.reset_button_text);
    }

    protected abstract CharSequence getVersionText();

    protected abstract void onLoginButtonClick(View v);

    protected void onLeftButtonClick(View v) {
        mBinding.etAccount.setText("");
        mBinding.etPassword.setText("");
    }

    protected abstract void onVersionFastClick(View v);

    protected String getAccount() {
        return mBinding.etAccount.getText().toString().trim();
    }

    protected String getPassword() {
        return mBinding.etPassword.getText().toString().trim();
    }
}
