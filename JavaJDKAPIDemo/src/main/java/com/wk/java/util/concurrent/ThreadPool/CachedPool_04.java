package com.wk.java.util.concurrent.ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 *  cachePool使用SynchronousQueue这个队列，也就是来一个任务，创建一个线程
 */
public class CachedPool_04 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();
        System.out.println(service);
        for (int i = 0; i < 3; i++) {
            service.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
            });
        }
        System.out.println(service);
        //闲暇60秒的线程会被回收
        TimeUnit.SECONDS.sleep(80);
        System.out.println(service);
    }
}
