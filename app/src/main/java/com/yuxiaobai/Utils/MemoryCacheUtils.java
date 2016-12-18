package com.yuxiaobai.Utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by yuxiaobai on 2016/12/16.
 */

public class MemoryCacheUtils {
    /**
     * 在内存中使用LruCache集合来存储数据，当有长时间不用，或者内存溢出时将自动清理
     *
     * @param imageUrl
     * @return
     */
    private LruCache<String, Bitmap> lrucache;

    public MemoryCacheUtils() {
        int maxsize = (int) (Runtime.getRuntime().maxMemory() / 8);//得到最大存储空间的1/8
        lrucache = new LruCache<String, Bitmap>(maxsize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };


    }

    /**
     * 根据url添加图片到内容中
     *
     * @param imageUrl
     * @return
     */
    public void putBitmap(String imageUrl, Bitmap bitmap) {
        lrucache.put(imageUrl, bitmap);

    }

    /**
     * 根据url返回内存中的图片
     *
     * @param imageUrl
     * @return
     */

    public Bitmap getBitmap(String imageUrl) {
        return lrucache.get(imageUrl);
    }
}
