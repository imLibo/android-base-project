package com.cnksi.android.fragment;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnksi.android.log.KLog;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;


/**
 * Fragment基类
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/9/30
 * @since 1.0
 */
public abstract class BaseCoreFragment<T extends ViewDataBinding> extends Fragment {

    /**
     * 自定义Handler
     */
    public static class CoreHandler extends Handler {
        WeakReference<BaseCoreFragment> mFragment;

        public CoreHandler(BaseCoreFragment mFragment) {
            this.mFragment = new WeakReference<>(mFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseCoreFragment mCurrentFragment = mFragment.get();
            if (mCurrentFragment != null) {
                mCurrentFragment.handleMessage(msg);
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

    protected Activity mActivity = null;
    protected Bundle bundle = null;
    protected Fragment mFragment = null;
    protected T mBinding;
    /**
     * fragment管理器
     */
    protected FragmentManager mFManager = null;
    protected LayoutInflater mInflater = null;
    /**
     * 自定义Handler
     */
    protected CoreHandler mHandler = null;
    /**
     * 是否可见的标志
     */
    protected boolean isVisible;
    /**
     * 是否是第一次加载
     */
    protected boolean isFirstLoad = true;
    /**
     * 是否已经准备好
     */
    protected boolean isPrepared = false;
    /**
     * 是否是
     */
    protected boolean isOnDetach = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = getActivity();
        this.mFragment = this;
        mHandler = initHandler() ? new CoreHandler(this) : null;
        isOnDetach = false;
        bundle = getArguments();
        mInflater = LayoutInflater.from(getActivity());
        mFManager = getChildFragmentManager();
    }

    /**
     * 是否初始化Handler
     */
    public boolean initHandler() {
        return false;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false);
        View view = mBinding.getRoot();
        initUI();
        onVisible();
        return view;
    }

    /**
     * 初始化控件
     */
    @CallSuper
    protected void initUI() {
        isPrepared = true;
    }

    /**
     * fragment布局文件
     */
    @LayoutRes
    public abstract int getLayoutResId();

    /**
     * 设置是否是第一次加载 用于刷新数据
     *
     * @param isFirstLoad true 第一次加载
     */
    public void setFirstLoad(boolean isFirstLoad) {
        this.isFirstLoad = isFirstLoad;
    }

    /**
     * 在这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser 是否对用户可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见的时候
     */
    private void onVisible() {
        if (isFirstLoad && isVisible && isPrepared) {
            lazyLoad();
            isFirstLoad = false;
        }
    }

    /**
     * 不可见时
     */
    protected void onInvisible() {
    }

    /**
     * 延迟加载
     * 需要在
     */
    protected abstract void lazyLoad();

    @Override
    public void onDetach() {
        super.onDetach();
        isOnDetach = true;
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            KLog.e(e);
        }
        removeHandlerMessages();
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
