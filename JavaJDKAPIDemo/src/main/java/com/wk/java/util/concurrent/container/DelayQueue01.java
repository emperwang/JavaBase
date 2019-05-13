package com.wk.java.util.concurrent.container;

import jdk.management.resource.internal.inst.DatagramDispatcherRMHooks;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
//按照compareTo方法进行延迟
public class DelayQueue01 {
    static BlockingQueue<MyTask> queue = new DelayQueue<MyTask>();
    static Random random = new Random();

    static class MyTask implements Delayed{
        long runningTime;
        MyTask(long rt){
            this.runningTime = rt;
        }
        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(runningTime-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            if (this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS)){
                return -1;
            }
            else if (this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS)){
                return 1;
            }else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return " " + runningTime;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        MyTask myTask1 = new MyTask(start + 1000);
        MyTask myTask2 = new MyTask(start + 2000);
        MyTask myTask3 = new MyTask(start + 1500);
        MyTask myTask4 = new MyTask(start + 2500);
        MyTask myTask5 = new MyTask(start + 500);
        queue.put(myTask1);
        queue.put(myTask2);
        queue.put(myTask3);
        queue.put(myTask4);
        queue.put(myTask5);
        System.out.println(queue);
        for (int i=0;i<5;i++){
            System.out.println(queue.take());
        }
    }
}
