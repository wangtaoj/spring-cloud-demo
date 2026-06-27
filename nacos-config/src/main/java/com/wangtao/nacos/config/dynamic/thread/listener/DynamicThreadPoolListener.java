package com.wangtao.nacos.config.dynamic.thread.listener;

import com.wangtao.nacos.config.dynamic.thread.DynamicThreadPoolFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;

/**
 * @author wangtao
 * Created at 2026-06-27
 */
@Slf4j
public class DynamicThreadPoolListener implements ApplicationListener<EnvironmentChangeEvent> {

    private final List<DynamicThreadPoolFactoryBean> dynamicThreadPoolFactoryBeans;

    public DynamicThreadPoolListener(List<DynamicThreadPoolFactoryBean> dynamicThreadPoolFactoryBeans) {
        this.dynamicThreadPoolFactoryBeans = dynamicThreadPoolFactoryBeans;
    }

    @Override
    public void onApplicationEvent(@NonNull EnvironmentChangeEvent event) {
        Set<String> changedKeys = event.getKeys();
        if (changedKeys.isEmpty()) {
            return;
        }
        for (DynamicThreadPoolFactoryBean dynamicThreadPoolFactoryBean : dynamicThreadPoolFactoryBeans) {
            String configPrefix = dynamicThreadPoolFactoryBean.getConfigPrefix();
            boolean needAdjust = false;
            for (String changedKey : changedKeys) {
                if (changedKey.startsWith(configPrefix + ".")) {
                    needAdjust = true;
                    break;
                }
            }
            if (needAdjust) {
                try {
                    dynamicThreadPoolFactoryBean.adjustThreadPool();
                } catch (Exception e) {
                    log.error("{} thread pool adjust error", dynamicThreadPoolFactoryBean.getDynamicThreadPoolName(), e);
                }
            }
        }
    }
}
