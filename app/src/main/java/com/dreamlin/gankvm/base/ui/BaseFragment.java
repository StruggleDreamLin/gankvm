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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = getViewModel();
        initObserve();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected abstract VM getViewModel();

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
