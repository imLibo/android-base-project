package com.cnksi.sample.activity;

import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cnksi.android.activity.BaseLoginActivity;
import com.cnksi.android.dialog.KPopupWindow;
import com.cnksi.android.safe.XSSLayoutFactory;
import com.cnksi.sample.BuildConfig;
import com.cnksi.sample.R;

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

        KPopupWindow kPopupWindow = new KPopupWindow
                .Builder()
                .with(mActivity)
                .layoutResId(R.layout.step_progress_view_layout)
                .width(LinearLayout.LayoutParams.WRAP_CONTENT)
                .height(LinearLayout.LayoutParams.WRAP_CONTENT)
                .build()
                .setOnClickListener(new KPopupWindow.OnClickListener() {
                    @Override
                    public void onLeftClick(View view) {

                    }

                    @Override
                    public void onRightClick(View view) {

                    }
                });
        kPopupWindow.initEvent(new KPopupWindow.OnInitEventListener() {
            @Override
            public void onInitEvent(View contentView) {
                Button button = contentView.findViewById(R.id.btn_change);
            }
        });
    }

    @Override
    protected String[] getPermission() {
        return new String[0];
    }

    @Override
    protected XSSLayoutFactory.FilterRegex getFilterRegex() {
        return () -> {
            //通用字符串匹配
            // String regex = "^[a-z0-9A-Z\u4e00-\u9fa5，、。：“”_/.:！？（） \\- . ]+$";
            SparseArray<String> map = new SparseArray<>();
            //限定账号输入框只能输入 字母数字
            map.put(R.id.core_et_account, "^[a-z0-9A-Z]+$");
            //限定密码输入框只能输入 字母数字及给定的符号
            map.put(R.id.core_et_password, "^[a-z0-9A-Z，、。：“”_/.:！？（） \\- . ]+$");
            return map;
        };
    }
}
