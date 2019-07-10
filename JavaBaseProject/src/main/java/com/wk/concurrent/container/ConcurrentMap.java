package com.wk.concurrent.container;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class ConcurrentMap {
    public static void main(String[] args) {
        Map<String ,String> map = new ConcurrentHashMap<>(); // 408
        //Map<String ,String> map = new ConcurrentSkipListMap<>();  // 361
        //Map<String ,String> map = new Hashtable<>();  //420
        //Map<String ,String> map = new HashMap<>();    //260

        Random random = new Random();
        Thread[] threads = new Thread[100];
        CountDownLatch countDownLatch = new CountDownLatch(threads.length);
        long start = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(()->{
                for (int j = 0; j < 10000; j++) {
                    map.put("a"+random.nextInt(100000),"b"+random.nextInt(200000));
                }
                countDownLatch.countDown();
            });
        }
        //Arrays.asList(threads).forEach(t->t.start());
        List<Thread> threads1 = Arrays.asList(threads);
        for (Thread t : threads1) {
            t.start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

}
