package com.wangtao.nacos.config.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2026-04-05
 */
@Slf4j
@RequestMapping("/dynamic")
@RestController
public class DynamicThreadController {

    @Qualifier("dynamicThreadPool")
    @Autowired
    private ThreadPoolExecutor executor;

    @GetMapping("/execute")
    public void execute() {
        for (int i = 0; i < 50; i++) {
            final int finalI = i;
            executor.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.info("task-{} finished", finalI);
            });
        }
    }

    @GetMapping("/monitorInfo")
    public void monitorInfo() {
        log.info("corePoolSize: {} maximumPoolSize: {}", executor.getCorePoolSize(), executor.getMaximumPoolSize());
        log.info("poolSize: {} largestPoolSize: {}", executor.getPoolSize(), executor.getLargestPoolSize());
        log.info("poolSize: {} largestPoolSize: {}", executor.getPoolSize(), executor.getLargestPoolSize());
    }
}
