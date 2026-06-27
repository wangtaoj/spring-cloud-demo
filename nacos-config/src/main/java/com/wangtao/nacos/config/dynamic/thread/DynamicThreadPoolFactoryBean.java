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

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
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

    public void adjustThreadPool() {
        DynamicThreadPoolProperties properties = bindProperties();
        BlockingQueue<Runnable> workQueue = executor.getQueue();
        if (workQueue instanceof ResizeableLinkedBlockingQueue) {
            ((ResizeableLinkedBlockingQueue<Runnable>) workQueue).setCapacity(properties.getQueueCapacity());
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
        log.info("调整后的参数, corePoolSize: {}, maximumPoolSize: {}", executor.getCorePoolSize(), executor.getMaximumPoolSize());
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
        Assert.isTrue(properties.getQueueCapacity() >= 0, "queueCapacity can not be negative");
        Assert.isTrue(properties.getCorePoolSize() <= properties.getMaximumPoolSize(), "corePoolSize must be less than or equals maximumPoolSize");
        return properties;
    }

    @Override
    public Class<?> getObjectType() {
        return ThreadPoolExecutor.class;
    }

    @Override
    public void destroy() {
        if (executor != null) {
            executor.shutdown();
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
