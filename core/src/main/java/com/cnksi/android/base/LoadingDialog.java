package com.cnksi.android.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.cnksi.android.R;
import com.cnksi.android.databinding.CoreLoadingLayoutBinding;
import com.cnksi.android.utils.ScreenUtil;
import com.cnksi.android.utils.StringUtil;

import java.util.HashMap;

/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/8/6
 * @since 1.0
 */
public class LoadingDialog extends AppCompatDialogFragment {

    private static final HashMap<Object, LoadingDialog> DIALOG_MAP = new HashMap<>(5);

    protected CoreLoadingLayoutBinding mBinding;

    private CharSequence mLodingContent;

    private int orientation = LinearLayout.VERTICAL;

    private Object tag;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.core_loading_layout, container, false);
        mBinding.coreTvLoadingContent.setText(StringUtil.fromNull(mLodingContent));
        mBinding.coreLoadingContainer.setOrientation(orientation);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            params.windowAnimations = R.style.CoreLoadingAnim;
            window.setAttributes(params);
            if (orientation == LinearLayout.HORIZONTAL) {
                window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mBinding.coreTvLoadingContent.setPadding(0, 0, getResources().getDimensionPixelSize(R.dimen.core_loading_padding), 0);
            } else {
                int width = ScreenUtil.getScreenWidth(getResources()) * 2 / 5;
                window.setLayout(width, width);
            }
        }
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnShowListener(dialog -> {
            mBinding.coreLoadingView.start();
            mBinding.coreTvLoadingContent.setText(mLodingContent);
        });
        getDialog().setOnDismissListener(dialog -> {
            if (DIALOG_MAP.get(tag) != null) {
                DIALOG_MAP.remove(tag);
            }
        });
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    /**
     * 设置加载文字
     *
     * @param loadingContent
     */
    private void setLoadingContent(CharSequence loadingContent) {
        this.mLodingContent = loadingContent;
        if (mBinding != null) {
            mBinding.coreTvLoadingContent.setText(loadingContent);
        }
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    /**
     * 显示Dialog
     *
     * @param activity
     * @return
     */
    public static LoadingDialog show(FragmentActivity activity) {
        return show(activity.getString(R.string.core_loading_content), activity);
    }

    /**
     * 显示Dialog
     *
     * @param activity
     * @return
     */
    public static LoadingDialog show(CharSequence loadingContent, FragmentActivity activity) {
        return show(loadingContent, activity, LinearLayout.VERTICAL);
    }

    /**
     * 显示Dialog
     *
     * @param loadingContent
     * @param activity
     * @return
     */
    public static LoadingDialog showHorizontal(CharSequence loadingContent, FragmentActivity activity) {
        return show(loadingContent, activity, LinearLayout.HORIZONTAL);
    }

    /**
     * 显示Dialog
     *
     * @param activity
     * @return
     */
    private static LoadingDialog show(CharSequence loadingContent, FragmentActivity activity, int orientation) {
        LoadingDialog dialog = DIALOG_MAP.get(activity);
        if (dialog == null) {
            DIALOG_MAP.put(activity, dialog = new LoadingDialog());
            dialog.setTag(activity);
        }
        dialog.setLoadingContent(loadingContent);
        dialog.setOrientation(orientation);
        if (!dialog.isAdded()) {
            dialog.show(activity.getSupportFragmentManager(), activity.getLocalClassName());
        }
        return dialog;
    }

}
