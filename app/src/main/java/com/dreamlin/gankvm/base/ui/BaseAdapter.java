package com.dreamlin.gankvm.base.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dreamlin.gankvm.base.ui.BaseViewHolder;

import java.util.List;

public class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {

    protected List<T> mDatas;

    protected @LayoutRes
    int layoutId;
    private AdapterListener<T> mListener;

    public BaseAdapter(List<T> datas, int layoutId) {
        this.mDatas = datas;
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public BaseViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<T> holder, int position) {
        if (mListener != null)
            mListener.convert(holder, mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setListener(AdapterListener<T> listener) {
        this.mListener = listener;
    }

    public interface AdapterListener<T> {
        void convert(BaseViewHolder<T> holder, T bean);
    }
}
