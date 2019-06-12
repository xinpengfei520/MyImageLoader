package com.xpf.android.imageloader.policy;

import com.xpf.android.imageloader.request.BitmapRequest;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:顺序加载策略
 */
public class SerialPolicy implements LoadPolicy {

    @Override
    public int compare(BitmapRequest request1, BitmapRequest request2) {
        // 如果优先级相等,那么按照添加到队列的序列号顺序来执行
        return request1.serialNum - request2.serialNum;
    }

}
