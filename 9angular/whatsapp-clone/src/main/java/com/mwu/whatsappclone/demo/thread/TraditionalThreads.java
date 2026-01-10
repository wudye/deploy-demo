package com.mwu.whatsappclone.demo.thread;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TraditionalThreads {
    public static void main(String[] args) {
        // 创建固定大小线程池（200 个线程）
        ExecutorService executor = Executors.newFixedThreadPool(200);

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
        long endTime = System.currentTimeMillis();

        System.out.println("Total time: " + (endTime - startTime) + "ms");
        System.out.println("Approximate throughput: " + (10000 * 1000 / (endTime - startTime)) + " req/s");
    }
}
