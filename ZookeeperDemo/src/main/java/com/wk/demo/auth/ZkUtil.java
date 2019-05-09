package com.wk.demo.auth;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ZkUtil implements Watcher {
     //连接地址
     final static String CONNECT_ADDR="192.168.72.15:2181";
     //路径
     final static String PATH="/testAuth";
     final static String PATH_DEL = "/testAuth/delNode";
     //认证方式
     final static String authentication_type="digest";
     //认证正确密码
     final static String correctAuthentication="123456";
     //认证错误方法
     final static String badAuthentication="654321";

     static ZooKeeper zk = null;
     //计时器
     AtomicInteger seq = new AtomicInteger();
     //标识
     private static final String LOG_PREFIX_OF_MAIN="[MAIN]";

     private CountDownLatch countDownLatch = new CountDownLatch(1);
    @Override
    public void process(WatchedEvent watchedEvent) {

        if(watchedEvent == null){
            return;
        }
        //连接状态
        Event.KeeperState state = watchedEvent.getState();
        //事件类型
        Event.EventType type = watchedEvent.getType();
        //受影响path
        String path = watchedEvent.getPath();
        //标识
        String logPrefix = "[Watcher-"+this.seq.incrementAndGet()+"]";
        System.out.println("收到watcher通知");
        System.out.println("连接状态"+state);
        System.out.println("事件类型"+type);

        if(Event.KeeperState.SyncConnected == state){
            if(Event.EventType.None == type){
                System.out.println(logPrefix+"成功连接");
                countDownLatch.countDown();
                }
        }
        else if(Event.KeeperState.Disconnected == state){
            System.out.println(logPrefix+"与zk服务器断开连接");
        }else if(Event.KeeperState.AuthFailed == state){
            System.out.println(logPrefix+"权限检查失败");
        }else if(Event.KeeperState.Expired == state){
            System.out.println(logPrefix+"会话失效");
        }
    }

    /**
     * 创建到zk服务器的连接
     * @param connAddr  连接地址
     * @param sessionTimeout 超时时间
     */
    public void createConnection(String connAddr,int sessionTimeout){
        this.releaseConnection();
        try {
            zk = new ZooKeeper(connAddr,sessionTimeout,this);
            //添加节点授权
            zk.addAuthInfo(authentication_type,correctAuthentication.getBytes());
            System.out.println(LOG_PREFIX_OF_MAIN+"开始连接zk服务器");
            //倒数等待
            countDownLatch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放连接
     */
    public void releaseConnection(){
        if(this.zk != null){
            try {
                zk.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 采用错误密码去获取数据
     */
    public void getDataByBadAuthenication(){
        try {
            ZooKeeper badZk = new ZooKeeper(CONNECT_ADDR, 20000, null);
            //授权
            badZk.addAuthInfo(authentication_type,badAuthentication.getBytes());
            Thread.sleep(2000);
            System.out.println("获取数据"+PATH);
            byte[] data = badZk.getData(PATH, false, null);
            System.out.println("获取到的数据"+new String(data));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    /**
     * 不使用密码 获取数据
     */
    public void getDataByNoAuthtication(){
        try {
            ZooKeeper keeper = new ZooKeeper(CONNECT_ADDR, 2000, null);
            Thread.sleep(200);
            byte[] data = keeper.getData(PATH, false, null);
            System.out.println("获取到的数据:"+new String(data));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    /**
     * 采用正确密码获取数据
     */
    public void getDataByCorrectAuthentication(){
        try {
            byte[] data = zk.getData(PATH, false, null);
            System.out.println("获取到的数据:"+new String(data));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新数据 不采用密码
     */
    public void updataDataByNoAuthenication(){
        String prefix = "不使用任何授权信息";
        try {
            ZooKeeper zooKeeper = new ZooKeeper(CONNECT_ADDR, 20000, null);
            Stat exists = zooKeeper.exists(PATH, false);
            if(exists != null){
                zooKeeper.setData(PATH,prefix.getBytes(),-1);
                System.out.println(prefix+"数据跟新成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新数据  采用错误的密码
     */
    public void updateDataByBadAuthication(){
        String prefix = "使用错误的授权信息";
        try {
            ZooKeeper zooKeeper = new ZooKeeper(CONNECT_ADDR, 2000, null);
            zooKeeper.addAuthInfo(authentication_type,badAuthentication.getBytes());
            Thread.sleep(200);
            Stat exists = zooKeeper.exists(PATH, false);
            if(exists != null){
                zooKeeper.setData(PATH,prefix.getBytes(),-1);
                System.out.println(prefix+"更新成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新数据   采用正确的密码
     */
    public void updataDataByCorrectAuthenication(){
        String prefix = "使用正确的授权信息";
        try {
            Stat exists = zk.exists(PATH, false);
            if(exists != null){
                zk.setData(PATH,prefix.getBytes(),-1);
                System.out.println(prefix+"更新成功");
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 不使用密码  删除节点
     */
    public void deleteNodeByNoAuthenication(){
        String prefix = "不使用任何授权信息删除节点";
        try {
            ZooKeeper zooKeeper = new ZooKeeper(CONNECT_ADDR, 20000, null);
            Thread.sleep(200);
            Stat stat = zooKeeper.exists(PATH_DEL, false);
            if(stat != null){
                zooKeeper.delete(PATH_DEL,-1);
                System.out.println(prefix+"删除成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用错误密码 删除节点
     */
    public void deleteNodeByBadAuthenication(){
        String prefix = "使用错误密码";
        try {
            ZooKeeper zooKeeper = new ZooKeeper(CONNECT_ADDR, 20000, null);
            zooKeeper.addAuthInfo(authentication_type,badAuthentication.getBytes());
            Thread.sleep(200);
            Stat exists = zooKeeper.exists(PATH_DEL, false);
            if(exists != null){
                zooKeeper.delete(PATH_DEL,-1);
                System.out.println(prefix+"删除成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用正确密码  删除节点
     */
    public void deleteNodeByCorrectAuthication(){
        String prefix = "使用正确的授权信息";
        try {
            Stat exists = zk.exists(PATH_DEL, false);
            if(exists != null){
                zk.delete(PATH_DEL,-1);
                System.out.println(prefix+"删除成功");
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除父节点
     */
    public void deleteParent(){
        try {
            Stat exists = zk.exists(PATH, false);
            if(exists != null){
                zk.delete(PATH,-1);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ZkUtil zkUtil = new ZkUtil();
        zkUtil.createConnection(CONNECT_ADDR,20000);
        List<ACL> acls = new ArrayList<>();
        for(ACL ids_acl: ZooDefs.Ids.CREATOR_ALL_ACL){
            //System.out.println(ids_acl);
            acls.add(ids_acl);
        }
        try {
            if(zk.exists(PATH,false) == null) {
                zk.create(PATH, "init content".getBytes(), acls, CreateMode.PERSISTENT);
                System.out.println("使用授权key"+correctAuthentication+",创建节点:"+PATH);
            }
            if(zk.exists(PATH_DEL,false) == null){
                zk.create(PATH_DEL,"will be deleted".getBytes(),acls,CreateMode.PERSISTENT);
                System.out.println("使用授权key:"+correctAuthentication+",创建节点:"+PATH_DEL);
            }

            //获取数据
/*            zkUtil.getDataByNoAuthtication();
            zkUtil.getDataByBadAuthenication();*/
            zkUtil.getDataByCorrectAuthentication();

            //删除数据
            zkUtil.deleteNodeByNoAuthenication();
            zkUtil.deleteNodeByBadAuthenication();
            zkUtil.deleteNodeByCorrectAuthication();

            zkUtil.deleteParent();

            zkUtil.releaseConnection();

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
