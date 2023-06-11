package com.wangtao.nacos.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wangtao
 * Created at 2023/6/10 15:29
 */
@EnableDiscoveryClient
@SpringBootApplication
public class NacosRegisterApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosRegisterApplication.class, args);
    }
}
