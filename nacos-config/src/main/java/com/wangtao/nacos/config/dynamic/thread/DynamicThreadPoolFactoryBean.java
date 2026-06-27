package com.wangtao.nacos.config.dynamic.thread;

import com.wangtao.nacos.config.dynamic.thread.queue.ResizeableLinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 只允许动态修改corePoolSize、maximumPoolSize、queueCapacity
 * @author wangtao
 * Created at 2026-06-27
 */
@Slf4j
public class DynamicThreadPoolFactoryBean implements FactoryBean<ThreadPoolExecutor>, DisposableBean, EnvironmentAware {

    private static final Map<String, String> NAME_MAP = new ConcurrentHashMap<>();

    public static final String CONFIG_PREFIX = "dynamic.threadpool";

    private final String dynamicThreadPoolName;

    private volatile ThreadPoolExecutor executor;

    private final DynamicThreadPoolBuilder builder;

    private Environment environment;

    public DynamicThreadPoolFactoryBean(String dynamicThreadPoolName) {
        this(dynamicThreadPoolName, new DynamicThreadPoolBuilder().threadNamePrefix(dynamicThreadPoolName));
    }

    public DynamicThreadPoolFactoryBean(String dynamicThreadPoolName, DynamicThreadPoolBuilder builder) {
        Assert.hasText(dynamicThreadPoolName, "dynamicThreadPoolName must not be empty");
        Assert.notNull(builder, "builder must not be null");
        String oldDynamicThreadPoolName = NAME_MAP.putIfAbsent(dynamicThreadPoolName, dynamicThreadPoolName);
        if (oldDynamicThreadPoolName != null) {
            throw new IllegalArgumentException(String.format("Duplicate thread pool name [%s]", oldDynamicThreadPoolName));
        }
        this.dynamicThreadPoolName = dynamicThreadPoolName;
        this.builder = builder;
    }

    @NonNull
    @Override
    public ThreadPoolExecutor getObject() {
        if (executor == null) {
            synchronized (this) {
                if (executor == null) {
                    executor = createExecutor();
                }
            }
        }
        return executor;
    }

    public synchronized void adjustThreadPool() {
        DynamicThreadPoolProperties properties = bindProperties();
        ThreadPoolExecutor executor = getObject();
        BlockingQueue<Runnable> workQueue = executor.getQueue();
        ResizeableLinkedBlockingQueue<Runnable> resizeableLinkedBlockingQueue = null;
        if (workQueue instanceof ResizeableLinkedBlockingQueue) {
            resizeableLinkedBlockingQueue = (ResizeableLinkedBlockingQueue<Runnable>) workQueue;
            resizeableLinkedBlockingQueue.setCapacity(properties.getQueueCapacity());
        }
        // 缩小, 先设置corePoolSize, 避免新的maximumPoolSize比当前的corePoolSize还要小, 从而报错
        if (properties.getMaximumPoolSize() < executor.getMaximumPoolSize()) {
            if (executor.getCorePoolSize() != properties.getCorePoolSize()) {
                executor.setCorePoolSize(properties.getCorePoolSize());
            }
            executor.setMaximumPoolSize(properties.getMaximumPoolSize());
        } else {
            if (executor.getMaximumPoolSize() != properties.getMaximumPoolSize()) {
                executor.setMaximumPoolSize(properties.getMaximumPoolSize());
            }
            if (executor.getCorePoolSize() != properties.getCorePoolSize()) {
                executor.setCorePoolSize(properties.getCorePoolSize());
            }
        }
        if (resizeableLinkedBlockingQueue != null) {
            log.info("调整后的参数, corePoolSize: {}, maximumPoolSize: {}, queueCapacity: {}",
                executor.getCorePoolSize(), executor.getMaximumPoolSize(),
                resizeableLinkedBlockingQueue.getCapacity());
        } else {
            log.info("调整后的参数, corePoolSize: {}, maximumPoolSize: {}", executor.getCorePoolSize(), executor.getMaximumPoolSize());
        }
    }

    private ThreadPoolExecutor createExecutor() {
        DynamicThreadPoolProperties properties = bindProperties();
        return builder.build(properties.getCorePoolSize(), properties.getMaximumPoolSize(), properties.getQueueCapacity());
    }

    private DynamicThreadPoolProperties bindProperties() {
        DynamicThreadPoolProperties properties = Binder.get(environment).bindOrCreate(getConfigPrefix(), DynamicThreadPoolProperties.class);
        Assert.isTrue(properties != null, "DynamicThreadPoolProperties can not be null");
        Assert.isTrue(properties.getCorePoolSize() >= 0, "corePoolSize can not be negative");
        Assert.isTrue(properties.getMaximumPoolSize() > 0, "maximumPoolSize must be greater than 0");
        Assert.isTrue(properties.getQueueCapacity() > 0, "queueCapacity must be greater than 0");
        Assert.isTrue(properties.getCorePoolSize() <= properties.getMaximumPoolSize(), "corePoolSize must be less than or equals maximumPoolSize");
        return properties;
    }

    @Override
    public Class<?> getObjectType() {
        return ThreadPoolExecutor.class;
    }

    @Override
    public void destroy() throws Exception {
        NAME_MAP.remove(dynamicThreadPoolName);
        if (executor != null) {
            executor.shutdown();
            boolean termination = executor.awaitTermination(builder.getAwaitTerminationTime().toNanos(), TimeUnit.NANOSECONDS);
            if (!termination) {
                List<Runnable> runnables = executor.shutdownNow();
                termination = executor.awaitTermination(builder.getShutdownNowAwaitTime().toNanos(), TimeUnit.NANOSECONDS);
                if (!termination || !runnables.isEmpty()) {
                    log.warn("[{}]线程池超时未终止，丢弃的任务数: {}", dynamicThreadPoolName, runnables.size());
                }
            }
        }
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    public String getDynamicThreadPoolName() {
        return dynamicThreadPoolName;
    }

    public String getConfigPrefix() {
        return CONFIG_PREFIX + "." + dynamicThreadPoolName;
    }
}
