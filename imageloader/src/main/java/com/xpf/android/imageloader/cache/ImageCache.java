package com.xpf.android.imageloader.cache;

import android.graphics.Bitmap;

import com.xpf.android.imageloader.request.BitmapRequest;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:定义图片缓存接口
 */
public interface ImageCache {

    /**
     * 获取图片
     *
     * @param request 图片的链接地址
     * @return bitmap object of image.
     */
    Bitmap get(BitmapRequest request);

    /**
     * 缓存图片
     *
     * @param request   图片的链接地址
     * @param bitmap bitmap object of image.
     */
    void put(BitmapRequest request, Bitmap bitmap);

    /**
     * 移除图片
     *
     * @param request
     */
    void remove(BitmapRequest request);
}
