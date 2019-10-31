package com.dreamlin.gankvm.base.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import butterknife.ButterKnife;

public abstract class NoVMFragment extends Fragment {

    protected LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = LayoutInflater.from(getContext()).inflate(getLayout(), container, false);
        ButterKnife.bind(this, view);
        initViews();
        initData();
        return view;
    }

    protected abstract void initViews();

    protected abstract void initData();

    protected abstract @LayoutRes
    int getLayout();

    protected @MenuRes
    int getMenu() {
        return -0x01;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getMenu() != -0x01) {
            inflater.inflate(getMenu(), menu);
        } else
            super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    protected void toastMessageShort(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    protected void toastMessageShort(@StringRes int strId) {
        Toast.makeText(getActivity(), strId, Toast.LENGTH_SHORT).show();
    }

    protected void toastMessageLong(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    protected void toastMessageLong(@StringRes int strId) {
        Toast.makeText(getActivity(), strId, Toast.LENGTH_LONG).show();
    }

    protected void showMessageShort(String message) {
        Snackbar.make(getActivity().getWindow().getDecorView(), message, Snackbar.LENGTH_SHORT);
    }

    protected void showMessageShort(@StringRes int messageId) {
        Snackbar.make(getActivity().getWindow().getDecorView(), getText(messageId), Snackbar.LENGTH_SHORT);
    }

    protected void showMessageLong(String message) {
        Snackbar.make(getActivity().getWindow().getDecorView(), message, Snackbar.LENGTH_LONG);
    }

    protected void showMessageLong(@StringRes int messageId) {
        Snackbar.make(getActivity().getWindow().getDecorView(), getText(messageId), Snackbar.LENGTH_LONG);
    }

    public void showLoading() {
        showLoading("数据加载中...");
    }

    public void showLoading(String msg) {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
            loadingDialog.setLoadingMsg(msg);
        } else {
            loadingDialog = new LoadingDialog(getContext());
            loadingDialog.show();
            loadingDialog.setLoadingMsg(msg);
        }
    }

    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

}
