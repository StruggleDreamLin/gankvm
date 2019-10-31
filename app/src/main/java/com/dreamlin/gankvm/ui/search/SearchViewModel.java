package com.dreamlin.gankvm.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dreamlin.gankvm.base.model.BaseViewModel;
import com.dreamlin.gankvm.entity.ResultsEntity;
import com.dreamlin.gankvm.retrofit.NetHelper;
import com.dreamlin.gankvm.retrofit.RxHelper;

import java.util.List;

import io.reactivex.disposables.Disposable;

import static com.dreamlin.gankvm.ui.home.HomeFragment.PAGE_SIZE;

public class SearchViewModel extends BaseViewModel {

    private MutableLiveData<List<ResultsEntity>> mResultsEnities;

    public SearchViewModel() {
        mResultsEnities = new MutableLiveData<>();
    }

    public void search(String keywords, int page) {

        Disposable subscribe = NetHelper.getApi()
                .search(keywords, page, PAGE_SIZE)
                .compose(RxHelper.applySchedulers(this))
                .subscribe(resultsEnities ->
                                mResultsEnities.setValue(resultsEnities),
                        throwable -> throwable.printStackTrace());
        addDisposable(subscribe);
    }

    public LiveData<List<ResultsEntity>> getSearch() {
        return mResultsEnities;
    }
}