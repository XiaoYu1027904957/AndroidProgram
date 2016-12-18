package com.yuxiaobai.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by yuxiaobai on 2016/12/12.
 */

public class CacheUtils {
    private static SharedPreferences sp;

    /**
     * 保持 boolean 数据类型
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();

    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);

    }

    /**
     * 吧字符串存储到sp中
     * @param context
     * @param key
     * @param value
     */

    public static void putString(Context context, String key, String value) {

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                String fileName = MD5Encoder.encode(key);
                String dir = Environment.getExternalStorageDirectory()+"/Yyuxiaobai/files";
                File  file = new File(dir,fileName);
              File parentFile = file.getParentFile();

                if(!parentFile.exists()) {
//                     如果文件存在采取文本
                    parentFile.mkdirs();
                    if(file.exists()) {
                        file.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(value.getBytes());
                    fos.flush();
                    fos.close();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
            sp.edit().putString(key,value).commit();
        }
        

    }

    /**
     *  从sp中获取存储的字符串
     * @param context
     * @param key
     * @param defult
     * @return
     */
    public static String getString(Context context, String key, String defult) {
        String result = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String fileName = null;//ljlsjljslskkljkslkskskl
            try {
                //mnt/sdcard/beijingnews/files/ljlsjljslskkljkslkskskl
                fileName = MD5Encoder.encode(key);
                String dir = Environment.getExternalStorageDirectory() + "/beijingnews/files";
                File file = new File(dir, fileName);
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(file);
                    int len = -1;
                    byte[] buffer = new byte[1024];
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while ((len = fis.read(buffer)) != -1) {
                        baos.write(buffer, 0, len);
                    }
                    result = baos.toString();
                    fis.close();
                    baos.close();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

         } else {
            SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
            result = sp.getString(key, "");
        }
        return result;
    }
}
