package com.xpf.android.imageloader.cache;

import android.graphics.Bitmap;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:定义图片缓存接口
 */

public interface ImageCache {
    /**
     * 获取图片
     *
     * @param url 图片的链接地址
     * @return bitmap object of image.
     */
    Bitmap get(String url);

    /**
     * 缓存图片
     *
     * @param url    图片的链接地址
     * @param bitmap bitmap object of image.
     */
    void put(String url, Bitmap bitmap);
}
