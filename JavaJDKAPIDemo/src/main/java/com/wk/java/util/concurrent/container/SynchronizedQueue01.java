package com.wk.java.util.concurrent.container;

import java.util.concurrent.SynchronousQueue;

public class SynchronizedQueue01 {
    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<String> queue = new SynchronousQueue<>();
  /*      new Thread(()->{
            try {
                System.out.println(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();*/

        queue.put("eee");  //阻塞等待消费者消费
        System.out.println(queue.size());   //长度一直为0
    }
}
