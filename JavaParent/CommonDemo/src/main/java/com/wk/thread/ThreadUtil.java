package com.wk.thread;

import java.util.concurrent.ThreadFactory;

public class ThreadUtil {

    /**
     *  返回当前线程组
     * @return
     */
    public static ThreadGroup currentGroup(){
        return Thread.currentThread().getThreadGroup();
    }

    /**
     *  以给定的名字 创建一个线程组
     * @param groupName  线程组名字
     * @return
     */
    public static ThreadGroup createGroup(String groupName){
        return new ThreadGroup(groupName);
    }

    /**
     * 以给定线程组名字,线程名字,线程  创建线程并运行
     * @param group 线程组名字
     * @param runnable  要运行的线程
     * @param name  线程名字
     */
    public static void runThread(ThreadGroup group,Runnable runnable,String name){
        new Thread(group,runnable,name).start();
    }

    /**
     *  以给定的runnable接口和线程名字创建线程,并指定是否为守护线程
     * @param runnable  要运行的线程
     * @param name  线程名字
     * @param daemon  是否为守护线程
     */
    public static void runThread(Runnable runnable,String name,boolean daemon){
        Thread thread = new Thread(runnable, name);
        thread.setDaemon(daemon);
        thread.start();
    }

    /**
     *  以指定的runnbale 和 name创建线程,并运行
     * @param runnable
     * @param name
     */
    public static void runThread(Runnable runnable,String name){
        Thread thread = new Thread(runnable, name);
        thread.start();
    }

    /**
     *  给线程指定默认的线程组并运行
     * @param runnable
     */
    public  static void runDefaultThread(Runnable runnable){
        ThreadGroupContatiner instance = ThreadGroupContatiner.getInstance();
        ThreadFactory factory = instance.defaultFactory();
        factory.newThread(runnable).start();
    }

    public static SimpleThreadFactory createFactory(ThreadGroup group,String prefix){
        return new SimpleThreadFactory(group,prefix);
    }
}
