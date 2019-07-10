package com.wk.concurrent.ThreadPool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//调度线程池
public class SchedulePool {
    public static void main(String[] args) {
        //创建核心线程是4的线程池
        ScheduledExecutorService service = Executors.newScheduledThreadPool(3);
        //添加任务到线程池，每500ms执行一次
        service.scheduleAtFixedRate(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
        },0,500,TimeUnit.MILLISECONDS);
    }
}
