package com.xpf.myimageloader;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.anloq.sdk.imageloader.DiskCache;
import com.anloq.sdk.imageloader.DoubleCache;
import com.anloq.sdk.imageloader.ImageCache;
import com.anloq.sdk.imageloader.ImageLoader;
import com.anloq.sdk.imageloader.MemoryCache;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        loadImage();
    }

    private void loadImage() {
        ImageLoader imageLoader = new ImageLoader();
        // 使用内存缓存
        imageLoader.setmImageCache(new MemoryCache());
        // 使用SD卡缓存
        imageLoader.setmImageCache(new DiskCache());
        // 使用双缓存
        imageLoader.setmImageCache(new DoubleCache());
        // 使用自定义的图片缓存
        imageLoader.setmImageCache(new ImageCache() {
            @Override
            public Bitmap get(String url) {
                return null;
            }

            @Override
            public void put(String url, Bitmap bitmap) {

            }
        });
        String imageUrl = "http://p1.meituan.net/160.0.80/xianfu/5e369ac9d6aa54125ad1b6562282b2ca36024.jpeg";
        imageLoader.displayImage(imageUrl, imageView);
    }
}
