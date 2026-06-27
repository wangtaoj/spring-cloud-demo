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
 * 完整运行信息(有些属性需要锁)
 * @author wangtao
 * Created at 2026-06-27
 */
@Endpoint(id = "dynamicThreadPoolFullInfo")
public class DynamicThreadPoolFullInfoEndpoint {

    private final List<DynamicThreadPoolFactoryBean> dynamicThreadPoolFactoryBeans;

    public DynamicThreadPoolFullInfoEndpoint(List<DynamicThreadPoolFactoryBean> dynamicThreadPoolFactoryBeans) {
        this.dynamicThreadPoolFactoryBeans = dynamicThreadPoolFactoryBeans;
    }

    @ReadOperation
    public Map<String, DynamicThreadPoolFullInfo> fullInfo() {
        Map<String, DynamicThreadPoolFullInfo> infoMap = new HashMap<>();
        for (DynamicThreadPoolFactoryBean dynamicThreadPoolFactoryBean : dynamicThreadPoolFactoryBeans) {
            DynamicThreadPoolFullInfo dynamicThreadPoolFullInfo = new DynamicThreadPoolFullInfo();
            ThreadPoolExecutor executor = dynamicThreadPoolFactoryBean.getObject();
            dynamicThreadPoolFullInfo.setCorePoolSize(executor.getCorePoolSize());
            dynamicThreadPoolFullInfo.setMaximumPoolSize(executor.getMaximumPoolSize());
            dynamicThreadPoolFullInfo.setPoolSize(executor.getPoolSize());
            dynamicThreadPoolFullInfo.setActiveCount(executor.getActiveCount());
            dynamicThreadPoolFullInfo.setLargestPoolSize(executor.getLargestPoolSize());
            dynamicThreadPoolFullInfo.setCompletedTaskCount(executor.getCompletedTaskCount());
            dynamicThreadPoolFullInfo.setQueueSize(executor.getQueue().size());
            BlockingQueue<Runnable> workQueue = executor.getQueue();
            if (workQueue instanceof ResizeableLinkedBlockingQueue) {
                dynamicThreadPoolFullInfo.setQueueCapacity(((ResizeableLinkedBlockingQueue<Runnable>) workQueue).getCapacity());
            }
            infoMap.put(dynamicThreadPoolFactoryBean.getDynamicThreadPoolName(), dynamicThreadPoolFullInfo);
        }
        return infoMap;
    }
}
