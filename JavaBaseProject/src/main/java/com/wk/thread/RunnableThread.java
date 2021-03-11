package com.wk.thread;

import com.wk.thread.sleep.SleepUtil;

/**
 * @author: ekiawna
 * @Date: 2021/3/11 13:04
 * @Description
 */
public class RunnableThread {
    static class PlusResource implements Runnable{
        volatile int num = 10;
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                synchronized (ExtendThread.PlusResource.class){
                    num++;
                    System.out.println(Thread.currentThread().getName()+" result: " + num);
                    SleepUtil.sleepSeconds(1);
                }
            }
        }
    }

    /*
     可以看到使用 runnable接口,可以使用多线程来 共同处理一些资源
     */
    public static void main(String[] args) {
        ExtendThread.PlusResource resource = new ExtendThread.PlusResource();
        Thread thread1 = new Thread(resource);
        thread1.setName("thread 1-1");
        Thread thread2 = new Thread(resource);
        thread2.setName("thread 2-2");
        Thread thread3 = new Thread(resource);
        thread3.setName("thread 3-3");
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
