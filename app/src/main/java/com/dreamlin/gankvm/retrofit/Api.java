package com.dreamlin.gankvm.retrofit;

import com.dreamlin.gankvm.entity.ResultsEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    @GET("data/all/{count}/{page}")
    Observable<List<ResultsEntity>> getAll(@Path("page") int page, @Path("count") int count);

    @GET("data/福利/{count}/{page}")
    Observable<List<ResultsEntity>> getGirls(@Path("page") int page, @Path("count") int count);

    @GET("search/query/{key}/category/all/count/{count}/page/{page}")
    Observable<List<ResultsEntity>> search(@Path("key") String keywords,
                                           @Path("page") int page,
                                           @Path("count") int count);

}
