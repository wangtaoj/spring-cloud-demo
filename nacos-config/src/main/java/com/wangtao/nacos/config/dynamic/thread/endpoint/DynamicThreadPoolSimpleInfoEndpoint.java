package com.wangtao.nacos.config.dynamic.thread.endpoint;

import com.wangtao.nacos.config.dynamic.thread.DynamicThreadPoolFactoryBean;
import com.wangtao.nacos.config.dynamic.thread.queue.ResizeableLinkedBlockingQueue;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 运行信息(这些属性不需要锁)
 * @author wangtao
 * Created at 2026-06-27
 */
@Endpoint(id = "dynamicThreadPoolSimpleInfo")
public class DynamicThreadPoolSimpleInfoEndpoint {

    private final List<DynamicThreadPoolFactoryBean> dynamicThreadPoolFactoryBeans;

    public DynamicThreadPoolSimpleInfoEndpoint(List<DynamicThreadPoolFactoryBean> dynamicThreadPoolFactoryBeans) {
        this.dynamicThreadPoolFactoryBeans = dynamicThreadPoolFactoryBeans;
    }

    @ReadOperation
    public Map<String, DynamicThreadPoolSimpleInfo> simpleInfo() {
        Map<String, DynamicThreadPoolSimpleInfo> infoMap = new HashMap<>();
        for (DynamicThreadPoolFactoryBean dynamicThreadPoolFactoryBean : dynamicThreadPoolFactoryBeans) {
            DynamicThreadPoolSimpleInfo dynamicThreadPoolSimpleInfo = new DynamicThreadPoolSimpleInfo();
            ThreadPoolExecutor executor = dynamicThreadPoolFactoryBean.getObject();
            dynamicThreadPoolSimpleInfo.setCorePoolSize(executor.getCorePoolSize());
            dynamicThreadPoolSimpleInfo.setMaximumPoolSize(executor.getMaximumPoolSize());
            dynamicThreadPoolSimpleInfo.setQueueSize(executor.getQueue().size());
            BlockingQueue<Runnable> workQueue = executor.getQueue();
            if (workQueue instanceof ResizeableLinkedBlockingQueue) {
                dynamicThreadPoolSimpleInfo.setQueueCapacity(((ResizeableLinkedBlockingQueue<Runnable>) workQueue).getCapacity());
            }
            infoMap.put(dynamicThreadPoolFactoryBean.getDynamicThreadPoolName(), dynamicThreadPoolSimpleInfo);
        }
        return infoMap;
    }
}
