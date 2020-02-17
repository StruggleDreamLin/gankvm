package com.dreamlin.gankvm.ui.girls;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dreamlin.gankvm.base.model.BaseViewModel;
import com.dreamlin.gankvm.entity.ResultsEntity;
import com.dreamlin.gankvm.retrofit.NetHelper;
import com.dreamlin.gankvm.retrofit.RxHelper;

import java.util.List;

import io.reactivex.disposables.Disposable;

import static com.dreamlin.gankvm.ui.home.HomeFragment.PAGE_SIZE;

public class GirlsViewModel extends BaseViewModel {

    MutableLiveData<List<ResultsEntity>> mGirls;

    public GirlsViewModel() {
        mGirls = new MutableLiveData<>();
    }

    public LiveData<List<ResultsEntity>> getGirls(int page) {

        Disposable subscribe = NetHelper.getApi()
                .getGirls(page, PAGE_SIZE)
                .compose(RxHelper.applySchedulers(this))
                .subscribe(resultsEnities -> mGirls.setValue(resultsEnities),
                        throwable -> throwable.printStackTrace());
        addDisposable(subscribe);
        return mGirls;
    }
}