package com.dreamlin.gankvm.base.ui;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder<B> extends RecyclerView.ViewHolder {

    public View itemView;

    public SparseArray<View> views;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.views = new SparseArray<>();
    }

    public <T extends View> T getView(@IdRes int id) {
        View view = views.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }
        return (T) view;
    }

    public void setTitle(@IdRes int id, String content) {
        View view = getView(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(content);
        } else {
            throw new ClassCastException("you should input a text view id");
        }
    }

    protected void bindItem(B bean) {

    }

}
