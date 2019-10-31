package com.dreamlin.gankvm.retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class NetHelper {

    private static final Object locker = new Object();
    private static final String BASE_URL = "http://gank.io/api/";
    private static Api api;

    private static final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    public static Api getApi() {
        synchronized (locker) {
            if (api == null) {
                api = RetrofitHelper.INSTANCE.newRetrofit(BASE_URL)
                        .create(Api.class);
            }
        }
        return api;
    }

    public static RequestBody applyBody(Object params) {
        String json = new Gson().toJson(params);
        return RequestBody.create(MEDIA_TYPE, json);
    }

    /**
     * 没有连接网络
     */
    public static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    public static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    public static final int NETWORK_WIFI = 1;

    public static int getNetWorkState(Context context) {
        // 得到连接管理器对象
        if (context == null)
            return NETWORK_NONE;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }

}
