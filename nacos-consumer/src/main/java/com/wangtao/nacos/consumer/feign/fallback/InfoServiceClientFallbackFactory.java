package com.wangtao.nacos.consumer.feign.fallback;

import com.wangtao.nacos.consumer.feign.InfoServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author wangtao
 * Created at 2026-10-26
 */
@Slf4j
@Component
public class InfoServiceClientFallbackFactory implements FallbackFactory<InfoServiceClient> {

    private final InfoServiceClient infoServiceClient;

    public InfoServiceClientFallbackFactory(@Qualifier("infoServiceClientFallback") InfoServiceClient infoServiceClient) {
        this.infoServiceClient = infoServiceClient;
    }

    @Override
    public InfoServiceClient create(Throwable cause) {
        log.error(cause.getMessage(), cause);
        return infoServiceClient;
    }
}
