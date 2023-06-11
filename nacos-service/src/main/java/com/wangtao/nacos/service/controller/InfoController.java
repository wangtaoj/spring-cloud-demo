package com.wangtao.nacos.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangtao
 * Created at 2023/6/11 08:43
 */
@RequestMapping("/info")
@RestController
public class InfoController {

    @Autowired
    private ServerProperties serverProperties;

    @GetMapping("/port")
    public int port() {
        return serverProperties.getPort();
    }
}
