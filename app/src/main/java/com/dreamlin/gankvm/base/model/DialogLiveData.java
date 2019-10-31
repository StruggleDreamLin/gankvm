package com.dreamlin.gankvm.base.model;

import androidx.lifecycle.LiveData;

public class DialogLiveData extends LiveData<DialogBean> {

    DialogBean dialogBean;

    @Override
    protected void setValue(DialogBean value) {
        dialogBean = value;
        super.setValue(value);
    }
}
