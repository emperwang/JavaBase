package com.wk.java.util.concurrent.container;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWirteList {
    public static void main(String[] args) {
        List<String> list =
                new ArrayList<>();            // 耗时 80  但是报错
                //new Vector<>();                 // 耗时 70
                //new CopyOnWriteArrayList<>();  // 耗时3158

        Random random = new Random();
        Thread[] threads = new Thread[100];
        for (int i = 0; i < threads.length; i++) {
            Runnable task = new Runnable(){
                @Override
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        list.add("a"+random.nextInt(1000));
                    }
                }
            };
            threads[i] = new Thread(task);
        }
        runAndComputeTime(threads);
        System.out.println("list's size is:"+list.size());
    }

    static void runAndComputeTime(Thread[] ths){
        long start = System.currentTimeMillis();
       /* List<Thread> threads = Arrays.asList(ths);
        for (Thread thss :threads) {
            thss.start();
        }
        for (Thread tt: threads) {
            try {
                tt.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

        Arrays.asList(ths).forEach(t->t.start());
        Arrays.asList(ths).forEach(t->{
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.currentTimeMillis();
        System.out.println(end-start);
    }
}
