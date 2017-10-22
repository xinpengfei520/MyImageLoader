package com.anloq.sdk.imageloader;

import android.graphics.Bitmap;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:
 */

public interface ImageCache {
    Bitmap get(String url);

    void put(String url, Bitmap bitmap);
}
