package com.xpf.android.imageloader.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.xpf.android.imageloader.core.EasyImageLoader;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:自定义网络加载图片控件
 */
public class NetworkImageView extends AppCompatImageView {

    public NetworkImageView(Context context) {
        this(context, null);
    }

    public NetworkImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置图片的 url
     *
     * @param url
     */
    public void setImageUrl(String url) {
        EasyImageLoader.getInstance().displayImage(this, url);
    }

}
