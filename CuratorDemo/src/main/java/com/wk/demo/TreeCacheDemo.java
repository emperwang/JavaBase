package com.wk.demo;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 监听器  既可以监听本节点状态 又可以监听子节点状态
 * 类似于 NodeCache和PathChildrenCache的结合
 */
public class TreeCacheDemo {
    private static final String CONNECT_ADDR="192.168.72.15:2181";
    private static final int SESSION_TIMEOUT=5000;

    public static void main(String[] args) throws Exception {
        RetryPolicy policy = new ExponentialBackoffRetry(1000,10);
        CuratorFramework curator = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDR)
                .sessionTimeoutMs(SESSION_TIMEOUT).retryPolicy(policy).build();
        curator.start();
        TreeCache treeCache = new TreeCache(curator, "/treeCache");
        treeCache.start();
        treeCache.getListenable().addListener((curatorFramework,event)->{
            switch (event.getType()){
                case NODE_ADDED:
                    System.out.println("CHILD_ADDED 类型:"+event.getType()+",路径:"+
                            event.getData().getPath()+"，数据:"+event.getData().getData()+",状态:"+
                            event.getData().getStat());
                    break;
                case NODE_UPDATED:
                    System.out.println("NODE_UPDATED 类型:"+event.getType()+",路径:"+
                            event.getData().getPath()+"，数据:"+event.getData().getData()+",状态:"+
                            event.getData().getStat());
                    break;
                case NODE_REMOVED:
                    System.out.println("NODE_REMOVED 类型:"+event.getType()+",路径:"+
                            event.getData().getPath()+"，数据:"+event.getData().getData()+",状态:"+
                            event.getData().getStat());
                    break;
                default: break;
            }
        });
    }
}
