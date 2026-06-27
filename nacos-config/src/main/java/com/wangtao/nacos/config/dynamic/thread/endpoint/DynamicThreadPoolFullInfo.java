package com.wangtao.nacos.config.dynamic.thread.endpoint;

/**
 * @author wangtao
 * Created at 2026-04-05
 */

public class DynamicThreadPoolFullInfo {

    /**
     * 核心线程数量
     */
    private int corePoolSize;

    /**
     * 最大线程数量
     */
    private int maximumPoolSize;

    /**
     * 当前线程数量
     */
    private int poolSize;

    /**
     * 当前正在执行任务的线程数量
     */
    private int activeCount;

    /**
     * 历史最大线程数量
     */
    private int largestPoolSize;

    /**
     * 已完成的任务总数
     */
    private long completedTaskCount;

    /**
     * 队列容量
     */
    private int queueCapacity;

    /**
     * 队列当前大小
     */
    private int queueSize;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public int getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(int activeCount) {
        this.activeCount = activeCount;
    }

    public int getLargestPoolSize() {
        return largestPoolSize;
    }

    public void setLargestPoolSize(int largestPoolSize) {
        this.largestPoolSize = largestPoolSize;
    }

    public long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(long completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
}
