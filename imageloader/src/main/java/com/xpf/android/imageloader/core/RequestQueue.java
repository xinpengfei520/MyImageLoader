/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 bboyfeiyu@gmail.com, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.xpf.android.imageloader.core;

import android.util.Log;

import com.xpf.android.imageloader.request.BitmapRequest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:请求队列, 使用优先队列,使得请求可以按照优先级进行处理(Thread Safe)
 */
public final class RequestQueue {

    private static final String TAG = "RequestQueue";
    /**
     * 请求队列 [ Thread-safe ]
     */
    private BlockingQueue<BitmapRequest> mRequestQueue = new PriorityBlockingQueue<>();
    /**
     * 请求的序列化生成器
     */
    private AtomicInteger mSerialNumGenerator = new AtomicInteger(0);
    /**
     * 默认的核心数
     */
    public static int DEFAULT_CORE_NUM = Runtime.getRuntime().availableProcessors() + 1;
    /**
     * CPU核心数 + 1个分发线程数
     */
    private int mDispatcherNum;
    /**
     * NetworkExecutor,执行网络请求的线程
     */
    private RequestDispatcher[] mDispatchers = null;

    /**
     * empty param Constructor
     */
    protected RequestQueue() {
        this(DEFAULT_CORE_NUM);
    }

    /**
     * single param Constructor
     *
     * @param coreNum 线程核心数
     */
    protected RequestQueue(int coreNum) {
        mDispatcherNum = coreNum;
    }

    /**
     * 启动RequestDispatcher
     */
    private final void startDispatchers() {
        mDispatchers = new RequestDispatcher[mDispatcherNum];
        for (int i = 0; i < mDispatcherNum; i++) {
            Log.e(TAG, "### 启动线程 " + i);
            mDispatchers[i] = new RequestDispatcher(mRequestQueue);
            mDispatchers[i].start();
        }
    }

    public void start() {
        stop();
        startDispatchers();
    }

    /**
     * 停止RequestDispatcher
     */
    public void stop() {
        if (mDispatchers != null && mDispatchers.length > 0) {
            for (int i = 0; i < mDispatchers.length; i++) {
                mDispatchers[i].interrupt();
            }
        }
    }

    /**
     * 不能重复添加请求
     *
     * @param request
     */
    public void addRequest(BitmapRequest request) {
        if (!mRequestQueue.contains(request)) {
            request.serialNum = this.generateSerialNumber();
            mRequestQueue.add(request);
        } else {
            Log.d(TAG, "请求队列中已经含有");
        }
    }

    public void clear() {
        mRequestQueue.clear();
    }

    public BlockingQueue<BitmapRequest> getAllRequests() {
        return mRequestQueue;
    }

    /**
     * 为每个请求生成一个系列号
     *
     * @return 序列号
     */
    private int generateSerialNumber() {
        return mSerialNumGenerator.incrementAndGet();
    }
}
