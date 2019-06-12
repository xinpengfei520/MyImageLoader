package com.xpf.myimageloader;

import android.app.Application;

import com.xpf.android.imageloader.cache.DoubleCache;
import com.xpf.android.imageloader.config.LoaderConfig;
import com.xpf.android.imageloader.core.EasyImageLoader;
import com.xpf.android.imageloader.policy.ReversePolicy;

/**
 * Created by x-sir on 2019-06-12 :)
 * Function:
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
    }

    private void initImageLoader() {
        LoaderConfig config = new LoaderConfig()
                .setLoadPlaceholder(R.drawable.load_placeholder)
                .setFailedPlaceholder(R.drawable.failed_placeholder)
                .setCache(new DoubleCache(this))
                .setThreadCount(5)
                .setLoadPolicy(new ReversePolicy());

        // 初始化
        EasyImageLoader.getInstance().init(config);
    }
}
