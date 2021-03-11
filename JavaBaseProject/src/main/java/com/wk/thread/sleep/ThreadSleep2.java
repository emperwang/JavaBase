package com.wk.thread.sleep;

/**
 * @author: ekiawna
 * @Date: 2021/3/11 12:59
 * @Description
 */
public class ThreadSleep2 {
    static class RunA extends Thread {
        @Override
        public void run() {
            while (true){
                System.out.println(Thread.currentThread().getName()+" runnning ....");
                SleepUtil.sleepSeconds(5);
            }
        }
    }

    static class RunB extends Thread{
        @Override
        public void run() {
            Thread thread = new RunA();
            thread.setName("thread-AA");
            thread.start();
            while (true){
                System.out.println(Thread.currentThread().getName()+" runnning ....");
                int i=0;
                while (i<Integer.MAX_VALUE){
                    i++;
                    SleepUtil.sleepMills(1);
                }
                SleepUtil.sleepMinutes(5);
            }
        }
    }


    public static void main(String[] args) {
        Thread thread = new RunB();
        thread.setName("thread-BB");
        thread.start();
    }
}
