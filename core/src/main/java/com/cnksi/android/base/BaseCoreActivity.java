package com.cnksi.android.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cnksi.android.permission.PermissionSetting;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/6/26
 * @since 1.0
 */
public abstract class BaseCoreActivity extends AppCompatActivity implements PermissionSetting.PermissionCallback {

    protected BaseCoreActivity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        requestPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 完全退出应用
     */
    protected void compeletlyExitSystem() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 返回需要授权的权限
     *
     * @return
     */
    protected abstract String[] getPermission();

    /**
     * 授权之后的回调
     *
     * @param permissions
     */
    @Override
    public void onGranted(List<String> permissions) {

    }

    /**
     * 权限拒绝后
     *
     * @param permissions
     */
    @Override
    public void onDenied(@NonNull List<String> permissions) {

    }

    /**
     * 请求权限
     */
    protected void requestPermissions() {
        if (!AndPermission.hasPermissions(mActivity, getPermission())) {
            new PermissionSetting(mActivity).requestPermission(getPermission());
        }
    }
}
