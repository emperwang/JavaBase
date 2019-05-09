package com.wk.demo;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CuratorDemo {
    private static final String CONNECT_ADDR="192.168.72.15:2181";
    private static final int SESSION_TIMEOUT=50000;

    public static void main(String[] args) throws Exception {
        //重试策略  初试时间1秒  重试10次
        RetryPolicy retry = new ExponentialBackoffRetry(1000, 10);
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDR)
                .sessionTimeoutMs(SESSION_TIMEOUT).retryPolicy(retry);
        CuratorFramework curator = builder.build();
        //开启连接
        curator.start();
        ExecutorService executorService = Executors.newCachedThreadPool();

        /**
         * 创建节点
         * creatingParentsIfNeeded  如果父节点不存在  连同父节点一起创建
         * withMode   创建的节点的模式
         * inBackground  回调函数
         * forpath  创建的节点的路径 以及其内容
         */
        curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .inBackground(((curatorFramework, curatorEvent) -> { //添加回调函数
                    System.out.println("Code:"+curatorEvent.getResultCode());
                    System.out.println("Type:"+curatorEvent.getType());
                    System.out.println("Path:"+curatorEvent.getPath());
                }),executorService).forPath("/surper/c1","C1cContent".getBytes());
        Thread.sleep(5000); //为了能看到回调信息

        //获取节点的信息
        String s = new String(curator.getData().forPath("/surper/c1"));
        System.out.println(s);
        //检查节点是否存在
        Stat stat = curator.checkExists().forPath("/surper/c1");
        System.out.println(stat);
        curator.setData().forPath("/surper/c1","c1NewData".getBytes());
        s = new String(curator.getData().forPath("/surper/c1"));
        System.out.println(s);

        List<String> childrens = curator.getChildren().forPath("/surper");
        for (String children:childrens){
            System.out.println(children);
        }

        //删除节点 deletingChildrenIfNeeded 连同子节点一起删除
        /**
         * guaranteed  安全删除
         */
        curator.delete().guaranteed().deletingChildrenIfNeeded().forPath("/surper");
        curator.close();
    }
}
