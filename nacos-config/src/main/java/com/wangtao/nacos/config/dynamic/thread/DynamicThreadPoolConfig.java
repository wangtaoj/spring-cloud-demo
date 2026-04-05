package com.wangtao.nacos.config.dynamic.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2026-04-05
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class DynamicThreadPoolConfig implements ApplicationListener<ApplicationEvent> {

    private ThreadPoolExecutor executor;

    private final DynamicThreadPoolProperties dynamicThreadPoolProperties;

    public DynamicThreadPoolConfig(DynamicThreadPoolProperties dynamicThreadPoolProperties) {
        this.dynamicThreadPoolProperties = dynamicThreadPoolProperties;
    }

    @Bean
    public ThreadPoolExecutor dynamicThreadPool() {
        this.executor = new ThreadPoolExecutor(
            dynamicThreadPoolProperties.getCorePoolSize(),
            dynamicThreadPoolProperties.getMaximumPoolSize(),
            60L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1000)
        );
        executor.prestartAllCoreThreads();
        return executor;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationEvent event) {
        if (event instanceof EnvironmentChangeEvent) {
            EnvironmentChangeEvent environmentChangeEvent = (EnvironmentChangeEvent) event;
            log.info("changed keys: {}", environmentChangeEvent.getKeys());
        } else if (event instanceof RefreshScopeRefreshedEvent) {
            // 参数不合理
            if (dynamicThreadPoolProperties.getMaximumPoolSize() < dynamicThreadPoolProperties.getCorePoolSize()) {
                log.warn("参数调整不合理, 忽略, corePoolSize: {}, maximumPoolSize: {}", dynamicThreadPoolProperties.getCorePoolSize(), dynamicThreadPoolProperties.getMaximumPoolSize());
                return;
            }
            // 缩小, 先设置corePoolSize, 避免新的maximumPoolSize比当前的corePoolSize还要小, 从而报错
            if (dynamicThreadPoolProperties.getMaximumPoolSize() < executor.getMaximumPoolSize()) {
                if (executor.getCorePoolSize() != dynamicThreadPoolProperties.getCorePoolSize()) {
                    executor.setCorePoolSize(dynamicThreadPoolProperties.getCorePoolSize());
                }
                executor.setMaximumPoolSize(dynamicThreadPoolProperties.getMaximumPoolSize());
            } else {
                if (executor.getMaximumPoolSize() != dynamicThreadPoolProperties.getMaximumPoolSize()) {
                    executor.setMaximumPoolSize(dynamicThreadPoolProperties.getMaximumPoolSize());
                }
                if (executor.getCorePoolSize() != dynamicThreadPoolProperties.getCorePoolSize()) {
                    executor.setCorePoolSize(dynamicThreadPoolProperties.getCorePoolSize());
                }
            }
            log.info("调整后的参数, corePoolSize: {}, maximumPoolSize: {}", executor.getCorePoolSize(), executor.getMaximumPoolSize());
        }
    }
}
