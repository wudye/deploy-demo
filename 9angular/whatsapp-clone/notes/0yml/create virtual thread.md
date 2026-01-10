方法 1：使用 Thread.ofVirtual() （推荐）

// 创建并启动一个虚拟线程
Thread.ofVirtual().start(() -> {
System.out.println("Hello from virtual thread!");
System.out.println("Current thread: " + Thread.currentThread());
});

// 输出：
// Hello from virtual thread!
// Current thread: VirtualThread[#21]/runnable@ForkJoinPool-1-worker-1

创建方式	适用场景	示例
Thread.ofVirtual().start()	简单任务	Thread.ofVirtual().start(() -> {...})
Executors.newVirtualThreadPerTaskExecutor()	批量处理	try (var e = Executors.newVirtualThreadPerTaskExecutor()) {...}
Spring Boot 自动	Web 应用	配置 spring.threads.virtual.enabled=true
StructuredTaskScope	结构化并发	new StructuredTaskScope.Shu