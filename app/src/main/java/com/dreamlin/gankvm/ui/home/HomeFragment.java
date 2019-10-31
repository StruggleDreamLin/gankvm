package com.dreamlin.gankvm.ui.home;

import android.content.Intent;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dreamlin.gankvm.R;
import com.dreamlin.gankvm.base.ui.BaseAdapter;
import com.dreamlin.gankvm.base.ui.BaseFragment;
import com.dreamlin.gankvm.base.ui.BaseViewHolder;
import com.dreamlin.gankvm.entity.ResultsEntity;
import com.dreamlin.gankvm.ui.web.WebActivity;
import com.dreamlin.gankvm.widget.RadioImageView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment<HomeViewModel> implements BaseAdapter.AdapterListener<ResultsEntity> {

    @BindView(R.id.m_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.m_smart_refresh_layout)
    SmartRefreshLayout mSmartRefreshLayout;

    private BaseAdapter<ResultsEntity> mAdapter;
    public static RequestOptions OPTIONS = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
            .centerCrop().placeholder(R.mipmap.ic_launcher).dontAnimate();
    private int page = 1;
    public static int PAGE_SIZE = 20;
    private List<ResultsEntity> mDatas = new ArrayList<>();

    @Override
    protected void initData() {
        viewModel.getAlls(page).observe(HomeFragment.this, resultsEnities -> {
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
                }
        );
    }

    @Override
    protected void initViews() {

        mAdapter = new MultiAdapter(mDatas, R.layout.item_girl);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.setListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.getAlls(++page);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mDatas.clear();
                page = 1;
                viewModel.getAlls(page);
            }
        });

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    protected HomeViewModel getViewModel() {
        return ViewModelProviders.of(this).get(HomeViewModel.class);
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
                final ImageView ivCate = ((MultiAdapter.OthersViewHolder) holder).ivCate;
                @DrawableRes int cateResId = R.mipmap.def;
                if (bean.isAndroid()){
                    cateResId = R.mipmap.android;
                }else if (bean.isIOS()){
                    cateResId = R.mipmap.ios;
                }else if (bean.isWeb()){
                    cateResId = R.mipmap.web;
                }else if (bean.isTv()){
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

    @OnClick(R.id.m_float_action_btn)
    public void onViewClicked() {
        mRecyclerView.smoothScrollToPosition(0);
    }
}