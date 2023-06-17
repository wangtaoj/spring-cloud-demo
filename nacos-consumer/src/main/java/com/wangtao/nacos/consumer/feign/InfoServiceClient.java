package com.wangtao.nacos.consumer.feign;

import com.wangtao.nacos.consumer.feign.fallback.InfoServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wangtao
 * Created at 2023/6/11 09:13
 */
@FeignClient(value = "nacos-service", contextId = "nacos-service", path = "/info", fallback = InfoServiceFallback.class)
public interface InfoServiceClient {

    @GetMapping("/port")
    Integer port();

    @GetMapping("/retry")
    Integer retry();

    @GetMapping("/mockExp")
    String mockExp(@RequestParam("hasExp") boolean hasExp);
}
