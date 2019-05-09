package com.wk.demo.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExampleClientThatLocks {
    private InterProcessMutex lock;
    private FakeLimitedResources resources;
    private String clientName;

    public ExampleClientThatLocks(CuratorFramework framework,String path,FakeLimitedResources resources,String clientName){
        this.lock = new InterProcessMutex(framework,path);
        this.resources = resources;
        this.clientName = clientName;
    }

    public void doWork(long time, TimeUnit timeUnit) throws Exception {
        if(!lock.acquire(time,timeUnit)){
            throw new IllegalStateException(clientName+" could not acquire a lock");
        }
        System.out.println(clientName + " has a lock");

        try{
            resources.use();
        }finally {
            System.out.println(clientName+" releasing the lock");
            lock.release();
        }
    }
    /**
结果可以看到，锁是随机的被每个实例排他性的使用。既然是可重用的，
你可以在一个线程中多次调用acquire,在线程拥有锁时它总是返回true。
你不应该在多个线程中用同一个InterProcessMutex，
你可以在每个线程中都生成一个InterProcessMutex实例，
它们的path都一样，这样它们可以共享同一个锁。
 */
    public static void main(String[] args) {
        int QTY = 5;
        int REPETITIONS = QTY * 5;
        String PATH = "/example/locks";
        String CONNECT_ADDR = "192.168.72.15:2181";
        FakeLimitedResources fakeLimitedResources = new FakeLimitedResources();
        ExecutorService executorService = Executors.newFixedThreadPool(QTY);

            for (int i = 0; i < QTY; i++) {
                int index = 1;
                Callable<Void> task = () -> {
                    CuratorFramework curator = CuratorFrameworkFactory.newClient(CONNECT_ADDR,
                            new RetryNTimes(3, 1000));
                    curator.start();
                   try {
                       ExampleClientThatLocks exampleClientThatLocks = new ExampleClientThatLocks(curator, PATH, fakeLimitedResources,
                               "Client" + index);

                       for (int j = 0; j < REPETITIONS; j++) {
                           exampleClientThatLocks.doWork(10, TimeUnit.SECONDS);
                       }
                   }finally {
                       CloseableUtils.closeQuietly(curator);
                   }
                    return null;
                };
                executorService.submit(task);
            }
        executorService.shutdown();
        try {
            executorService.awaitTermination(10,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
