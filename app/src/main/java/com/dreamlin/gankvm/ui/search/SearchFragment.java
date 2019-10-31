package com.dreamlin.gankvm.ui.search;

import android.app.Service;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dreamlin.gankvm.R;
import com.dreamlin.gankvm.base.ui.BaseAdapter;
import com.dreamlin.gankvm.base.ui.BaseFragment;
import com.dreamlin.gankvm.base.ui.BaseViewHolder;
import com.dreamlin.gankvm.entity.ResultsEntity;
import com.dreamlin.gankvm.ui.home.MultiAdapter;
import com.dreamlin.gankvm.ui.web.WebActivity;
import com.dreamlin.gankvm.widget.RadioImageView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.dreamlin.gankvm.ui.home.HomeFragment.OPTIONS;

public class SearchFragment extends BaseFragment<SearchViewModel> implements BaseAdapter.AdapterListener<ResultsEntity> {


    @BindView(R.id.m_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.m_smart_refresh_layout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.m_edt_search)
    EditText mEdtSearch;
    private int page = 1;
    private List<ResultsEntity> mDatas = new ArrayList<>();
    private MultiAdapter mAdapter;
    private InputMethodManager inputMethodManager;

    @Override
    protected void initData() {
        viewModel.getSearch().observe(this, resultsEnities -> {
            if (mSmartRefreshLayout.isRefreshing())
                mSmartRefreshLayout.finishRefresh(1000);
            else if (mSmartRefreshLayout.isLoading())
                mSmartRefreshLayout.finishLoadMore(1000);
            if (resultsEnities != null && resultsEnities.size() > 0) {
                String lastHeader = mDatas.size() > 0 ? mDatas.get(mDatas.size() - 1).getType() : "";
                for (int i = 0; i < resultsEnities.size(); i++) {
                    if (!resultsEnities.get(i).getType().equals(lastHeader)) {
                        ResultsEntity resultsEntity = new ResultsEntity();
                        resultsEntity.setType(resultsEnities.get(i).getType());
                        resultsEntity.isHeader = true;
                        resultsEnities.add(i, resultsEntity);
                        lastHeader = resultsEntity.getType();
                    } else continue;
                }
                mDatas.addAll(resultsEnities);
                mAdapter.notifyDataSetChanged();
            } else {
                if (page > 1)
                    page--;
                showMessageShort(R.string.no_more_data);
            }
        });
    }

    @Override
    protected void initViews() {
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        mAdapter = new MultiAdapter(mDatas, R.layout.item_girl);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.setListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.search(mEdtSearch.getText().toString().trim(), ++page);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mDatas.clear();
                page = 1;
                viewModel.search(mEdtSearch.getText().toString().trim(), page);
            }
        });
        mEdtSearch.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_SEND || event != null
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (inputMethodManager.isActive()) {
                    //隐藏软键盘
                    inputMethodManager.hideSoftInputFromWindow(mEdtSearch.getApplicationWindowToken(), 0);
                    //TODO:do search
                    page = 1;
                    viewModel.search(mEdtSearch.getText().toString().trim(), page);
                }
                return true;
            } else {
                return false;
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_search;
    }

    @Override
    protected SearchViewModel getViewModel() {
        return ViewModelProviders.of(this).get(SearchViewModel.class);
    }

    @Override
    public void convert(BaseViewHolder<ResultsEntity> holder, ResultsEntity bean) {
        if (holder instanceof MultiAdapter.HeaderViewHolder) {

        } else {

            if (holder instanceof MultiAdapter.OthersViewHolder) {
                holder.itemView.setOnClickListener(v -> {
                    // jump webView
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra(WebActivity.WEB_TITLE, bean.getDesc());
                    intent.putExtra(WebActivity.WEB_URL, bean.getUrl());
                    startActivity(intent);
                });
                //cate image
                final ImageView ivCate = holder.getView(R.id.iv_cate);
                @DrawableRes int cateResId = R.mipmap.def;
                if (bean.isAndroid()) {
                    cateResId = R.mipmap.android;
                } else if (bean.isIOS()) {
                    cateResId = R.mipmap.ios;
                } else if (bean.isWeb()) {
                    cateResId = R.mipmap.web;
                } else if (bean.isTv()) {
                    cateResId = R.mipmap.tv;
                }
                Glide.with(getActivity())
                        .load(cateResId)
                        .apply(new RequestOptions().circleCrop()).into(ivCate);
            } else {
                holder.itemView.setOnClickListener(v -> {
                    //jump Face
                });
                final RadioImageView ivGirl = holder.getView(R.id.iv_girl);
                ivGirl.setOriginalSize(50, 50);
                Glide.with(getActivity())
                        .load(bean.getUrl())
                        .apply(OPTIONS)
                        .into(ivGirl);
            }
        }
    }
}