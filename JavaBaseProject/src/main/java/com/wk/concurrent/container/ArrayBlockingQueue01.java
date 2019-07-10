package com.wk.concurrent.container;

import java.util.*;
import java.util.concurrent.*;

public class ArrayBlockingQueue01 {
    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            queue.put("a"+i);
        }
        System.out.println("put  "+queue);
        //queue.put("bb"); //满了就会等待，程序阻塞
        System.out.println("add  "+queue);
        //queue.add("ccc");   //满了，添加报错
        System.out.println("offer  "+queue);
        queue.offer("ddd"); //满了 添加不进入
        System.out.println(""+queue);
        queue.offer("eee",10, TimeUnit.SECONDS);
        System.out.println("offer timeout  "+queue);
    }
}

