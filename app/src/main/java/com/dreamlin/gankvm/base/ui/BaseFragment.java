package com.dreamlin.gankvm.base.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dreamlin.gankvm.base.model.BaseViewModel;

public abstract class BaseFragment<VM extends BaseViewModel> extends NoVMFragment {

    protected VM viewModel;
    protected boolean isUIVisible = false;
    protected boolean isCreated = false;
    protected boolean hasLoad = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = getViewModel();
        initObserve();
        View view = super.onCreateView(inflater, container, savedInstanceState);
        isCreated = true;
        if (!hasLoad && isUIVisible) {
            lazyLoad();
        }
        return view;
    }

    protected abstract VM getViewModel();

    //懒加载
    protected void lazyLoad() {
        hasLoad = true;
    }

    //刷新加载,每次切换到该界面时都重新记载数据
    protected void refreshLoad() {
        hasLoad = true;
    }

    //对应父NoVMFragment里的initData就是首次加载,create的时候加载

    /**
     * 这里需要注意，一般加载数据 三选一即可
     * initData仅会在Fragment创建时调用一次
     * lazyLoad会在界面可见时调用一次
     * refreshLoad会在界面每次可见时调用
     * 如果希望在Create的时候做一些预处理，比如希望每次都刷新数据，并且需要在创建时添加ViewModel的监听
     * 可以只在initData中添加监听，不要调用super.initData(),这样就不会影响另外两个加载方法的触发
     */
    @Override
    protected void initData() {
        hasLoad = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isUIVisible = isVisibleToUser;
        if (isCreated && !hasLoad && isVisibleToUser)
            lazyLoad();
        if (isCreated && isVisibleToUser)
            refreshLoad();
    }

    protected void initObserve() {
        if (viewModel == null) return;
        viewModel.initShowDialog(this, dialogBean -> {
            if (dialogBean.isShow()) {
                showLoading(dialogBean.getMsg());
            } else {
                hideLoading();
            }
        });

        viewModel.initError(this, error ->
                showMessageShort(error)
        );
    }
}
