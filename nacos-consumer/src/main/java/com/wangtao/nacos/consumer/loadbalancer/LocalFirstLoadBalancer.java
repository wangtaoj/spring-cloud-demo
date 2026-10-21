package com.wangtao.nacos.consumer.loadbalancer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本机服务优先负载均衡算法, 若本机没有该服务则降级为轮训策略
 * @author wangtao
 * Created at 2026-10-21
 */
@Slf4j
public class LocalFirstLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private final ObjectProvider<ServiceInstanceListSupplier> supplierProvider;

    /**
     * 服务id
     */
    private final String serviceId;

    /**
     * 本机ip
     */
    private final String localIp;

    private final AtomicInteger position;

    public LocalFirstLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> supplierProvider,
                                  String serviceId,
                                  NodeIdentity nodeIdentity) {
        this.serviceId = serviceId;
        this.supplierProvider = supplierProvider;
        this.localIp = nodeIdentity.getIpAddress();
        this.position = new AtomicInteger(new Random().nextInt(1000));
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = supplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map(this::getInstanceResponse);
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        ServiceInstance instance = chooseInstance(instances);
        if (Objects.isNull(instance)) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: {}", serviceId);
            }
            return new EmptyResponse();
        }
        return new DefaultResponse(instance);
    }

    private ServiceInstance chooseInstance(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            return null;
        }
        for (ServiceInstance instance : instances) {
            if (Objects.equals(localIp, instance.getHost())) {
                return instance;
            }
        }
        // 降级为轮训策略
        if (instances.size() == 1) {
            return instances.get(0);
        }
        int pos = this.position.incrementAndGet() & Integer.MAX_VALUE;
        return instances.get(pos % instances.size());
    }
}
