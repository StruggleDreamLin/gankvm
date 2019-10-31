package com.dreamlin.gankvm.retrofit;

import com.dreamlin.gankvm.BuildConfig;
import com.dreamlin.gankvm.retrofit.cstgson.CstGsonConverterFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public enum RetrofitHelper {

    INSTANCE;
    private OkHttpClient mOkHttpClient;

    RetrofitHelper() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        //公共参数这里可以添加拦截器
        builder.addInterceptor(new CommonParamsInterceptor());
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        mOkHttpClient = builder.build();
    }

    public Retrofit newRetrofit(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(CstGsonConverterFactory.create())
                .client(mOkHttpClient)
                .build();
        return retrofit;
    }

    public Retrofit downloadRetrofit(String url){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .client(mOkHttpClient)
                .build();
        return retrofit;
    }
}
