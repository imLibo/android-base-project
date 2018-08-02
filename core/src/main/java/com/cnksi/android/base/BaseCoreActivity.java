package com.cnksi.android.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cnksi.android.permission.PermissionSetting;
import com.yanzhenjie.permission.AndPermission;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

/**
 * BaseActivity
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/8/2
 * @since 1.0
 */
public abstract class BaseCoreActivity extends AppCompatActivity implements PermissionSetting.PermissionCallback {

    protected CoreHandler mHandler;
    protected BaseCoreActivity mActivity;

    private static class CoreHandler extends Handler {
        private WeakReference<BaseCoreActivity> mReference;

        public CoreHandler(BaseCoreActivity activity) {
            this.mReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseCoreActivity activity = this.mReference.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }

    /**
     * 处理消息
     *
     * @param msg
     */
    public void handleMessage(Message msg) {
        
    }

    /**
     * 是否初始化Handler
     *
     * @return
     */
    public boolean initHandler() {
        return false;
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
        } else {
            onGranted(Arrays.asList(getPermission()));
        }
    }

    /**
     * 完全退出应用
     */
    protected void compeletlyExitSystem() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mHandler = initHandler() ? new CoreHandler(mActivity) : null;
        requestPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        removeHandlerMessages();
        super.onDestroy();
    }

    /**
     * 移除所有消息
     */
    private void removeHandlerMessages() {
        if (mHandler != null) {
            mHandler.removeCallbacks(null);
        }
    }
}
