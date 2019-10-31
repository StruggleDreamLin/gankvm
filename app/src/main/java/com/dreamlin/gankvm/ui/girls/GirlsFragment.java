package com.dreamlin.gankvm.ui.girls;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dreamlin.gankvm.R;
import com.dreamlin.gankvm.base.ui.BaseAdapter;
import com.dreamlin.gankvm.base.ui.BaseFragment;
import com.dreamlin.gankvm.base.ui.BaseViewHolder;
import com.dreamlin.gankvm.entity.ResultsEntity;
import com.dreamlin.gankvm.ui.home.MultiAdapter;
import com.dreamlin.gankvm.widget.RadioImageView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GirlsFragment extends BaseFragment<GirlsViewModel> implements BaseAdapter.AdapterListener<ResultsEntity> {

    @BindView(R.id.m_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.m_smart_refresh_layout)
    SmartRefreshLayout mSmartRefreshLayout;
    private BaseAdapter<ResultsEntity> mAdapter;
    private List<ResultsEntity> mDatas = new ArrayList<>();
    private int page = 1;

    @Override
    protected void initData() {
        viewModel.getGirls(page).observe(this, resultsEnities -> {
            if (mSmartRefreshLayout.isLoading())
                mSmartRefreshLayout.finishLoadMore(1000);
            else if (mSmartRefreshLayout.isRefreshing())
                mSmartRefreshLayout.finishRefresh(1000);
            if (resultsEnities != null && resultsEnities.size() > 0) {
                mDatas.addAll(resultsEnities);
                mAdapter.notifyDataSetChanged();
            } else {
                if (page > 0) page--;
            }
        });
    }

    @Override
    protected void initViews() {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mAdapter = new MultiAdapter(mDatas, R.layout.item_girl);
        mAdapter.setListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.getGirls(++page);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mDatas.clear();
                page = 1;
                viewModel.getGirls(page);
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    protected GirlsViewModel getViewModel() {
        return ViewModelProviders.of(this).get(GirlsViewModel.class);
    }

    @Override
    public void convert(BaseViewHolder<ResultsEntity> holder, ResultsEntity bean) {
        final RadioImageView ivGirl = holder.getView(R.id.iv_girl);
        ivGirl.setOriginalSize(20, 20);
        if (!TextUtils.isEmpty(bean.getUrl())) {
            Glide.with(holder.itemView)
                    .clear(ivGirl);
            Glide.with(holder.itemView)
                    .load(bean.getUrl())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                            .dontAnimate()
                            .centerCrop())
                    .into(ivGirl);
        }
        ivGirl.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FaceActivity.class);
            intent.putExtra(FaceActivity.FACE_TAG, bean);
            //这里用Activity好一些吧
            startActivity(intent);
        });
    }

    @OnClick(R.id.m_float_action_btn)
    public void onViewClicked() {
        mRecyclerView.smoothScrollToPosition(0);
    }
}