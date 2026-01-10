package com.mwu.whatsappclone.demo.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CreateThreadVitualDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread t = Thread.ofVirtual()
                .name("virtual-thread-demo")
                .start(() -> {
                    System.out.println("Hello from a virtual thread!");
                });

        // main thread will not 等待虚拟线程执行完成, can not能看到输出
        batchThreads();
        // main thread will wait for virtual threads to complete
        List<Thread> threadList = improveBatchThreads();
        for (Thread thread : threadList) {
            thread.join();
        }
        // improve: use executor to batch create virtual threads and wait for completion

        batchThreadsAndWait();
        t.join();


        // use 使用 Executors.newVirtualThreadPerTaskExecutor() （推荐用于批处理）
        batchProcess();

    }

    private static void batchProcess() {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            res.add("task-" + i);
        }
        try (var excutor = Executors.newVirtualThreadPerTaskExecutor()) {
           res.forEach(task ->{
               excutor.submit(() -> {
                   processMockDatabase(task);
                });
           });
        }
    }

    private static void processMockDatabase(String task) {
        // 模拟耗时操作（如数据库查询）

        try {
            Thread.sleep(100);
            System.out.println("Processing " + task + " in " + Thread.currentThread().getName());
        } catch (InterruptedException e
        ) {
            Thread.currentThread().interrupt();
        }
    }

    private static List<Thread> improveBatchThreads() {
        List<Thread> threadList =  new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            final int index = i;
            Thread t = Thread.ofVirtual()
                    .name("virtual-thread-", 11)
                    .start(() -> {
                        System.out.println(Thread.currentThread().getName() + "  "  + index + " is running.");
                    });
            threadList.add(t);
        }
        return threadList;
    }

    private static void batchThreadsAndWait() throws InterruptedException {
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        try {
            for (int i = 0; i < 1000; i++) {
                final int index = i;
                executorService.submit(() -> {
                    System.out.println(Thread.currentThread().getName() + "  "  + index + " is running.");
                });
            }
        } finally {
            executorService.shutdown();
            if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
                System.out.println("Warning: not all tasks finished within timeout");
            }
        }
    }

    private static void batchThreads() {
        for (int i = 0; i < 1000; i++) {
            final int index = i;
            Thread.ofVirtual()
                    .name("virtual-thread-", 11)
                    .start(() -> {
                System.out.println(Thread.currentThread().getName() + "  "  + index + " is running.");
            });
        }
    }
}
