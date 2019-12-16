package com.wk.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {

    public static ExecutorService fixedPool(int coreSize){
        ExecutorService executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                coreSize+20,20, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(100),new ThreadPoolExecutor.CallerRunsPolicy());
        return executorService;
    }
}
