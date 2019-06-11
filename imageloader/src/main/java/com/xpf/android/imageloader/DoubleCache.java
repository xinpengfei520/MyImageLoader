package com.xpf.android.imageloader;

import android.graphics.Bitmap;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:双缓存类
 */
public class DoubleCache implements ImageCache {

    /**
     * 内存缓存
     */
    private ImageCache mMemoryCache = new MemoryCache();
    /**
     * 磁盘缓存
     */
    private ImageCache mDiskCache = new DiskCache();

    /**
     * 优先使用内存加载，如果无再使用SD卡缓存
     *
     * @param url
     * @return
     */
    @Override
    public Bitmap get(String url) {
        Bitmap bitmap = mMemoryCache.get(url);
        if (bitmap == null) {
            bitmap = mDiskCache.get(url);
        }

        return bitmap;
    }

    /**
     * 将图片缓存到内存和SD卡中
     *
     * @param url
     * @param bitmap
     */
    @Override
    public void put(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
        mDiskCache.put(url, bitmap);
    }
}
