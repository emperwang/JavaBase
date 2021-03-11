package com.wk.thread;

/**
 * @author: ekiawna
 * @Date: 2021/3/11 13:04
 * @Description
 */
public class ExtendThread {

    static class PlusResource extends Thread{
        volatile int num = 10;
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                synchronized (PlusResource.class){
                    num++;
                    System.out.println(Thread.currentThread().getName()+" result: " + num);
                }
            }
        }
    }

    /*
    每个线程是独立的, 不能用来处理共同的资源
     */
    public static void main(String[] args) {
        Thread thread1 = new PlusResource();
        thread1.setName("thread 1-1..");
        Thread thread2 = new PlusResource();
        thread2.setName("thread 2-2..");
        Thread thread3 = new PlusResource();
        thread3.setName("thread 3-3..");
        thread1.start();
        thread2.start();
        thread3.start();
    }
}

