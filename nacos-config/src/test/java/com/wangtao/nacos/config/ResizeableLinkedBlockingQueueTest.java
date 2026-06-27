package com.wangtao.nacos.config;

import com.wangtao.nacos.config.dynamic.thread.queue.ResizeableLinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2026-06-27
 */
@Slf4j
public class ResizeableLinkedBlockingQueueTest {

    public static void main(String[] args) {
        ResizeableLinkedBlockingQueue<Integer> queue = new ResizeableLinkedBlockingQueue<>(5);
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    queue.put(i);
                    log.info("put: {} finished", i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("queue capacity grow");
        queue.setCapacity(10);

    }
}
