package com.dreamlin.gankvm.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 可以使用这个拦截器做一些预处理、缓存之类的
 */
public class CommonParamsInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder newBuilder = chain.request().newBuilder();
//        newBuilder.addHeader("app", "BTRH");
        Request request = newBuilder.build();

        return chain.proceed(request);
    }
}
