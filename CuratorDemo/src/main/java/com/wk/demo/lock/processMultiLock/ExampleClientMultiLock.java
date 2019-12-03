package com.wk.demo.lock.processMultiLock;

import com.wk.demo.lock.interProcessMutex.FakeLimitedResources;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.RetryNTimes;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExampleClientMultiLock {
    private static FakeLimitedResources resources;
    private static String ConnectAddr = "192.168.72.11:2181";
    private static String path = "/example/locks";

    public static void main(String[] args) {
        resources = new FakeLimitedResources();
        CuratorFramework curator = CuratorFrameworkFactory.newClient(ConnectAddr,
                new RetryNTimes(3, 1000));
        curator.start();
        InterProcessLock lock1 = new InterProcessMutex(curator,path);
        InterProcessLock lock2 = new InterProcessSemaphoreMutex(curator,path);
        InterProcessMultiLock multiLock = new InterProcessMultiLock(Arrays.asList(lock1, lock2));

        try {
            if (!multiLock.acquire(10, TimeUnit.SECONDS)){
                throw new IllegalStateException("could not acquire the lock");
            }
            log.info("has lock....");
            log.info("has lock1 : "+ lock1.isAcquiredInThisProcess());
            log.info("has lock2 : "+ lock2.isAcquiredInThisProcess());
            try {
                resources.use();
            }finally {
                multiLock.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("end has lock1 : "+ lock1.isAcquiredInThisProcess());
        log.info("end has lock2 : "+ lock2.isAcquiredInThisProcess());
    }
}
