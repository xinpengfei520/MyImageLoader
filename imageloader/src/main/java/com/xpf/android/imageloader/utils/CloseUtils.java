package com.xpf.android.imageloader.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by x-sir on 2019-06-11 :)
 * Function:
 */
public final class CloseUtils {

    /**
     * 私有化构造器，禁止创建对象和使用对象进行调用
     */
    private CloseUtils() {
    }

    /**
     * 关闭 Closeable 对象
     *
     * @param closeable
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
