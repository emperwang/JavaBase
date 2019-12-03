package com.wk.demo.barrier;

import com.wk.demo.lock.interProcessMutex.FakeLimitedResources;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *  client#4 wait on barrier..
 client#1 wait on barrier..
 client#0 wait on barrier..
 client#3 wait on barrier..
 client#1 begins
 client#0 begins
 client#4 begins
 client#2 begins
 client#3 begins
 */
@Slf4j
public class DistributeBarrierExample {
    private static FakeLimitedResources resources;
    private static String ConnectAddr = "192.168.72.11:2181";
    private static String path = "/example/barrier";

    public static void main(String[] args) {
        CuratorFramework curator = CuratorFrameworkFactory.newClient(ConnectAddr, new RetryNTimes(3, 10000));
        curator.start();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        DistributedBarrier barrier = new DistributedBarrier(curator, path);
        try {
            barrier.setBarrier();
            for (int i=0; i < 5; i++){
                DistributedBarrier tmpBarrier = new DistributedBarrier(curator, path);
                final int index = i;
                Callable<Void> task = () ->{
                    Thread.sleep((long)(3 * Math.random()));
                    log.info("client#"+index+" wait on barrier..");
                    tmpBarrier.waitOnBarrier();
                    log.info("client#"+index+" begins");
                    return null;
                };
                executorService.submit(task);
            }
            Thread.sleep(5000);
            barrier.removeBarrier();
            Thread.sleep(5000);
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            CloseableUtils.closeQuietly(curator);
        }

    }
}
