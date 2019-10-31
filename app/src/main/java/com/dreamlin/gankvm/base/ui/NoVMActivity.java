package com.dreamlin.gankvm.base.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import butterknife.ButterKnife;

public abstract class NoVMActivity extends AppCompatActivity {

    protected Context mContext;
    protected LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(getLayout());
        ButterKnife.bind(this);
        initViews();
        initData();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getMenu() != -0x01) {
            getMenuInflater().inflate(getMenu(), menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void toastMessageShort(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void toastMessageShort(@StringRes int strId){
        Toast.makeText(this, strId, Toast.LENGTH_SHORT).show();
    }

    protected void toastMessageLong(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected void toastMessageLong(@StringRes int strId){
        Toast.makeText(this, strId, Toast.LENGTH_LONG).show();
    }

    protected void showMessageShort(String message) {
        Snackbar.make(getWindow().getDecorView(), message, Snackbar.LENGTH_SHORT);
    }

    protected void showMessageShort(@StringRes int messageId) {
        Snackbar.make(getWindow().getDecorView(), getText(messageId), Snackbar.LENGTH_SHORT);
    }

    protected void showMessageLong(String message) {
        Snackbar.make(getWindow().getDecorView(), message, Snackbar.LENGTH_LONG);
    }

    protected void showMessageLong(@StringRes int messageId) {
        Snackbar.make(getWindow().getDecorView(), getText(messageId), Snackbar.LENGTH_LONG);
    }

    public void showLoading() {
        showLoading("数据加载中...");
    }

    public void showLoading(String msg) {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
            loadingDialog.setLoadingMsg(msg);
        } else {
            loadingDialog = new LoadingDialog(this);
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
