package com.wk.demo.noauth;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**没有认证的测试
 * 事件类型
 *
 *
 * 状态类型
 */
public class ZkUtil implements Watcher{
    //设置会话超时时间
    private static final int SESSION_TIMEOUT=20000;
    //连接地址
    private static final String CONNNECTION_ADDR="192.168.72.15:2181";
    //父目录
    private static final String PARENT_PATH = "/testWatcher";
    //子目录
    private static final String CHILDREN_PATH="/testWatcher/children";
    //进入表示
    private static final String LOG_PREFIX_OF_MAIN="[main]";
    //信号量  用户等待zookeeper连接建立
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    //zk变量
    private ZooKeeper zk = null;

    /**
     * 创建连接
     * @param connectAddr 连接地址
     * @param sessionTimeout 超时时间
     */
    public void createConnection(String connectAddr,int sessionTimeout){
        this.releaseConnection();
        try {
            zk = new ZooKeeper(connectAddr,sessionTimeout,this);
            System.out.println(LOG_PREFIX_OF_MAIN+"开始连接zk服务器");
            countDownLatch.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     */
    public void releaseConnection(){
        if(this.zk!=null){
            try {
                this.zk.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean createPath(String path,String data){
        try {
            //设置监控(由于zookeeper监控都是一次性的 所以每次必须设置监控)
            this.zk.exists(path,true);
            String s = this.zk.create(
                    path,  //创建的节点路径
                    data.getBytes(),  //节点数据
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, //所有可见  权限
                    CreateMode.PERSISTENT //持久化
            );
            System.out.println(LOG_PREFIX_OF_MAIN+"节点创建成，path"
            +s+"content :"+data);

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 读取指定节点的数据
     * @param path  路径
     * @param needWatch  是否需要设置观察者
     * @return
     */
    public String readData(String path,boolean needWatch){
        try {
            byte[] data = this.zk.getData(path, needWatch, null);
            return new String(data);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean writeData(String path,String data){
        try {
            Stat stat = this.zk.setData(path, data.getBytes(), -1);
            System.out.println(LOG_PREFIX_OF_MAIN+"更新数据,stat:"+stat);
            return true;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除节点
     * @param path  节点路径
     */
    public void deleteNode(String path){
        try {
            this.zk.delete(path,-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断节点是否存在
     * @param path  节点路径
     * @param needWatch  是否需要观察者
     * @return
     */
    public Stat existNode(String path,boolean needWatch){
        try {
            Stat exists = this.zk.exists(path, needWatch);
            return exists;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  获取子节点
     * @param path  父节点路径
     * @param needWatch  是否需要观察者
     * @return
     */
    public List<String> getChildren(String path,boolean needWatch){
        try {
            List<String> children = this.zk.getChildren(path, needWatch);
            return children;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除所有节点
     */
    public void deleteAllTestPath(){
        if(this.existNode(CHILDREN_PATH,false) != null){
            this.deleteNode(CHILDREN_PATH);
        }
        if(this.existNode(PARENT_PATH,false) != null){
            this.deleteNode(PARENT_PATH);
        }
    }

    /**
     * 收到来自server的watcher通知后的watcher的处理
     * @param watchedEvent
     *
     * Event.KeeperState.SyncConnected;   连接建立
     * Event.KeeperState.AuthFailed;  认证失败
     * Event.KeeperState.Disconnected; 断开连接
     * Event.KeeperState.Expired;  会话过期
     *
     * Event.EventType.NodeChildrenChanged;  子节点改变
     * Event.EventType.NodeCreated;  节点创建
     * Event.EventType.NodeDataChanged;  节点数据改变
     * Event.EventType.NodeDeleted;  节点删除
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("进入watcher的处理...event = "+watchedEvent);
        if(watchedEvent == null){
            return;
        }
        //链接状态
        Event.KeeperState state = watchedEvent.getState();
        //事件类型
        Event.EventType type = watchedEvent.getType();
        //受影响状态
        String path = watchedEvent.getPath();

        System.out.println("收到watcher通知");
        System.out.println("连接状态\t"+state);
        System.out.println("事件类型\t"+type);

        if(Event.KeeperState.SyncConnected == state){
            //成功连接上zk服务器
            if (Event.EventType.None == type){
                System.out.println("成功连接上zk服务器");
                countDownLatch.countDown();;
            }
            //创建节点
            else if(Event.EventType.NodeCreated == type){
                System.out.println("节点创建");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.existNode(path,true);
            }
            //更新节点
            else if(Event.EventType.NodeDataChanged == type){
                System.out.println("节点数据更新");
            }
            //更新子节点
            else if(Event.EventType.NodeChildrenChanged == type){
                System.out.println("子节点变更");
            }
            //删除节点
            else if(Event.EventType.NodeDeleted == type){
                System.out.println("删除节点");
            }
        }else if(Event.KeeperState.Disconnected == state){
            System.out.println("与zk服务器断开连接");
        }else if(Event.KeeperState.AuthFailed == state){
            System.out.println("权限检查失败");
        }else if(Event.KeeperState.Expired == state){
            System.out.println("会话失效");
        }
    }
}
