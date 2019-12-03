package com.wk.demo.dictributeInte;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.atomic.PromotedToLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.TimeUnit;

/**
 * 分布式计数器
 */
public class DistributedIntegerDemo {
    private static final String CONNECT_ADDR = "192.168.72.15:2181";
    private static final int SESSION_TIMEOUT = 50000;

    public static void main(String[] args) {
        //重试策略
        RetryPolicy policy = new ExponentialBackoffRetry(1000,10);

        CuratorFramework curator = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDR)
                .sessionTimeoutMs(SESSION_TIMEOUT).retryPolicy(policy).build();
        curator.start();
        // promotedToLock 对分布式计数器添加了分布式锁
        PromotedToLock promotedToLock = PromotedToLock.builder().lockPath("/atomic/lock").retryPolicy(new RetryNTimes(3, 1000))
                .timeout(10, TimeUnit.SECONDS).build();
        DistributedAtomicInteger distributedAtomicInteger = new DistributedAtomicInteger(curator, "/super",
                new RetryNTimes(3, 1000),promotedToLock);
        try {
            AtomicValue<Integer> value = distributedAtomicInteger.add(1);
            System.out.println(value.succeeded());
            System.out.println(value.preValue());
            System.out.println(value.postValue());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            curator.close();
        }
    }
}
