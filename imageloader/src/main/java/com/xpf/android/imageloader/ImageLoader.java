package com.xpf.android.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:
 */

public class ImageLoader {

    private static final String TAG = "ImageLoader";
    private ImageCache mImageCache = new MemoryCache();
    private Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 线程池，线程池数量为 CPU 的数量 + 1
     */
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() + 1);

    /**
     * 注入缓存实现
     *
     * @param mImageCache
     */
    public void setImageCache(ImageCache mImageCache) {
        this.mImageCache = mImageCache;
    }

    private void updateImgaeView(final ImageView imageView, final Bitmap bitmap) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    /**
     * 加载显示图片
     *
     * @param url
     * @param imageView
     */
    public void displayImage(final String url, final ImageView imageView) {
        Bitmap bitmap = mImageCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }

        // 图片没有缓存提交到线程池中下载
        submitLoadRequest(url, imageView);
    }

    private void submitLoadRequest(final String url, final ImageView imageView) {
        imageView.setTag(url);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(url);
                if (bitmap == null) {
                    return;
                }
                if (imageView.getTag().equals(url)) {
                    updateImgaeView(imageView, bitmap);
                }
                mImageCache.put(url, bitmap);
            }
        });
    }

    /**
     * 下载图片
     *
     * @param imageUrl
     * @return
     */
    private Bitmap downloadImage(String imageUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception:" + e.getMessage());
        }

        return bitmap;
    }

}
