package com.mwu.whatsappclone.demo.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class VirtualThreads {
    public static void main(String[] args) throws InterruptedException {
        // 创建虚拟线程执行器（无限制）
        var executor = Executors.newVirtualThreadPerTaskExecutor();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            final int requestId = i;
            executor.submit(() -> {
                // 模拟 HTTP 请求（阻塞 I/O）
                try {
                    Thread.sleep(100); // 阻塞 100 毫秒
                    System.out.println("Request " + requestId + " completed");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        boolean finished = executor.awaitTermination(1, TimeUnit.MINUTES);

        long endTime = System.currentTimeMillis();
        if (!finished) {
            System.out.println("Warning: not all tasks finished within timeout");
        }

        System.out.println("Total time: " + (endTime - startTime) + "ms");
        System.out.println("Approximate throughput: " + (10000 * 1000 / (endTime - startTime)) + " req/s");
    }
}
