package com.wk.java.util.concurrent.container;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentQueue {
    public static void main(String[] args) {
        Queue<String> strs = new ConcurrentLinkedQueue<>();
        for (int i=0; i<10;i++){
            strs.offer("a"+i);
        }
        System.out.println(strs);
        System.out.println(strs.size());
        System.out.println(strs.poll()); //取出元素，并从队列中删除
        System.out.println(strs.size());
        System.out.println(strs.peek());  //取出元素，但是不删除
        System.out.println(strs.size());
    }
}
