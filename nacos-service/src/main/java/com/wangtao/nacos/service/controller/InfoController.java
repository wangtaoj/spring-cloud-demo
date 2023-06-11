package com.wangtao.nacos.service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2023/6/11 08:43
 */
@Slf4j
@RequestMapping("/info")
@RestController
public class InfoController {

    @Autowired
    private ServerProperties serverProperties;

    private boolean isRetry = true;

    @GetMapping("/port")
    public int port() {
        return serverProperties.getPort();
    }

    @GetMapping("/retry")
    public int retry() {
        log.info("============retry==========");
        int timeout = isRetry ? 3 : 0;
        isRetry = !isRetry;
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return serverProperties.getPort();
    }
}
