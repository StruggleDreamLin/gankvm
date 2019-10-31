package com.dreamlin.gankvm.ui.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.dreamlin.gankvm.R;
import com.dreamlin.gankvm.base.model.BaseViewModel;
import com.dreamlin.gankvm.base.model.DialogBean;
import com.dreamlin.gankvm.base.ui.BaseActivity;
import com.dreamlin.gankvm.base.ui.NoVMActivity;

import java.lang.ref.WeakReference;

import butterknife.BindView;

public class WebActivity extends BaseActivity {
    @BindView(R.id.m_web_view)
    WebView mWebView;

    String url;
    String title;

    public static final String WEB_URL = "webUrl";
    public static final String WEB_TITLE = "webTitle";

    @Override
    protected void initViews() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        WebSettings settings = mWebView.getSettings();
        //支持适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //隐藏原生缩放控件
        settings.setDisplayZoomControls(false);
        //开启JS支持
        settings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebClient(this));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        url = getIntent().getStringExtra(WEB_URL);
        title = getIntent().getStringExtra(WEB_TITLE);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected BaseViewModel getViewModel() {
        return ViewModelProviders.of(this).get(BaseViewModel.class);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        url = getIntent().getStringExtra(WEB_URL);
        title = getIntent().getStringExtra(WEB_TITLE);
        super.onNewIntent(intent);
        initData();
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(url))
            mWebView.loadUrl(url);
        if (!TextUtils.isEmpty(title)) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_web;
    }

    class WebClient extends WebViewClient {
        WeakReference<WebActivity> weakWeb;

        public WebClient(WebActivity webActivity) {
            weakWeb = new WeakReference<>(webActivity);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            //想在页面开始加载时有操作，在这添加
            if (weakWeb.get() != null) {
                DialogBean.getInstance().setShow(true);
                weakWeb.get().viewModel.setShow(DialogBean.getInstance());
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (weakWeb.get() != null) {
                DialogBean.getInstance().setShow(false);
                weakWeb.get().viewModel.setShow(DialogBean.getInstance());
            }
            //想在页面加载结束时有操作，在这添加
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //返回值是true的时候WebView打开，为false则系统浏览器或第三方浏览器打开。如果要下载页面中的游戏或者继续点击网页中的链接进入下一个网页的话，重写此方法下，不然就会跳到手机自带的浏览器了，而不继续在你这个webview里面展现了
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,

                                    String description, String failingUrl) {

            //想在收到错误信息的时候，执行一些操作，走此方法
            view.loadUrl(failingUrl);
        }
    }

}
