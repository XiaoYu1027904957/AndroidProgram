package com.maning.retrofitokhttpcache.app;

import android.app.Application;
import android.util.Log;

import com.maning.retrofitokhttpcache.utils.NetUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by maning on 16/6/16.
 */
public class MyApplication extends Application {

    private static final String TAG = "maning----";
    private static MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

    }

    public static MyApplication getInstance() {
        return application;
    }

    public static OkHttpClient defaultOkHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.writeTimeout(30 * 1000, TimeUnit.MILLISECONDS);
        client.readTimeout(20 * 1000, TimeUnit.MILLISECONDS);
        client.connectTimeout(15 * 1000, TimeUnit.MILLISECONDS);
        //设置缓存路径
        File httpCacheDirectory = new File(application.getCacheDir(), "okhttpCache");
        //设置缓存 10M大小
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        // 给链接设置缓存区域
        client.cache(cache);
        //设置拦截器
        client.addInterceptor(LoggingInterceptor);
//         设置网络拦截器
        client.addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        client.addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        return client.build();
    }

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {

        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            //方案一：有网和没有网都是先读缓存
//                Request request = chain.request();
//                Log.i(TAG, "request=" + request);
//                Response response = chain.proceed(request);
//                Log.i(TAG, "response=" + response);
//
//                String cacheControl = request.cacheControl().toString();
//                if (TextUtils.isEmpty(cacheControl)) {
//                    cacheControl = "public, max-age=60";
//                }
//                return response.newBuilder()
//                        .header("Cache-Control", cacheControl)
//                        .removeHeader("Pragma")
//                        .build();

            //方案二：无网读缓存，有网根据过期时间重新请求
            boolean netWorkConection = NetUtils.hasNetWorkConection(MyApplication.getInstance());
            Request request = chain.request();
            if (!netWorkConection) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response response = chain.proceed(request);
            if (netWorkConection) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                Log.i(TAG, "cacheControl:" + cacheControl);
                response.newBuilder()
                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .header("Cache-Control", cacheControl)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 7;
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    };

    private static final Interceptor LoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            Log.i(TAG, String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            Log.i(TAG, String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    };
}
