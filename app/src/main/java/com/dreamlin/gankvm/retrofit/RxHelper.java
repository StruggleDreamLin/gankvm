package com.dreamlin.gankvm.retrofit;

import com.dreamlin.gankvm.base.model.BaseViewModel;
import com.dreamlin.gankvm.base.model.DialogBean;
import com.dreamlin.gankvm.base.ui.NoVMFragment;
import com.dreamlin.gankvm.excrptions.rxweaver.core.GlobalErrorTransformer;
import com.dreamlin.gankvm.excrptions.rxweaver.func.Suppiler;
import com.dreamlin.gankvm.excrptions.rxweaver.retry.RetryConfig;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxHelper {

    public static <T> ObservableTransformer<T, T> applySchedulers(BaseViewModel viewModel) {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> viewModel.setShow(DialogBean.getInstance()))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    DialogBean.getInstance().setShow(false);
                    viewModel.setShow(DialogBean.getInstance());
                })
                .compose(handleGlobalError(viewModel));
    }

    public static <T> GlobalErrorTransformer<T> handleGlobalError(BaseViewModel viewModel) {

        return new GlobalErrorTransformer<>(

                // 通过onNext流中数据的状态进行操作
                results -> {
//                    switch (results.code) {
//                        case STATUS_REQUEST_FAIL:
//                            return Observable.error(new UniteException(tUnityBean.code, tUnityBean.message));
//                        case STATUS_CONNECT_FAIL:
//                            return Observable.error(new ConnectServerException(tUnityBean.code, tUnityBean.message));
//                        case STATUA_OK:
//                            return Observable.just(tUnityBean);
//
//                    }
                    return Observable.just(results);
                },

                // 通过onError中Throwable状态进行操作
                error -> {
//                    if (error instanceof ConnectServerException) {
//                        return Observable.error(new ConnectFailedAlertDialogException());
//                    }
                    return Observable.error(error);
                },

                new Function<Throwable, RetryConfig>() {
                    @Override
                    public RetryConfig apply(Throwable error) throws Exception {

//                        if (error instanceof ConnectServerException) {
//                            return new RetryConfig(
//                                    new Suppiler<Single<Boolean>>() {
//                                        @Override
//                                        public Single<Boolean> call() {
//                                            return RxDialog.showErrorDialog((BaseActivity) mView, "ConnectServerException")
//                                                    .flatMap(new Function<Boolean, SingleSource<? extends Boolean>>() {
//                                                        @Override
//                                                        public SingleSource<? extends Boolean> apply(Boolean retry) throws Exception {
//                                                            return Single.just(retry);
//                                                        }
//                                                    });
//                                        }
//                                    });
//                        }

                        if (error instanceof SocketTimeoutException) {
                            return new RetryConfig(1, 3000,     // 最多重试1次，延迟3000ms
                                    new Suppiler<Single<Boolean>>() {
                                        @Override
                                        public Single<Boolean> call() {
                                            return Single.create(emitter -> {
                                                emitter.onSuccess(true);
                                            });
                                        }
                                    }
                            );
                        }

                        return new RetryConfig();   // 其它异常都不重试
                    }
                },

                new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //确保这里是UI线程，可以直接处理全局异常
                        throwable.printStackTrace();
                        DialogBean.getInstance().setShow(false);
                        viewModel.setShow(DialogBean.getInstance());
                        if (throwable instanceof JSONException) {
                            viewModel.setError("数据解析异常");
                        } else if (throwable instanceof ConnectException) {
                            //判断是无网络异常，还是服务器异常
                        } else if (throwable instanceof SocketTimeoutException) {
                            viewModel.setError("服务器异常");
                        } else if (throwable instanceof UniteException) {
                            UniteException uniteException = (UniteException) throwable;
                            if (uniteException.getCode() == 201) {
                                viewModel.setError(uniteException.getMessage());
                            } else if (uniteException.getCode() == 202) {
                                viewModel.setError("服务器异常");
                            }
                        } else {

                        }
                    }
                }
        );
    }

}
