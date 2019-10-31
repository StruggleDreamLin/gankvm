package com.dreamlin.gankvm.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dreamlin.gankvm.R;
import com.dreamlin.gankvm.base.ui.BaseAdapter;
import com.dreamlin.gankvm.base.ui.BaseViewHolder;
import com.dreamlin.gankvm.entity.ResultsEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MultiAdapter extends BaseAdapter<ResultsEntity> {

    public MultiAdapter(List<ResultsEntity> datas, int layoutId) {
        super(datas, layoutId);
    }

    @NonNull
    @Override
    public BaseViewHolder<ResultsEntity> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == Type.HEADER.ordinal()) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == Type.GIRL.ordinal()) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_girl, parent, false);
            return new BaseViewHolder<>(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_others, parent, false);
            return new OthersViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<ResultsEntity> holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bindItem(mDatas.get(position));
        } else if (holder instanceof OthersViewHolder) {
            ((OthersViewHolder) holder).bindItem(mDatas.get(position));
        }
        super.onBindViewHolder(holder, position);
    }

    enum Type {
        HEADER,
        GIRL,
        OTHER
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.get(position).isHeader)
            return Type.HEADER.ordinal();
        else if (mDatas.get(position).isGirls())
            return Type.GIRL.ordinal();
        return Type.OTHER.ordinal();
    }

    public static
    class HeaderViewHolder extends BaseViewHolder<ResultsEntity> {
        @BindView(R.id.tv_header)
        TextView tvHeader;

        HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        protected void bindItem(ResultsEntity bean) {
            super.bindItem(bean);
            tvHeader.setText(bean.getType());
        }
    }

    public static
    class OthersViewHolder extends BaseViewHolder<ResultsEntity> {
        @BindView(R.id.iv_cate)
        ImageView ivCate;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;

        public OthersViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void bindItem(ResultsEntity bean) {
            super.bindItem(bean);
            tvContent.setText(bean.getDesc());
            tvTitle.setText(bean.getType());
        }
    }
}
