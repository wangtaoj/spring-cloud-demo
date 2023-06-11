package com.wangtao.nacos.consumer.controller;

import com.wangtao.nacos.consumer.feign.IInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

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
    private IInfoService infoService;

    @GetMapping("/port")
    public Integer getPortByServiceName(String serviceName) {
        log.info("serviceName: {}", serviceName);
        String url = "http://{serviceName}/info/port";
        return restTemplate.getForObject(url, Integer.class, serviceName);
    }

    @GetMapping("/port1")
    public Integer getPortByServiceNameUseFeign(String serviceName) {
        log.info("serviceName: {}", serviceName);
        if (Objects.equals(serviceName, "nacos-service")) {
            return infoService.port();
        } else {
            throw new IllegalArgumentException(serviceName);
        }
    }

    @GetMapping("/retry")
    public Integer retry() {
        return infoService.retry();
    }
}
