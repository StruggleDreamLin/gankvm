package com.dreamlin.gankvm.base.model;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseViewModel extends ViewModel {

    CompositeDisposable compositeDisposable;

    protected DialogLiveData showDialog = new DialogLiveData();

    protected MutableLiveData<String> error = new MutableLiveData<>();

    protected void addDisposable(Disposable disposable) {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public void initShowDialog(LifecycleOwner owner, Observer<DialogBean> observer) {
        showDialog.observe(owner, observer);
    }

    public void initError(LifecycleOwner owner, Observer<String> observer) {
        error.observe(owner, observer);
    }

    public void setShow(DialogBean dialogBean) {
        showDialog.setValue(dialogBean);
    }

    public void setError(String error) {
        this.error.setValue(error);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
        showDialog = null;
        error = null;
    }
}
