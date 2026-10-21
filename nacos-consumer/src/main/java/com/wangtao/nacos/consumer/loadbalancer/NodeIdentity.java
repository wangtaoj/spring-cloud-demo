package com.wangtao.nacos.consumer.loadbalancer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.HostInfoEnvironmentPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 节点标识
 * @see HostInfoEnvironmentPostProcessor
 * @author wangtao
 * Created at 2026-10-21
 */
@Component
public class NodeIdentity {

    @Value("${spring.cloud.client.hostname:}")
    private String hostname;

    @Value("${spring.cloud.client.ip-address:}")
    private String ipAddress;

    public String getHostname() {
        return hostname;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
