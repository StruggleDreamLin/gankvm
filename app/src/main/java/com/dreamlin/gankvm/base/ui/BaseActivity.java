package com.dreamlin.gankvm.base.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.dreamlin.gankvm.base.model.BaseViewModel;

public abstract class BaseActivity<VM extends BaseViewModel> extends NoVMActivity {

    protected VM viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        viewModel = getViewModel();
        initObserve();
        super.onCreate(savedInstanceState);

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
