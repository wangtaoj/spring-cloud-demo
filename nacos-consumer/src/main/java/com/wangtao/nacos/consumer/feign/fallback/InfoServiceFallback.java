package com.wangtao.nacos.consumer.feign.fallback;

import com.wangtao.nacos.consumer.feign.InfoServiceClient;
import org.springframework.stereotype.Component;

/**
 * 1. fallback机制需要结合熔断器组件(引入依赖)
 * 2. 然后开启feign.circuitbreaker.enable=true
 * @author wangtao
 * Created at 2023/6/17 10:42
 */
@Component
public class InfoServiceFallback implements InfoServiceClient {

    @Override
    public Integer port() {
        return null;
    }

    @Override
    public Integer retry() {
        return null;
    }

    @Override
    public String mockExp(boolean hasExp) {
        return "when exp then fallback";
    }
}
