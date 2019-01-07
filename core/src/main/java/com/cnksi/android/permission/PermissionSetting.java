package com.cnksi.android.permission;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Process;
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
 * 权限请求
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/6/26
 * @since 1.0
 */
public final class PermissionSetting<T extends Activity & PermissionSetting.PermissionCallback> {

    public interface PermissionCallback {

        void onGranted(List<String> permissions);

        void onDenied(List<String> permissions);
    }

    private final T mActivity;


    public PermissionSetting(T activity) {
        this.mActivity = activity;
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
                        mActivity.onGranted(permissions);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(mActivity, permissions)) {
                            showSetting(permissions);
                        } else {
                            mActivity.onDenied(permissions);
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
                        mActivity.onDenied(permissions);
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
                            mActivity.onGranted(permissions);
                        } else {
                            ArrayList<String> deniedPermissions = new ArrayList<>();
                            for (String permission : permissions) {
                                if (!AndPermission.hasPermissions(mActivity, permission)) {
                                    deniedPermissions.add(permission);
                                }
                            }
                            mActivity.onDenied(deniedPermissions);
                        }
                    }
                })
                .start();
    }


    /**
     * 显示退出Dialog
     *
     * @param permissions
     */
    public void showExitDialog(final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(mActivity, permissions);
        String message = "您未授权 " + TextUtils.join(",", permissionNames) + " 权限, 将无法使用系统。";
        new AlertDialog.Builder(mActivity)
                .setCancelable(false)
                .setTitle("提示")
                .setMessage(message)
                .setPositiveButton("重新授权", (dialog, which) -> {
                    requestPermission(permissions.toArray(new String[]{}));
                    dialog.dismiss();
                })
                .setNegativeButton("退出", (dialog, which) -> {
                    System.exit(0);
                    android.os.Process.killProcess(Process.myPid());
                })
                .show();
    }
}