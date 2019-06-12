package com.xpf.android.imageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xpf.android.imageloader.request.BitmapRequest;
import com.xpf.android.imageloader.utils.CloseUtils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:
 */
public class UrlLoader extends AbsLoader {

    @Override
    public Bitmap onLoadImage(BitmapRequest request) {
        final String imageUrl = request.imageUri;
        InputStream is = null;
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(imageUrl);
            conn = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(conn.getInputStream());
            bitmap = BitmapFactory.decodeStream(is, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeQuietly(is);
            if (conn != null) {
                conn.disconnect();
            }
        }

        return bitmap;
    }
}
