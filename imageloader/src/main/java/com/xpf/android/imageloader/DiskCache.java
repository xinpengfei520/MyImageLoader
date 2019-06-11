package com.xpf.android.imageloader;

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
 * Function:磁盘缓存
 */
public class DiskCache implements ImageCache {

    private static final String TAG = "DiskCache";
    private static String sCacheDir = Environment.getExternalStorageState() + "ImageCache/";

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
            url = url.substring(j + 1);
            Log.e("TAG", "urlName===" + url);
        }

        Log.i(TAG, "sCacheDir===" + sCacheDir);
        File file = new File(sCacheDir + url);
        if (!file.exists()) {
            return null;
        }

        return BitmapFactory.decodeFile(sCacheDir + url);
    }

    /**
     * 缓存到SD卡中
     *
     * @param url
     * @param bmp
     */
    @Override
    public void put(String url, Bitmap bmp) {
        Log.e(TAG, "sCacheDir===" + sCacheDir);
        FileOutputStream fos = null;
        try {
            File file = new File(sCacheDir);
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

            fos = new FileOutputStream(sCacheDir + url);
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
