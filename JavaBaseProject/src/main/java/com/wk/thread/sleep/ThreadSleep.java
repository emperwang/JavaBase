package com.wk.thread.sleep;

/**
 * @author: ekiawna
 * @Date: 2021/3/11 12:48
 * @Description
 */
public class ThreadSleep {
    static class RunA implements Runnable {
        @Override
        public void run() {
            while (true){
                System.out.println(Thread.currentThread().getName()+" runnning ....");
                SleepUtil.sleepSeconds(5);
            }
        }
    }

    static class RunB implements Runnable{
        @Override
        public void run() {
            Thread thread = new Thread(new RunA());
            thread.setName("thread-A");
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
        Thread thread = new Thread(new RunB());
        thread.setName("thread-B");
        thread.start();
    }
}
