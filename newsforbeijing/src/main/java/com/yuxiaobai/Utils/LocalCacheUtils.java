package com.yuxiaobai.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by yuxiaobai on 2016/12/16.
 * 图片本地缓存工具类
 */

public class LocalCacheUtils {
    private final MemoryCacheUtils memoryCacheUtils;

    public LocalCacheUtils(MemoryCacheUtils memoryCacheUtils) {
        this.memoryCacheUtils = memoryCacheUtils;
    }

    /**
     * 得到本地图片
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            String fileName = MD5Encoder.encode(imageUrl);
            String dir = Environment.getExternalStorageDirectory() + "/yuxiaobai";

            File file = new File(dir,fileName);
            if(file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                if(bitmap!=null) {
                    memoryCacheUtils.putBitmap(imageUrl,bitmap);
                }
                fis.close();
                return  bitmap;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 保存图片到本地
     * @param imageUrl
     * @param bitmap
     */
    public void putBitmap(String imageUrl, Bitmap bitmap){
//        保存sdcard--mnt/sdcard/yuxiaobai/文件名md5加密
        try {
            String fileName = MD5Encoder.encode(imageUrl);
            String dir = Environment.getExternalStorageDirectory() + "/yuxiaobai";//获取目录
            File file= new File(dir,fileName);
            File parentFile = file.getParentFile();//看看上级目录是否存在
            if(!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if(!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
        } catch (Exception e) {


        }
    }
}
