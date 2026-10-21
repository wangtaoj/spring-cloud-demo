package com.wangtao.nacos.consumer.loadbalancer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @author wangtao
 * Created at 2026-10-21
 */
public class LocalFirstLoadBalancerConfiguration {

    /**
     * @see LoadBalancerClientConfiguration#reactorServiceInstanceLoadBalancer
     */
    @ConditionalOnMissingBean
    @Bean
    public ReactorServiceInstanceLoadBalancer localFirstLoadBalancer(Environment environment,
                                                                     LoadBalancerClientFactory loadBalancerClientFactory,
                                                                     NodeIdentity nodeIdentity) {
        // 服务名, NamedContextFactory.createContext方法创建子容器时会把服务名放到environment中
        String serviceId = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        ObjectProvider<ServiceInstanceListSupplier> lazyObjectProvider = loadBalancerClientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class);
        return new LocalFirstLoadBalancer(lazyObjectProvider, serviceId, nodeIdentity);
    }
}
