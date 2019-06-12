package com.xpf.android.imageloader.policy;

import com.xpf.android.imageloader.request.BitmapRequest;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:定义加载策略接口
 */
public interface LoadPolicy {
    /**
     * 比较方法
     *
     * @param request1 请求1
     * @param request2 请求2
     * @return
     */
    int compare(BitmapRequest request1, BitmapRequest request2);
}
