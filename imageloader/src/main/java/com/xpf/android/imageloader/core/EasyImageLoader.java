package com.xpf.android.imageloader.core;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.xpf.android.imageloader.cache.ImageCache;
import com.xpf.android.imageloader.cache.MemoryCache;
import com.xpf.android.imageloader.config.DisplayConfig;
import com.xpf.android.imageloader.config.LoaderConfig;
import com.xpf.android.imageloader.policy.SerialPolicy;
import com.xpf.android.imageloader.request.BitmapRequest;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:图片加载类，支持 url 和本地图片的 uri 形式加载，根据图片路径格式来判断是网络图片还是本地图片，如果是网络图片
 * 则交给 SimpleNet 框架来加载，如果是本地图片那么则交给 mExecutorService 从 sd 卡中加载，加载之后直接更新 UI，无需
 * 用户干预，如果用户设置了缓存策略,那么会将加载到的图片缓存起来.用户也可以设置加载策略，例如顺序加载和逆向加载
 */
public final class EasyImageLoader {
    /**
     * EasyImageLoader 实例
     */
    private static EasyImageLoader sInstance;
    /**
     * 网络请求队列
     */
    private RequestQueue mRequestQueue;
    /**
     * 缓存策略，默认为内存缓存
     */
    private volatile ImageCache mCache = new MemoryCache();
    /**
     * 图片加载配置对象
     */
    private LoaderConfig mConfig;

    /**
     * 私有化构造器
     */
    private EasyImageLoader() {
    }

    /**
     * 获取 ImageLoader 单例
     *
     * @return EasyImageLoader
     */
    public static EasyImageLoader getInstance() {
        if (sInstance == null) {
            synchronized (EasyImageLoader.class) {
                if (sInstance == null) {
                    sInstance = new EasyImageLoader();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化
     *
     * @param config
     */
    public void init(LoaderConfig config) {
        mConfig = config;
        mCache = mConfig.mImageCache;
        checkConfig();
        mRequestQueue = new RequestQueue(mConfig.mThreadCount);
        mRequestQueue.start();
    }

    private void checkConfig() {
        if (mConfig == null) {
            throw new RuntimeException("The config of EasyImageLoader is Null, please call the " +
                    "init(LoaderConfig config) method to initialize");
        }

        if (mConfig.mLoadPolicy == null) {
            mConfig.mLoadPolicy = new SerialPolicy();
        }

        if (mCache == null) {
            mCache = new MemoryCache();
        }
    }

    public void displayImage(ImageView imageView, String uri) {
        displayImage(imageView, uri, null, null);
    }

    public void displayImage(ImageView imageView, String uri, DisplayConfig config) {
        displayImage(imageView, uri, config, null);
    }

    public void displayImage(ImageView imageView, String uri, ImageListener listener) {
        displayImage(imageView, uri, null, listener);
    }

    public void displayImage(final ImageView imageView, final String uri, final DisplayConfig config, final ImageListener listener) {
        BitmapRequest request = new BitmapRequest(imageView, uri, config, listener);
        // 加载的配置对象,如果没有设置则使用 ImageLoader 的配置
        request.displayConfig = request.displayConfig != null ? request.displayConfig : mConfig.mDisplayConfig;
        // 添加对队列中
        mRequestQueue.addRequest(request);
    }

    public LoaderConfig getConfig() {
        return mConfig;
    }

    public void stop() {
        mRequestQueue.stop();
    }

    /**
     * 图片加载的监听器
     */
    public interface ImageListener {
        /**
         * 当完成时的回调
         *
         * @param imageView
         * @param bitmap
         * @param uri
         */
        void onComplete(ImageView imageView, Bitmap bitmap, String uri);
    }
}
