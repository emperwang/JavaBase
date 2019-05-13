package com.wk.java.util.concurrent.container;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class LinkedBlockingQueue01 {
    public static void main(String[] args) {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        Random random = new Random();
        new Thread(()->{
           for (int i=0;i<100;i++){
               try {
                   queue.put("a"+i);  //如果满了就会等待
                   TimeUnit.SECONDS.sleep(1);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        }).start();

        for (int i=0;i<5;i++){
            new Thread(()->{
                for (;;){
                    //如果空了，获取不到数据，就会等待
                    try {
                        System.out.println(Thread.currentThread().getName()+",  take-"+queue.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            },"c"+i).start();
        }
    }
}
