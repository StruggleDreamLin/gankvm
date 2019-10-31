package com.dreamlin.gankvm.base.ui;


import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dreamlin.gankvm.R;
import com.dreamlin.gankvm.utils.DensityUtil;

public class LoadingDialog extends Dialog {

    private final TextView tvMsg;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.LoadingDialog);
        setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null, false);
        tvMsg = view.findViewById(R.id.tv_msg);
        setContentView(view);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = DensityUtil.dip2px(context, 150);
        lp.height = DensityUtil.dip2px(context, 110);
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
    }

    /**
     * 设置等待提示信息
     */
    public void setLoadingMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        tvMsg.setText(msg);
    }
}
