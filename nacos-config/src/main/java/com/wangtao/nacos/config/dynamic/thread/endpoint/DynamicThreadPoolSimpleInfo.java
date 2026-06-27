package com.wangtao.nacos.config.dynamic.thread.endpoint;

/**
 * @author wangtao
 * Created at 2026-04-05
 */

public class DynamicThreadPoolSimpleInfo {

    /**
     * 核心线程数量
     */
    private int corePoolSize;

    /**
     * 最大线程数量
     */
    private int maximumPoolSize;

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
