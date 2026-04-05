package com.wangtao.nacos.config.dynamic.thread;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wangtao
 * Created at 2026-04-05
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "dynamic.thread")
public class DynamicThreadPoolProperties {

    private int corePoolSize = 1;

    private int maximumPoolSize = 1;
}
