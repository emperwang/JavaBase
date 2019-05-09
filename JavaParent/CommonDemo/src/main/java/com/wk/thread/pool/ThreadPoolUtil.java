package com.wk.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public class ThreadPoolUtil {

    /**
     * 缓存线程池
     * @return
     */
    public static ExecutorService cachedPool(){
        return Executors.newCachedThreadPool();
    }

    /**
     *  使用工程创建缓存线程池
     * @param factory
     * @return
     */
    public static ExecutorService cachedPoll(ThreadFactory factory){
        return Executors.newCachedThreadPool(factory);
    }

    /**
     * 固定线程数量的线程池
     * @param numThread
     * @return
     */
    public static ExecutorService fixedPool(int numThread){
        return Executors.newFixedThreadPool(numThread);
    }

    /**
     *  调度线程池
     * @return
     */
    public static ScheduledExecutorService schedulePool(){
        return Executors.newScheduledThreadPool(1);
    }

    /**
     * 使用工厂创建调度线程池
     * @param factory
     * @return
     */
    public static ScheduledExecutorService scheduledPool(ThreadFactory factory){
        return Executors.newScheduledThreadPool(1,factory);
    }
}
