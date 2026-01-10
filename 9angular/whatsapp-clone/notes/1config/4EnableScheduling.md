@EnableScheduling 详解
@EnableScheduling 是 Spring 框架提供的注解，用于启用 Spring 的任务调度功能，让开发者能够轻松创建定时任务。
fixedDelay vs fixedRate
特性	fixedDelay	fixedRate
执行时机	上次执行完成后再等待指定时间	从上次开始执行时算起，固定间隔执行
适用场景	任务执行时间不确定，需要保证任务不重叠	需要精确的时间间隔，如心跳检测
示例	fixedDelay = 5000 → 执行完成后等 5 秒再执行	fixedRate = 5000 → 每 5 秒执行一次
图解:

fixedDelay (任务耗时 3 秒):
[执行 3s] [等待 5s] [执行 3s] [等待 5s]
0s        3s         8s     11s       16s

fixedRate (任务耗时 3 秒):
[执行 3s] [等待 2s] [执行 3s] [等待 2s]
0s        3s         5s     8s        10s

cron 表达式
格式： 秒 分 时 日 月 周

Java
插入
复制
新建文件
保存
应用代码
@Scheduled(cron = "0 0 2 * * ?")  // 每天 02:00:00 执行
@Scheduled(cron = "0 */5 * * * ?") // 每 5 分钟执行
@Scheduled(cron = "0 0 12 * * MON-FRI") // 工作日中午 12 点执行
@Scheduled(cron = "0 0 0 1 * ?")   // 每月 1 号零点执行
字段	允许值	特殊字符
秒	0-59	, - * /
分	0-59	, - * /
时	0-23	, - * /
日	1-31	, - * / ? L W C
月	1-12	, - * /
周	1-7 (1=周日)	, - * / ? L C #

特性	说明
用途	启用 Spring 任务调度功能
核心注解	@Scheduled
调度方式	fixedDelay , fixedRate , cron
线程模型	默认单线程，可配置线程池
异常处理	任务异常不会停止调度
典型应用	数据清理、心跳检测、定时推送、统计任务
@EnableScheduling 为 WhatsApp 克隆这类实时通讯应用提供了强大的后台任务处理能力，是实现自动化运维和系统维护的关键组件。

1. 默认单线程执行
   Spring 的调度器默认使用单线程执行所有任务，可能导致任务排队：
   // 任务 A 执行 10 秒
   @Scheduled(fixedRate = 1000)
   public void taskA() throws InterruptedException {
   Thread.sleep(10000);
   }

// 任务 B 会等待任务 A 完成后再执行
@Scheduled(fixedRate = 1000)
public void taskB() {
System.out.println("Task B"); // 可能延迟 10 秒才执行
}

2. 配置线程池解决并发问题
   @Configuration
   @EnableScheduling
   public class SchedulingConfiguration implements SchedulingConfigurer {

   @Override
   public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
   taskRegistrar.setScheduler(taskExecutor());
   }

   @Bean(destroyMethod = "shutdown")
   public Executor taskExecutor() {
   return Executors.newScheduledThreadPool(10);
   }
   }
