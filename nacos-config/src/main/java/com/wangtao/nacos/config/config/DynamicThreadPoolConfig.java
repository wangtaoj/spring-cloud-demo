package com.wangtao.nacos.config.config;

import com.wangtao.nacos.config.dynamic.thread.DynamicThreadPoolFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangtao
 * Created at 2026-06-27
 */
@Configuration(proxyBeanMethods = false)
public class DynamicThreadPoolConfig {

    @Bean
    public DynamicThreadPoolFactoryBean logDynamicThreadPool() {
        return new DynamicThreadPoolFactoryBean("log");
    }
}
