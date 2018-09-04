package com.cnksi.sample.activity;

import android.view.View;

import com.cnksi.android.activity.BaseLoginActivity;
import com.cnksi.sample.BuildConfig;

import es.dmoral.toasty.Toasty;

/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/9/4
 * @since 1.0
 */
public class LoginActivity extends BaseLoginActivity {

    @Override
    protected void initData() {

    }

    @Override
    protected CharSequence getAppName() {
        return "风险管控移动作业终端";
    }

    @Override
    protected CharSequence getVersionText() {
        return getString(com.cnksi.android.R.string.login_label_version_format, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
    }

    @Override
    protected void onLoginButtonClick(View v) {
        Toasty.info(mActivity, getAccount() + " " + getPassword()).show();
        getAccount();
    }

    @Override
    protected void onVersionFastClick(View v) {
        Toasty.info(mActivity, "连续点击").show();
    }

    @Override
    protected String[] getPermission() {
        return new String[0];
    }
}
