package com.wangtao.nacos.consumer.loadbalancer;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * {@code @LoadBalancerClients}注解中defaultConfiguration指定的配置类会被注册到每一个子容器中
 * @author wangtao
 * Created at 2026-10-21
 */
@Profile("local")
@LoadBalancerClients(defaultConfiguration = LocalFirstLoadBalancerConfiguration.class)
@Configuration(proxyBeanMethods = false)
public class LocalFirstLoadBalancerAutoConfiguration {
}
