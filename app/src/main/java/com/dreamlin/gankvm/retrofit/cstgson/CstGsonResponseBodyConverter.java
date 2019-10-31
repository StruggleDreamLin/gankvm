package com.dreamlin.gankvm.retrofit.cstgson;

import com.dreamlin.gankvm.retrofit.SuperBean;
import com.dreamlin.gankvm.retrofit.UniteException;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by dreamlin on 2018/1/2.
 */


final class CstGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    CstGsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        /**
         * 这里解析报错 由于后台返回数据格式不统一
         */
        try {
            //responseBody只能调用一次 调用后stream就关闭了
//            String string = value.string();
            SuperBean superBean = gson.fromJson(value.charStream(),
                    $Gson$Types.newParameterizedTypeWithOwner(null, SuperBean.class, type));
            if (superBean.error) {
                throw new UniteException(-1, "服务器开小差啦~");
            }
            return (T) superBean.results;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            value.close();
        }
        return null;
    }
}
