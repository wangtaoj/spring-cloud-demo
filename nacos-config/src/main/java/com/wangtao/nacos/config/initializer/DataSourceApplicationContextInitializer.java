package com.wangtao.nacos.config.initializer;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangtao
 * Created at 2023/7/28 23:38
 */
public class DataSourceApplicationContextInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String encryptPassword = environment.getProperty("spring.datasource.password");
        if (StringUtils.isBlank(encryptPassword)) {
            return;
        }
        // 解密获取明文(这里方便起见使用的Base64编码)
        String realPassword = new String(Base64.decodeBase64(encryptPassword));
        Map<String, Object> map = new HashMap<>();
        map.put("spring.datasource.password", realPassword);
        PropertySource<?> dbPasswordPropertySource = new MapPropertySource("dbPassword", map);
        // 优先级最高, 起到属性覆盖作用
        environment.getPropertySources().addFirst(dbPasswordPropertySource);
    }

    /**
     * 最低优先级, 低于加载配置中心的ApplicationContextInitializer即可
     * PropertySourceBootstrapConfiguration
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
