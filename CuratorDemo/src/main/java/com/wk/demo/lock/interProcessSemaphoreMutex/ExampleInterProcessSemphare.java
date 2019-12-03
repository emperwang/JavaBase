package com.wk.demo.lock.interProcessSemaphoreMutex;

import com.wk.demo.lock.interProcessMutex.FakeLimitedResources;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExampleInterProcessSemphare {
    // 不可重入锁，也就是不能在同一个线程中多次获取
    private InterProcessSemaphoreMutex interProcessSemaphoreMutex;
    private FakeLimitedResources resources;
    private String clientName;

    public ExampleInterProcessSemphare(InterProcessSemaphoreMutex interProcessSemaphoreMutex,
                                       FakeLimitedResources resources, String clientName) {
        this.interProcessSemaphoreMutex = interProcessSemaphoreMutex;
        this.resources = resources;
        this.clientName = clientName;
    }

    public void doWorker(long time, TimeUnit timeUnit) throws Exception {
        if (! interProcessSemaphoreMutex.acquire(time,timeUnit)){
            throw new IllegalStateException(clientName+" could not acquire a lock");
        }
        log.info(clientName +"  has lock..");
        // IllegalStateException: Client1 could not acquire a lock   重复获取锁失败
        /*if (! interProcessSemaphoreMutex.acquire(time,timeUnit)){
            throw new IllegalStateException(clientName+" could not acquire a lock");
        }*/
        try{
            resources.use();
        }finally {
            log.info(clientName +"   relaese the lock..");
            interProcessSemaphoreMutex.release();
            //interProcessSemaphoreMutex.release();
        }
    }

    public static void main(String[] args) {
        int QTY = 5;
        int REPETITIONS = QTY * 5;
        String PATH = "/example/locks";
        String CONNECT_ADDR = "192.168.72.11:2181";
        FakeLimitedResources fakeLimitedResources = new FakeLimitedResources();
        ExecutorService executorService = Executors.newFixedThreadPool(QTY);
        for (int i = 0; i < 1; i++) {
            int index = 1;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    CuratorFramework curator = CuratorFrameworkFactory.newClient(CONNECT_ADDR,
                            new RetryNTimes(3, 10000));
                    curator.start();
                    try {
                        ExampleInterProcessSemphare interProcessSemphare = new ExampleInterProcessSemphare(
                                new InterProcessSemaphoreMutex(curator,PATH),
                                fakeLimitedResources,"Client" + index);

                        for (int j = 0; j < REPETITIONS; j++) {
                            interProcessSemphare.doWorker(10, TimeUnit.SECONDS);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        CloseableUtils.closeQuietly(curator);
                    }
                }
            };
            executorService.execute(runnable);
        }
    }

}

