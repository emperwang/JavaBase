package com.wk.demo.barrier;

import com.wk.demo.lock.interProcessMutex.FakeLimitedResources;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *  输出:
 *   – client#0 enter
 – client#4 enter
 – client#1 enter
 – client#3 enter
 – client#1 begin
 – client#2 begin
 – client#0 begin
 – client#3 begin
 – client#4 begin
 – Client#1 left
 – Client#3 left
 – Client#0 left
 – Client#4 left
 – Client#2 left

 */

@Slf4j
public class DistributeDoubleBarierExample {
    private static FakeLimitedResources resources;
    private static String ConnectAddr = "192.168.72.11:2181";
    private static String path = "/example/double/barrier";

    public static void main(String[] args) {
        CuratorFramework curator = CuratorFrameworkFactory.newClient(ConnectAddr, new RetryNTimes(3, 1000));
        curator.start();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        try{
            for(int i =0 ; i< 5; i++) {
                DistributedDoubleBarrier doubleBarrier = new DistributedDoubleBarrier(curator,
                        path, 5);
                final int index = i;
                Callable<Void> task = ()->{
                    Thread.sleep((long)(3 * Math.random()));
                    log.info("client#"+index+" enter");
                    doubleBarrier.enter();
                    log.info("client#"+index+" begin");
                    Thread.sleep((long)(3000*Math.random()));
                    doubleBarrier.leave();
                    log.info("Client#"+index+ " left");
                    return null;
                };
                executorService.submit(task);
            }
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            CloseableUtils.closeQuietly(curator);
        }
    }
}
