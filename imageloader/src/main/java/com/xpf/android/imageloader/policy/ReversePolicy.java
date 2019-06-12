package com.xpf.android.imageloader.policy;

import com.xpf.android.imageloader.request.BitmapRequest;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:逆序加载策略,即从最后加入队列的请求进行加载
 */
public class ReversePolicy implements LoadPolicy {

    @Override
    public int compare(BitmapRequest request1, BitmapRequest request2) {
        // 注意 Bitmap 请求要先执行最晚加入队列的请求,ImageLoader 的策略
        return request2.serialNum - request1.serialNum;
    }
}
