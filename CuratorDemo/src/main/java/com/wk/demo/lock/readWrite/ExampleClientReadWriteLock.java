package com.wk.demo.lock.readWrite;

import com.wk.demo.lock.interProcessMutex.FakeLimitedResources;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *  类似于ReentrantReadWriteLock。一个是读写锁管理一对相关的锁。 一个负责读操作，另一个负责写操作
 *  读操作在写锁没被使用时可同时由多个进程使用，而写锁使用时不允许读(阻塞).此锁是可重入的
 *  一个拥有写锁的线程可重入读锁,但是读锁却不能进入写锁,这有意味着写锁可以降级成读锁.
 *  比如: 写锁 -> 读锁 -> 释放锁. 从读锁升级成写锁是不行的.
 */
@Slf4j
public class ExampleClientReadWriteLock {
    private InterProcessReadWriteLock readWriteLock;    // 读写锁
    private InterProcessMutex readLock;
    private InterProcessMutex writeLock;
    private FakeLimitedResources resources;
    private String clientName;

    public ExampleClientReadWriteLock(InterProcessReadWriteLock readWriteLock,
                                      FakeLimitedResources resources, String clientName) {
        this.readWriteLock = readWriteLock;
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();
        this.resources = resources;
        this.clientName = clientName;
    }

    public void doWork(Long time , TimeUnit timeUnit) throws Exception {
        // 先获取写锁
        if (!writeLock.acquire(time,timeUnit)){
            throw new IllegalStateException(clientName + " could not acquire the writeLock!");
        }
        log.info(clientName +"   has the wirtelock...........");
        // 降级成读锁
        if (!readLock.acquire(time,timeUnit)){
            throw new IllegalStateException(clientName + " could not acquire the writeLock!");
        }

        log.info(clientName + "  has the readLock...");
        try{
            resources.use();
        } finally {
            // 锁释放
            readLock.release();
            writeLock.release();
        }
    }

    public static void main(String[] args)  {
        int QTY = 5;
        int REPETITIONS = 5;
        String path = "/example/locks";
        String CONNECT_ADDR = "192.168.72.11:2181";
        FakeLimitedResources fakeLimitedResources = new FakeLimitedResources();
        ExecutorService executorService = Executors.newFixedThreadPool(QTY);
        for (int  i=0;i<QTY; i++){
            int index = 1;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    CuratorFramework curator = CuratorFrameworkFactory
                            .newClient(CONNECT_ADDR, new RetryNTimes(3, 10000));
                    curator.start();
                    try {
                        ExampleClientReadWriteLock exampleClientReadWriteLock = new ExampleClientReadWriteLock(new InterProcessReadWriteLock(curator, path), fakeLimitedResources,
                                "client " + index);
                        for (int j = 0; j < REPETITIONS; j++) {
                            exampleClientReadWriteLock.doWork(10L, TimeUnit.SECONDS);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        CloseableUtils.closeQuietly(curator);
                    }
                }
            };
            executorService.submit(runnable);
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
