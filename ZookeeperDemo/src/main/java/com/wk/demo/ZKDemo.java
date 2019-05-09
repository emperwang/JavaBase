package com.wk.demo;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKDemo {
    public static void main(String[] args) throws IOException, InterruptedException {
        String connect_addr = "192.168.72.15:2181";
        int session_outtime=200000;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper(connect_addr, session_outtime, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                //获取事件的状态
                Event.KeeperState state = watchedEvent.getState();
                //事件类型
                Event.EventType type = watchedEvent.getType();
                if(Event.KeeperState.SyncConnected == state){
                    if(Event.EventType.None == type){
                        //如果建立连接成功  则发送信号量   让后续阻塞程序向下执行s
                        System.out.println("zk 建立连接");
                        countDownLatch.countDown();
                    }
                }
            }
        });
        //进行阻塞
        countDownLatch.await();
        System.out.println("....");
        try {
            /**
             * CreateMode.PERSISTENT   持久化节点
             * CreateMode.EPHEMERAL    临时节点
             * CreateMode.EPHEMERAL_SEQUENTIAL  临时序列化节点
             * CreateMode.PERSISTENT_SEQUENTIAL  持久序列化节点
             */
            //创建父节点  临时节点
//            zooKeeper.create("/testRoot", "testData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
//                    CreateMode.PERSISTENT);
            //创建子节点  临时节点
/*            zooKeeper.create("/testRoot/children","Children data".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);*/
            //获取节点信息
            byte[] data = zooKeeper.getData("/testRoot", false, null);
            System.out.println(new String(data));
            System.out.println(zooKeeper.getChildren("/testRoot",false));

            //修改节点的值
            zooKeeper.setData("/testRoot","modify data root".getBytes(),
                    -1);
            byte[] bytes = zooKeeper.getData("/testRoot", false, null);
            System.out.println(new String(bytes));

            //判断节点是否存在
            System.out.println(zooKeeper.exists("/testRoot/children",false));

            //删除节点
            zooKeeper.delete("/testRoot/children",-1);
            System.out.println(zooKeeper.exists("/testRoot/children",false));
        } catch (KeeperException e) {
            e.printStackTrace();
        }finally {
            zooKeeper.close();
        }

    }
}
