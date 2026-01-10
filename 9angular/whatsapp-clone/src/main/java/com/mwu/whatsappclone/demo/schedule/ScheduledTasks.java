package com.mwu.whatsappclone.demo.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
//
//@Component
//public class ScheduledTasks {
//
//    // 固定延迟：上次执行完成后，延迟 5 秒再执行
//    @Scheduled(fixedDelay = 5000)
//    public void fixedDelayTask() {
//        System.out.println("Fixed Delay Task: " + LocalDateTime.now());
//    }
//
//    // 固定频率：每隔 5 秒执行一次（不管上一次是否完成）
//    @Scheduled(fixedRate = 5000)
//    public void fixedRateTask() {
//        System.out.println("Fixed Rate Task: " + LocalDateTime.now());
//    }
//
//    // Cron 表达式：每天凌晨 2 点执行
//    @Scheduled(cron = "0 0 2 * * ?")
//    public void cronTask() {
//        System.out.println("Daily Cleanup Task: " + LocalDateTime.now());
//    }
//
//    // 初始延迟：应用启动后等待 10 秒，然后每 30 秒执行一次
//    @Scheduled(initialDelay = 10000, fixedRate = 30000)
//    public void initialDelayTask() {
//        System.out.println("Initial Delay Task: " + LocalDateTime.now());
//    }
//}
