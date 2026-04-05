package com.wangtao.nacos.config;

import com.wangtao.nacos.config.dynamic.thread.DynamicThreadPoolProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author wangtao
 * Created at 2023/6/10 15:29
 */
@EnableConfigurationProperties({DynamicThreadPoolProperties.class})
@EnableDiscoveryClient
@SpringBootApplication
public class NacosConfigApplication {

    public static void main(String[] args) throws SQLException {
        ConfigurableApplicationContext ac = SpringApplication.run(NacosConfigApplication.class, args);
        System.out.println(ac.getParent());
        Environment environment = ac.getEnvironment();
        System.out.println(environment);
        DataSource dataSource = ac.getBean(DataSource.class);
        System.out.println(dataSource.getConnection());
    }
}
