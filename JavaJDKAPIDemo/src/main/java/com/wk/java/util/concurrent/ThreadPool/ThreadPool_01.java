package com.wk.java.util.concurrent.ThreadPool;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 测试FixecThreadPool
 * 这是一个由固定线程的线程池
 */
public class ThreadPool_01 {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for (int i=0;i<10;i++){
            //添加任务到线程池中， 这里使用了lamba表达式
            threadPool.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
            });
        }
        System.out.println(threadPool);
        threadPool.shutdown();
        System.out.println(threadPool.isTerminated());
        System.out.println(threadPool.isShutdown());
        System.out.println(threadPool);
    }
}
