package com.wk.threadlocal;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ThreadLocal2 {
    // 复写 初始值
    private ThreadLocal<Integer> tla = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue() {
            return 3;
        }
    };
    // 设置一个信号量
    Semaphore semaphore = new Semaphore(1);
    private Random rnd = new Random();

    public class Worker implements Runnable{

        @Override
        public void run() {
            try {
                Thread.sleep(rnd.nextInt(1000));
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Integer valA = tla.get();
            System.out.println(Thread.currentThread().getName()+" tlA initial value: "+ valA);
            valA = rnd.nextInt();
            tla.set(valA);      // 设置一个随机值
            System.out.println(Thread.currentThread().getName()+ "  tla initial value : "+ tla.get());

            semaphore.release();
            // 在线程池中一定要删除threadLocal，因为线程池中的线程对象是循环使用的
            tla.remove();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(3);
        ThreadLocal2 tld2 = new ThreadLocal2();
        service.execute(tld2.new Worker());
        service.execute(tld2.new Worker());
        service.execute(tld2.new Worker());

        service.awaitTermination(5, TimeUnit.HOURS);
        service.shutdown();

    }
}
