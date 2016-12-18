package com.yuxiaobai.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yuxiaobai on 2016/12/16.
 * 网络缓存工具类
 */

public class NetCacheUtils {
    public final Handler handler;
    public final LocalCacheUtils localCachUtils;
    public final MemoryCacheUtils memoryCacheUtils;
    public static final int SUCCESS = 1;
    public static final int FAIL = 2;
    private final ExecutorService service;

    public NetCacheUtils(Handler handler, LocalCacheUtils localCachUtils, MemoryCacheUtils memoryCacheUtils) {
        this.handler = handler;
        this.localCachUtils = localCachUtils;
        this.memoryCacheUtils = memoryCacheUtils;
//         创建有10个线程的线程池
        service = Executors.newFixedThreadPool(10);

    }


    public void getBitmapFromNet(String imageUrl, int position) {
//        new Thread(new MyRunnable(imageUrl)).start();
        service.execute(new MyRunnable(imageUrl,position));
    }

    class MyRunnable implements Runnable {

        private final String imageUrl;
        private final int position;

        public MyRunnable(String imageUrl,int position) {
            this.imageUrl = imageUrl;
            this.position = position;
        }

        @Override
        public void run() {

            try {
                HttpURLConnection coon = (HttpURLConnection) new URL(imageUrl).openConnection();
                coon.setRequestMethod("GET");
                coon.setReadTimeout(5000);
                coon.setConnectTimeout(5000);
                coon.connect();
                int code = coon.getResponseCode();
                if (code == 200) {
                    InputStream is = coon.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
//                    发送到主线程显示
                    Message msg = Message.obtain();
                    msg.arg1 = position;
                    msg.what = SUCCESS;
                    msg.obj = bitmap;
//                     向内存保存一份
//                     本地sd卡保存图片
                          localCachUtils.putBitmap(imageUrl,bitmap);
//                     向捏村保存一份
                    memoryCacheUtils.putBitmap(imageUrl,bitmap);

                }
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.arg1 = position;
                msg.what= FAIL;
                handler.sendMessage(msg);
            }

        }
    }
}
