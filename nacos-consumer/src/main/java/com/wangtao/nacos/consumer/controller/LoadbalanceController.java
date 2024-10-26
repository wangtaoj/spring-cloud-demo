package com.wangtao.nacos.consumer.controller;

import com.wangtao.nacos.consumer.feign.InfoServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author wangtao
 * Created at 2023/6/11 08:47
 */
@Slf4j
@RestController
public class LoadbalanceController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private InfoServiceClient infoService;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/choose")
    public void choose() {
        ServiceInstance instance = loadBalancerClient.choose("nacos-service");
        log.info("host:ip = {}:{}", instance.getHost(), instance.getPort());
    }

    @GetMapping("/port")
    public Integer getPortByServiceName(String serviceName) {
        log.info("serviceName: {}", serviceName);
        String url = "http://{serviceName}/info/port";
        return restTemplate.getForObject(url, Integer.class, serviceName);
    }

    @GetMapping("/port1")
    public Integer getPortByServiceNameUseFeign() {
        return infoService.port();
    }

    @GetMapping("/retry")
    public Integer retry() {
        return infoService.retry();
    }

    @GetMapping("/tryFallback")
    public String tryFallback(boolean hasExp) {
        return infoService.mockExp(hasExp);
    }
}
