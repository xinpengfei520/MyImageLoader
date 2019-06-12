package com.xpf.android.imageloader.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.xpf.android.imageloader.request.BitmapRequest;
import com.xpf.android.imageloader.utils.BitmapDecoder;
import com.xpf.android.imageloader.utils.CloseUtils;
import com.xpf.android.imageloader.utils.Md5Helper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:磁盘缓存，使用 Jake Wharton DiskLruCache
 */
public class DiskCache implements ImageCache {

    private static final String TAG = "DiskCache";
    /**
     * 1MB
     */
    private static final int MB = 1024 * 1024;

    /**
     * cache dir
     */
    private static final String IMAGE_DISK_CACHE = "bitmap";
    /**
     * Disk LRU Cache
     */
    private DiskLruCache mDiskLruCache;
    /**
     * Disk Cache Instance
     */
    private static DiskCache mDiskCache;

    /**
     * @param context
     */
    private DiskCache(Context context) {
        initDiskCache(context);
    }

    public static DiskCache getDiskCache(Context context) {
        if (mDiskCache == null) {
            synchronized (DiskCache.class) {
                if (mDiskCache == null) {
                    mDiskCache = new DiskCache(context);
                }
            }

        }
        return mDiskCache;
    }

    /**
     * 初始化sdcard缓存
     */
    private void initDiskCache(Context context) {
        try {
            File cacheDir = getDiskCacheDir(context, IMAGE_DISK_CACHE);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 50 * MB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @param uniqueName
     * @return
     */
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.d(TAG, "### context : " + context + ", dir = " + context.getExternalCacheDir());
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * @param context
     * @return
     */
    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 1;
    }

    @Override
    public synchronized Bitmap get(final BitmapRequest request) {
        // 图片解析器
        BitmapDecoder decoder = new BitmapDecoder() {

            @Override
            public Bitmap decodeBitmapWithOption(BitmapFactory.Options options) {
                final InputStream inputStream = getInputStream(request.imageUriMd5);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null,
                        options);
                CloseUtils.closeQuietly(inputStream);
                return bitmap;
            }
        };

        return decoder.decodeBitmap(request.getImageViewWidth(),
                request.getImageViewHeight());
    }

    private InputStream getInputStream(String md5) {
        DiskLruCache.Snapshot snapshot;
        try {
            snapshot = mDiskLruCache.get(md5);
            if (snapshot != null) {
                return snapshot.getInputStream(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * sd卡缓存只缓存从网络下下载下来的图片,本地图片则不缓存
     */
    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        if (request.justCacheInMem) {
            Log.e(IMAGE_DISK_CACHE, "### 仅缓存在内存中");
            return;
        }

        DiskLruCache.Editor editor = null;
        try {
            // 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
            editor = mDiskLruCache.edit(request.imageUriMd5);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                if (writeBitmapToDisk(bitmap, outputStream)) {
                    // 写入disk缓存
                    editor.commit();
                } else {
                    editor.abort();
                }
                CloseUtils.closeQuietly(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean writeBitmapToDisk(Bitmap bitmap, OutputStream outputStream) {
        BufferedOutputStream bos = new BufferedOutputStream(outputStream, 8 * 1024);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        boolean result = true;
        try {
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            CloseUtils.closeQuietly(bos);
        }

        return result;
    }

    @Override
    public void remove(BitmapRequest request) {
        try {
            mDiskLruCache.remove(Md5Helper.toMD5(request.imageUriMd5));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
