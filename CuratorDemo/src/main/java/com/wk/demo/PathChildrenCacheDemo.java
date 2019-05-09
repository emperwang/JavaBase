package com.wk.demo;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * 监听器  监听子节点的新增  修改  删除操作
 */
public class PathChildrenCacheDemo {
    private static final String CONNECT_ADDR="192.168.72.15:2181";
    private static final int SESSION_TIMEOUT=5000;

    public static void main(String[] args) {
        RetryPolicy policy = new ExponentialBackoffRetry(1000,10);
        CuratorFramework curator = CuratorFrameworkFactory.builder().retryPolicy(policy).connectString(CONNECT_ADDR)
                .sessionTimeoutMs(SESSION_TIMEOUT).build();
        curator.start();

        //第三个参数表示是否接受节点数据内容
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curator, "/super", true);
        /**
         * 如果不填写这个参数   则无法监听到子节点的数据更新
         * PathChildrenCache.StartMode.BUILD_INITIAL_CACHE 会预先创建之前指定的super节点
         * PathChildrenCache.StartMode.POST_INITIALIZED_EVENT  效果与BUILD_INITIAL_CACHE一样  只是不会预先创建super节点
         * PathChildrenCache.StartMode.NOEMAL  与不填写参数是同样的效果  不会监听子节点的数据更新操作
         */
        try {
            pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            pathChildrenCache.getListenable().addListener(((curatorFramework, event) -> {
                switch (event.getType()){
                    case CHILD_ADDED:
                        System.out.println("CHILD_ADDED 类型:"+event.getType()+",路径:"+
                        event.getData().getPath()+"，数据:"+event.getData().getData()+",状态:"+
                        event.getData().getStat());
                        break;
                    case CHILD_UPDATED:
                        System.out.println("CHILD_UPDATED 类型:"+event.getType()+",路径:"+
                                event.getData().getPath()+"，数据:"+event.getData().getData()+",状态:"+
                                event.getData().getStat());
                        break;
                    case CHILD_REMOVED:
                        System.out.println("CHILD_REMOVED 类型:"+event.getType()+",路径:"+
                                event.getData().getPath()+"，数据:"+event.getData().getData()+",状态:"+
                                event.getData().getStat());
                        break;
                    default: break;
                }
            }));
            //这里是给出测试样例  程序运行不到这里
            curator.create().forPath("/super","123".getBytes());
            curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                    .forPath("/super/c1","c1data".getBytes());
            //测试 不会监听本节点的数据变更 只会监听指定节点下子节点数据的变更
            curator.setData().forPath("/super","456".getBytes());
            curator.setData().forPath("/super/c1","c1newdata".getBytes());
            curator.delete().guaranteed().deletingChildrenIfNeeded().forPath("/super/c1");
            curator.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
