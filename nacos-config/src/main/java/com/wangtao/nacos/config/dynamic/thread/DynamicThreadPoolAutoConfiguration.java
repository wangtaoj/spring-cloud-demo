package com.wangtao.nacos.config.dynamic.thread;

import com.wangtao.nacos.config.dynamic.thread.endpoint.DynamicThreadPoolFullInfoEndpoint;
import com.wangtao.nacos.config.dynamic.thread.endpoint.DynamicThreadPoolSimpleInfoEndpoint;
import com.wangtao.nacos.config.dynamic.thread.listener.DynamicThreadPoolListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author wangtao
 * Created at 2026-04-05
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class DynamicThreadPoolAutoConfiguration {

    @Bean
    public DynamicThreadPoolListener dynamicThreadPoolListener(List<DynamicThreadPoolFactoryBean> dynamicThreadPoolFactoryBeans) {
        return new DynamicThreadPoolListener(dynamicThreadPoolFactoryBeans);
    }

    @Bean
    public DynamicThreadPoolFullInfoEndpoint dynamicThreadPoolFullInfoEndpoint(List<DynamicThreadPoolFactoryBean> dynamicThreadPoolFactoryBeans) {
        return new DynamicThreadPoolFullInfoEndpoint(dynamicThreadPoolFactoryBeans);
    }

    @Bean
    public DynamicThreadPoolSimpleInfoEndpoint dynamicThreadPoolSimpleInfoEndpoint(List<DynamicThreadPoolFactoryBean> dynamicThreadPoolFactoryBeans) {
        return new DynamicThreadPoolSimpleInfoEndpoint(dynamicThreadPoolFactoryBeans);
    }
}
