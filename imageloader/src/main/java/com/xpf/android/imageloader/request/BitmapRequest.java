package com.xpf.android.imageloader.request;

import android.util.Log;
import android.widget.ImageView;

import com.xpf.android.imageloader.config.DisplayConfig;
import com.xpf.android.imageloader.core.EasyImageLoader;
import com.xpf.android.imageloader.policy.LoadPolicy;
import com.xpf.android.imageloader.utils.ImageViewHelper;
import com.xpf.android.imageloader.utils.Md5Helper;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:Bitmap 网络请求类，注意 GET 和 DELETE 不能传递参数，因为其请求的性质所致，用户可以将参数
 * 构建到 url 后传递进来到 Request 中
 */
public class BitmapRequest implements Comparable<BitmapRequest> {

    private static final String TAG = "BitmapRequest";
    public String imageUriMd5;
    private Reference<ImageView> mImageViewRef;
    public DisplayConfig displayConfig;
    public EasyImageLoader.ImageListener imageListener;
    public String imageUri;
    /**
     * 请求序列号
     */
    public int serialNum = 0;
    /**
     * 是否取消该请求
     */
    public boolean isCancel = false;
    /**
     * 仅使用内存缓存
     */
    public boolean justCacheInMem = false;
    /**
     * 加载策略
     */
    private LoadPolicy mLoadPolicy = EasyImageLoader.getInstance().getConfig().mLoadPolicy;

    /**
     * @param imageView
     * @param uri
     * @param config
     * @param listener
     */
    public BitmapRequest(ImageView imageView, String uri, DisplayConfig config, EasyImageLoader.ImageListener listener) {
        mImageViewRef = new WeakReference<>(imageView);
        displayConfig = config;
        imageListener = listener;
        imageUri = uri;
        imageView.setTag(uri);
        imageUriMd5 = Md5Helper.toMD5(imageUri);
        Log.i(TAG, "imageUriMd5===" + imageUriMd5);
    }

    /**
     * @param policy
     */
    public void setLoadPolicy(LoadPolicy policy) {
        if (policy != null) {
            mLoadPolicy = policy;
        }
    }

    /**
     * 判断 imageView 的 tag 与 uri 是否相等
     *
     * @return
     */
    public boolean isImageViewTagValid() {
        return mImageViewRef.get() != null && mImageViewRef.get().getTag().equals(imageUri);
    }

    public ImageView getImageView() {
        return mImageViewRef.get();
    }

    public int getImageViewWidth() {
        return ImageViewHelper.getImageViewWidth(mImageViewRef.get());
    }

    public int getImageViewHeight() {
        return ImageViewHelper.getImageViewHeight(mImageViewRef.get());
    }

    @Override
    public int compareTo(BitmapRequest another) {
        return mLoadPolicy.compare(this, another);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((imageUri == null) ? 0 : imageUri.hashCode());
        result = prime * result + ((mImageViewRef == null) ? 0 : mImageViewRef.get().hashCode());
        result = prime * result + serialNum;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BitmapRequest other = (BitmapRequest) obj;
        if (imageUri == null) {
            if (other.imageUri != null) {
                return false;
            }
        } else if (!imageUri.equals(other.imageUri)) {
            return false;
        }
        if (mImageViewRef == null) {
            if (other.mImageViewRef != null) {
                return false;
            }
        } else if (!mImageViewRef.get().equals(other.mImageViewRef.get())) {
            return false;
        }
        return serialNum == other.serialNum;
    }

}
