package com.wk.concurrent.container;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentQueue {
    public static void main(String[] args) {
        //concurrentLinkedQueueDemo();
        concurrentLinkedDeque();
    }

    private static void concurrentLinkedDeque(){
        ConcurrentLinkedDeque<String> deque = new ConcurrentLinkedDeque<>();

        deque.add("123");

        deque.addFirst("qwe");

        String s = deque.pollFirst();

    }

    private static void concurrentLinkedQueueDemo() {
        Queue<String> strs = new ConcurrentLinkedQueue<>();
        strs.offer("aaa");
        strs.offer("aaa22");
        strs.poll();
        strs.poll();
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
