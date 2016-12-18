package com.maning.retrofitokhttpcache.http;

import com.maning.retrofitokhttpcache.bean.GankModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by maning on 16/6/16.
 * 写一个接口，使用get模式进行请求。
 *
 */
public interface MNApiService {
    //http://gank.io/api/data/Android/10/1
    @Headers("Cache-Control: public, max-age=60")
    @GET("data/{type}/{count}/{pageIndex}")
    Call<GankModel> getGankCommonData(@Path("type") String type,
                                      @Path("count") int count,
                                      @Path("pageIndex") int pageIndex
    );

}
