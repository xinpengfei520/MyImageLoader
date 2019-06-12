package com.xpf.android.imageloader.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:封装先加载图片 bound，计算出 inSmallSize 之后再加载图片的逻辑操作
 */
public abstract class BitmapDecoder {

    private static final String TAG = "BitmapDecoder";
    /**
     * @param options
     * @return
     */
    public abstract Bitmap decodeBitmapWithOption(Options options);

    /**
     * @param width  图片的目标宽度
     * @param height 图片的目标高度
     * @return
     */
    public final Bitmap decodeBitmap(int width, int height) {
        // 如果请求原图,则直接加载原图
        if (width <= 0 || height <= 0) {
            return decodeBitmapWithOption(null);
        }
        // 1、获取只加载 Bitmap 宽高等数据的 Option, 即设置 options.inJustDecodeBounds = true;
        Options options = new Options();
        // 设置为 true, 表示解析 Bitmap 对象，该对象不占内存
        options.inJustDecodeBounds = true;
        // 2、通过 options 加载 bitmap，此时返回的 bitmap 为空,数据将存储在 options 中
        decodeBitmapWithOption(options);
        // 3、计算缩放比例, 并且将 options.inJustDecodeBounds 设置为 false;
        configBitmapOptions(options, width, height);
        // 4、通过 options 设置的缩放比例加载图片
        return decodeBitmapWithOption(options);
    }

    /**
     * 加载原图
     *
     * @return
     */
    public Bitmap decodeOriginBitmap() {
        return decodeBitmapWithOption(null);
    }

    /**
     * 配置 Bitmap 的图片质量参数
     *
     * @param options
     * @param width
     * @param height
     */
    protected void configBitmapOptions(Options options, int width, int height) {
        // 设置缩放比例
        options.inSampleSize = computeInSmallSize(options, width, height);

        Log.d(TAG, "$## inSampleSize = " + options.inSampleSize
                + ", width = " + width + ", height= " + height);
        // 图片质量
        options.inPreferredConfig = Config.RGB_565;
        // 设置为false,解析Bitmap对象加入到内存中
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
    }

    private int computeInSmallSize(Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value,
            // this will guarantee a final image
            // with both dimensions larger than or equal to the requested
            // height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down
            // further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }

        return inSampleSize;
    }

}
