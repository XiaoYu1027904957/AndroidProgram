package com.yuxiaobai.Utils;

import android.graphics.Bitmap;
import android.os.Handler;

/**
 * Created by yuxiaobai on 2016/12/16.
 * 图片缓存工具类
 */

public class BitmapCacheUtils {
    private NetCacheUtils netCacheUtils;
    private LocalCacheUtils localCacheUtils;
    /**
     * 内存缓存的工具类
     */
    private MemoryCacheUtils memoryCacheUtils;

    public BitmapCacheUtils(Handler handler) {
        memoryCacheUtils = new MemoryCacheUtils();
        localCacheUtils = new LocalCacheUtils(memoryCacheUtils);
        netCacheUtils = new NetCacheUtils(handler,localCacheUtils,memoryCacheUtils);


    }

    /**
     * 三级缓存设计步骤
     * 1.从内存中获取图片
     * 2.从本地获取图片
     * 向内存中报保存一份
     * 3.请求网络图片，获取图片，显示到控件上
     *
     * @param imageUrl
     * @return
     */

    public Bitmap getBitmapFromNet(String imageUrl, int position) {
//         速度最快
        if (memoryCacheUtils != null) {
            Bitmap bitmap = memoryCacheUtils.getBitmap(imageUrl);
            if (bitmap != null) {
                return bitmap;
            }
        }
//             本地缓存，其次。
        if (localCacheUtils != null) {
            Bitmap bitmap = localCacheUtils.getBitmapFromUrl(imageUrl);
            if (bitmap != null) {
                return bitmap;
            }
        }
//        网络缓存,速度最慢
        netCacheUtils.getBitmapFromNet(imageUrl, position);
        return null;
    }
}
