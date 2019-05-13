package com.wk.java.util.concurrent.ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 线程池中只有一个线程执行任务，一个线程执行可以保证任务的执行顺序
 */
public class SingledThreadPool_05 {
    public static void main(String[] args) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 5; i++) {
            int j = i;
            service.execute(()->{
                System.out.println("i= "+String.valueOf(j)+" "+Thread.currentThread().getName());
            });
        }
    }
}
