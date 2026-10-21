package com.wangtao.nacos.consumer.config;

import feign.Retryer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author wangtao
 * Created at 2023/6/11 11:01
 */
public class FeignDefultConfig {

    @ConditionalOnMissingBean
    @Bean
    public Retryer retryer() {
        // 表示重试第一次间隔为1000ms, 随后间隔会递增, 最大间隔为2000ms, 最多重试3次
        return new Retryer.Default(1000, 2000, 3);
    }
}
