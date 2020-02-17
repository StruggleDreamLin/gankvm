package com.dreamlin.gankvm.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dreamlin.gankvm.base.model.BaseViewModel;
import com.dreamlin.gankvm.entity.ResultsEntity;
import com.dreamlin.gankvm.retrofit.NetHelper;
import com.dreamlin.gankvm.retrofit.RxHelper;

import java.util.List;

import io.reactivex.disposables.Disposable;

import static com.dreamlin.gankvm.ui.home.HomeFragment.PAGE_SIZE;

public class HomeViewModel extends BaseViewModel {

    MutableLiveData<List<ResultsEntity>> mResultsEntities;

    public HomeViewModel() {
        mResultsEntities = new MutableLiveData<>();
    }

    public LiveData<List<ResultsEntity>> getAlls(int page) {
        Disposable subscribe = NetHelper.getApi()
                .getAll(page, PAGE_SIZE)
                .compose(RxHelper.applySchedulers(this))
                .subscribe(resultsEnities -> {
                    mResultsEntities.setValue(resultsEnities);
                }, throwable -> throwable.printStackTrace());
        addDisposable(subscribe);
        return mResultsEntities;
    }
}