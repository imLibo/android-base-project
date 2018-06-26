package com.cnksi.android.permission;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/6/26
 * @since 1.0
 */
public final class PermissionSetting {

    public interface PermissionCallback {

        void onGranted(List<String> permissions);

        void onDenied(List<String> permissions);
    }

    private final Activity mActivity;
    private PermissionCallback mCallback;

    public PermissionSetting(Activity activity) {
        this.mActivity = activity;
        if (activity instanceof PermissionCallback) {
            mCallback = (PermissionCallback) mActivity;
        } else {
            throw new RuntimeException("Activity 请实现 PermissionCallback 接口");
        }
    }

    /**
     * 请求权限
     */
    public void requestPermission(String... permissions) {
        AndPermission.with(mActivity)
                .runtime()
                .permission(permissions)
                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        mCallback.onGranted(permissions);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(mActivity, permissions)) {
                            showSetting(permissions);
                        } else {
                            mCallback.onDenied(permissions);
                        }
                    }
                })
                .start();
    }

    /**
     * 显示权限提示
     *
     * @param permissions 未授权权限
     */
    private void showSetting(final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(mActivity, permissions);
        String message = "您未授权 " + TextUtils.join(",", permissionNames) + " 权限, 请在权限管理中开启此权限。";
        new AlertDialog.Builder(mActivity)
                .setCancelable(false)
                .setTitle("提示")
                .setMessage(message)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermission(permissions);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCallback.onDenied(permissions);
                    }
                })
                .show();
    }

    /**
     * 跳转到权限设置界面进行权限设置
     *
     * @param permissions 权限
     */
    private void setPermission(final List<String> permissions) {
        AndPermission.with(mActivity)
                .runtime()
                .setting()
                .onComeback(new Setting.Action() {
                    @Override
                    public void onAction() {
                        if (AndPermission.hasPermissions(mActivity, permissions.toArray(new String[]{}))) {
                            mCallback.onGranted(permissions);
                        } else {
                            ArrayList<String> deniedPermissions = new ArrayList<>();
                            for (String permission : permissions) {
                                if (!AndPermission.hasPermissions(mActivity, permission)) {
                                    deniedPermissions.add(permission);
                                }
                            }
                            mCallback.onDenied(deniedPermissions);
                        }
                    }
                })
                .start();
    }
}