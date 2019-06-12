package com.xpf.android.imageloader.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.xpf.android.imageloader.request.BitmapRequest;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:图片缓存类
 */
public class MemoryCache implements ImageCache {

    /**
     * 图片LRU缓存
     */
    private LruCache<String, Bitmap> mImageCache;

    public MemoryCache() {
        initImageCache();
    }

    /**
     * 初始化图片缓存大小
     */
    private void initImageCache() {
        // 计算可使用的最大内存
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 取 1/4 的可用内存作为缓存
        final int cacheSize = maxMemory / 4;
        mImageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        return mImageCache.get(request.imageUri);
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        mImageCache.put(request.imageUri, bitmap);
    }

    @Override
    public void remove(BitmapRequest request) {
        mImageCache.remove(request.imageUri);
    }

}
