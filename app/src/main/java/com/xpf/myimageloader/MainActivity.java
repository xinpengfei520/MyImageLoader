package com.xpf.myimageloader;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xpf.android.imageloader.core.EasyImageLoader;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private static final String IMAGE_URL = "http://image.x-sir.com/FoiEznYF5eSQS8WMN62NzUQ4gk40";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        requestPermission();
    }

    private void requestPermission() {
        final RxPermissions rxPermissions = new RxPermissions(this);

        // Must be done during an initialization phase like onCreate
        Disposable disposable =
                rxPermissions
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            // Always true pre-M
                            if (granted) {
                                // I can control the camera now
                                loadImage();
                            } else {
                                // Oups permission denied
                                Toast.makeText(MainActivity.this, "您拒绝了权限！", Toast.LENGTH_SHORT).show();
                            }
                        });

        compositeDisposable.add(disposable);
    }

    /**
     * 加载图片
     */
    private void loadImage() {
        EasyImageLoader.getInstance().displayImage(imageView, IMAGE_URL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        // 退出时关闭
        EasyImageLoader.getInstance().stop();
    }

}
