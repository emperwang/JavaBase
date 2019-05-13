package com.wk.java.util.concurrent.ThreadPool;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
//workStealing是守护线程池

/**
 * workStealing线程池，相当于每个线程维护一个队列，当任意一个线程执行完队列中的任务后，就会取
 * 其他任务的队列中拿过来任务进行执行
 */
public class WorkStealingPool {
    public static void main(String[] args) throws IOException {
        ExecutorService service = Executors.newWorkStealingPool();
        System.out.println(Runtime.getRuntime().availableProcessors());
        service.execute(new R(1));
        service.execute(new R(3));
        service.execute(new R(3));
        service.execute(new R(3));
        service.execute(new R(3));
        service.execute(new R(3));
        service.execute(new R(3));
        //主线程必须阻塞，不然看不到输出
        System.in.read();
    }

    static class R implements Runnable{
        int time;

        R(int num){
            this.time = num;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(time + ":"+ Thread.currentThread().getName());
        }
    }
}
