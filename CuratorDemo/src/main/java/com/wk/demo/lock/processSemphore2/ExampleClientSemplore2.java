package com.wk.demo.lock.processSemphore2;

import com.wk.demo.lock.interProcessMutex.FakeLimitedResources;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.apache.curator.retry.RetryNTimes;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExampleClientSemplore2 {
    private static InterProcessSemaphoreV2 semphore;
    private static String clientName;
    private static FakeLimitedResources resources;
    private static String ConnectAddr = "192.168.72.11:2181";
    private static String path = "/example/locks";

    public static void main(String[] args) {
        resources = new FakeLimitedResources();
        clientName = "clientname";
        CuratorFramework curator = CuratorFrameworkFactory
                .newClient(ConnectAddr, new RetryNTimes(3, 1000));
        curator.start();
        semphore = new InterProcessSemaphoreV2(curator,path,10);
        Collection<Lease> leases = null;
        try {
             leases = semphore.acquire(6, 10, TimeUnit.SECONDS);
            log.info("get leases:{}, content:{}",leases.size(), leases.toString());
            resources.use();
            // timeout
            Collection<Lease> leases1 = semphore.acquire(5, 10, TimeUnit.SECONDS);
            log.info("second get leases....leases1:{}",leases1.size());
        } catch (Exception e) {
            log.error("main exception:{}",e.getMessage());
        }finally {
            if (leases != null && leases.size() > 0){
                log.info("return leases....................");
                semphore.returnAll(leases);
            }
        }


    }
}
