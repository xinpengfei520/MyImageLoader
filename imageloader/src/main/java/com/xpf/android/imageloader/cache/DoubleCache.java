package com.xpf.android.imageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;

import com.xpf.android.imageloader.request.BitmapRequest;

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
    private ImageCache mDiskCache;

    public DoubleCache(Context context) {
        mDiskCache = DiskCache.getDiskCache(context);
    }

    /**
     * 优先使用内存加载，如果无再使用SD卡缓存
     *
     * @param request
     * @return
     */
    @Override
    public Bitmap get(BitmapRequest request) {
        Bitmap bitmap = mMemoryCache.get(request);
        if (bitmap == null) {
            bitmap = mDiskCache.get(request);
            saveBitmapIntoMemory(request, bitmap);
        }

        return bitmap;
    }

    /**
     * 将图片缓存到内存和SD卡中
     *
     * @param request
     * @param bitmap
     */
    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        mMemoryCache.put(request, bitmap);
        mDiskCache.put(request, bitmap);
    }

    @Override
    public void remove(BitmapRequest request) {
        mDiskCache.remove(request);
        mMemoryCache.remove(request);
    }

    private void saveBitmapIntoMemory(BitmapRequest key, Bitmap bitmap) {
        // 如果 value 从 disk 中读取,那么存入内存缓存
        if (bitmap != null) {
            mMemoryCache.put(key, bitmap);
        }
    }
}
