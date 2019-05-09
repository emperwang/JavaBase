package com.wk.demo;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 监听器  监听节点的新增  修改操作
 */
public class NodeCacheDemo {
    private static final String CONNECT_ADDR="192.168.72.15:2181";
    private static final int SESSION_TIMEOUT=5000;

    public static void main(String[] args) {
        //间隔1s重试   重试10次
        RetryPolicy policy = new ExponentialBackoffRetry(1000,10);
        CuratorFramework curator = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDR)
                .sessionTimeoutMs(SESSION_TIMEOUT).retryPolicy(policy)
                .build();
        curator.start();
        //最后一个参数表示是否进行压缩
        NodeCache nodeCache = new NodeCache(curator, "/super", false);
        try {
            nodeCache.start(true);
            //只会监听节点的创建和修改  删除不会监听
            nodeCache.getListenable().addListener(()->{
                System.out.println("路径:"+nodeCache.getCurrentData().getPath());
                System.out.println("数据:"+new String(nodeCache.getCurrentData().getData()));
                System.out.println("状态:"+nodeCache.getCurrentData().getStat());
            });
            curator.create().forPath("/super","super".getBytes());
            Thread.sleep(1000);
            curator.setData().forPath("/super","1234".getBytes());
            Thread.sleep(1000);
            curator.delete().forPath("/super");
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
