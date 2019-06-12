package com.xpf.android.imageloader.loader;

import android.graphics.Bitmap;
import android.util.Log;

import com.xpf.android.imageloader.request.BitmapRequest;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:
 */
public class NullLoader extends AbsLoader {

    private static final String TAG = "NullLoader";

    @Override
    public Bitmap onLoadImage(BitmapRequest requestBean) {
        Log.e(TAG, "### wrong schema, your image uri is : " + requestBean.imageUri);
        return null;
    }

}
