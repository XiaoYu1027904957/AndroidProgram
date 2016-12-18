package com.maning.retrofitokhttpcache.http;

import com.maning.retrofitokhttpcache.app.MyApplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by maning on 16/6/16.
 */
public class BaseRetrofit {

    private static Retrofit retrofit;

    //接口请求的Url
    public static final String BASEURL = "http://gank.io/api/";

    public static MNApiService getMNApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASEURL) //设置Base的访问路径
                    .addConverterFactory(GsonConverterFactory.create()) //设置默认的解析库：Gson
                    .client(MyApplication.defaultOkHttpClient())
                    .build();
        }
        return retrofit.create(MNApiService.class);
    }

}
