package com.wangtao.nacos.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author wangtao
 * Created at 2023/6/11 09:13
 */
@FeignClient(value = "nacos-service", contextId = "nacos-service", path = "/info")
public interface IInfoService {

    @GetMapping("/port")
    Integer port();

    @GetMapping("/retry")
    Integer retry();
}
