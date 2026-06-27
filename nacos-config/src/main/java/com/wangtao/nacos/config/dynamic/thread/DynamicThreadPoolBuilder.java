package com.wangtao.nacos.config.dynamic.thread;

import com.wangtao.nacos.config.dynamic.thread.queue.ResizeableLinkedBlockingQueue;

import java.time.Duration;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 注: Builder中的配置项不可动态修改，也不会从配置中心读取，在注册时指定
 * @author wangtao
 * Created at 2026-06-27
 */
public class DynamicThreadPoolBuilder {

    private long keepAliveTime = 60;

    private TimeUnit unit = TimeUnit.SECONDS;

    /**
     * 线程工厂
     */
    private ThreadFactory threadFactory;

    /**
     * 拒绝策略
     */
    private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();

    /**
     * 等待线程池终止的最大时间
     */
    private Duration awaitTerminationTime = Duration.ofSeconds(10);

    /**
     * shutdownNow后的再一次等待时间
     */
    private Duration shutdownNowAwaitTime = Duration.ofSeconds(5);

    /**
     * 是否允许核心线程超时
     */
    private boolean allowCoreThreadTimeOut;

    public DynamicThreadPoolBuilder() {

    }

    public ThreadPoolExecutor build(int corePoolSize, int maximumPoolSize, int queueCapacity) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            corePoolSize, maximumPoolSize, keepAliveTime, unit,
            new ResizeableLinkedBlockingQueue<>(queueCapacity), threadFactory, rejectedExecutionHandler
        );
        if (allowCoreThreadTimeOut) {
            executor.allowCoreThreadTimeOut(true);
        }
        return executor;
    }

    public DynamicThreadPoolBuilder keepAliveTime(long keepAliveTime, TimeUnit unit) {
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        return this;
    }

    public DynamicThreadPoolBuilder threadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
        return this;
    }

    public DynamicThreadPoolBuilder threadNamePrefix(String threadNamePrefix) {
        this.threadFactory = new DefaultThreadFactory(threadNamePrefix);
        return this;
    }

    public DynamicThreadPoolBuilder rejectedExecutionHandler(RejectedExecutionHandler handler) {
        this.rejectedExecutionHandler = handler;
        return this;
    }

    public DynamicThreadPoolBuilder awaitTerminationTime(Duration awaitTerminationTime) {
        this.awaitTerminationTime = awaitTerminationTime;
        return this;
    }

    public DynamicThreadPoolBuilder shutdownNowAwaitTime(Duration shutdownNowAwaitTime) {
        this.shutdownNowAwaitTime = shutdownNowAwaitTime;
        return this;
    }

    public DynamicThreadPoolBuilder allowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
        return this;
    }

    public Duration getAwaitTerminationTime() {
        return awaitTerminationTime;
    }

    public Duration getShutdownNowAwaitTime() {
        return shutdownNowAwaitTime;
    }
}
