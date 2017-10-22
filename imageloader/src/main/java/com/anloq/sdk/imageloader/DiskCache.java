package com.anloq.sdk.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:
 */

public class DiskCache implements ImageCache {
    static String cacheDir = Environment.getExternalStorageState() + "ImageCache/";

    /**
     * 从SD卡中读取
     *
     * @param url
     * @return
     */
    @Override
    public Bitmap get(String url) {
        if (!TextUtils.isEmpty(url)) {
            int i = url.lastIndexOf(".");
            url = url.substring(0, i);
            int j = url.lastIndexOf("/");
            url = url.substring(j + 1, url.length());
            Log.e("TAG", "urlName===" + url);
        }
        return BitmapFactory.decodeFile(cacheDir + url);
    }

    /**
     * 缓存到SD卡中
     *
     * @param url
     * @param bmp
     */
    @Override
    public void put(String url, Bitmap bmp) {
        Log.e("TAG", "cacheDir===" + cacheDir);
        FileOutputStream fos = null;
        try {
            File file = new File(cacheDir);
            if (!file.exists()) {
                if (file.isDirectory()) {
                    file.mkdirs();
                }
            }

            if (!TextUtils.isEmpty(url)) {
                int i = url.lastIndexOf(".");
                url = url.substring(0, i);
                int j = url.lastIndexOf("/");
                url = url.substring(j + 1, url.length());
                Log.e("TAG", "urlName===" + url);
            }

            fos = new FileOutputStream(cacheDir + url);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
