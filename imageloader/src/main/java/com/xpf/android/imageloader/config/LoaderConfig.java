package com.xpf.android.imageloader.config;

import com.xpf.android.imageloader.cache.ImageCache;
import com.xpf.android.imageloader.cache.MemoryCache;
import com.xpf.android.imageloader.policy.LoadPolicy;
import com.xpf.android.imageloader.policy.SerialPolicy;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:ImageLoader 配置类
 */
public class LoaderConfig {

    /**
     * 缓存策略，默认为内存缓存
     */
    public ImageCache mImageCache = new MemoryCache();
    /**
     * 加载图片时的 loading 和加载失败的图片配置对象
     */
    public DisplayConfig mDisplayConfig = new DisplayConfig();
    /**
     * 加载策略
     */
    public LoadPolicy mLoadPolicy = new SerialPolicy();
    /**
     * 线程数，默认为 CPU 核心数 + 1
     */
    public int mThreadCount = Runtime.getRuntime().availableProcessors() + 1;

    /**
     * 设置线程数，最小为1，使用 max 防止当用户设置了小于 1 的线程数
     *
     * @param count
     * @return
     */
    public LoaderConfig setThreadCount(int count) {
        mThreadCount = Math.max(1, count);
        return this;
    }

    /**
     * 设置缓存策略
     *
     * @param cache
     * @return
     */
    public LoaderConfig setCache(ImageCache cache) {
        mImageCache = cache;
        return this;
    }

    /**
     * 设置占位图
     *
     * @param resId
     * @return
     */
    public LoaderConfig setLoadPlaceholder(int resId) {
        mDisplayConfig.loadingResId = resId;
        return this;
    }

    /**
     * 设置加载失败的图片
     *
     * @param resId
     * @return
     */
    public LoaderConfig setFailedPlaceholder(int resId) {
        mDisplayConfig.failedResId = resId;
        return this;
    }

    /**
     * 设置加载策略
     *
     * @param policy
     * @return
     */
    public LoaderConfig setLoadPolicy(LoadPolicy policy) {
        if (policy != null) {
            mLoadPolicy = policy;
        }
        return this;
    }
}
