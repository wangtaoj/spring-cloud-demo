package com.wangtao.nacos.config.dynamic.thread;

import com.wangtao.nacos.config.dynamic.thread.queue.ResizeableLinkedBlockingQueue;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2026-06-27
 */
public class DynamicThreadPoolBuilder {

    private long keepAliveTime = 60;

    private TimeUnit unit = TimeUnit.SECONDS;

    private ThreadFactory threadFactory;

    private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();

    public DynamicThreadPoolBuilder() {

    }

    public ThreadPoolExecutor build(int corePoolSize, int maximumPoolSize, int queueCapacity) {
        return new ThreadPoolExecutor(
            corePoolSize, maximumPoolSize, keepAliveTime, unit,
            new ResizeableLinkedBlockingQueue<>(queueCapacity), threadFactory, rejectedExecutionHandler
        );
    }

    public DynamicThreadPoolBuilder KeepAliveTime(long keepAliveTime, TimeUnit unit) {
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        return this;
    }

    public DynamicThreadPoolBuilder ThreadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
        return this;
    }

    public DynamicThreadPoolBuilder threadNamePrefix(String threadNamePrefix) {
        this.threadFactory = new DefaultThreadFactory(threadNamePrefix);
        return this;
    }

    public DynamicThreadPoolBuilder RejectedExecutionHandler(RejectedExecutionHandler handler) {
        this.rejectedExecutionHandler = handler;
        return this;
    }
}
