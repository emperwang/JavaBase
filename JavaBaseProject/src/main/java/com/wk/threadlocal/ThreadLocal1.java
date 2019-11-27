package com.wk.threadlocal;

public class ThreadLocal1 {
    public static ThreadLocal<String> local1 = new ThreadLocal<>();
    public static ThreadLocal<String> local2 = new ThreadLocal<>();
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++){
            Thread thread = new Thread(new TestThread());
            thread.setName("name-" + i);
            thread.start();
        }

        Thread.sleep(Long.MAX_VALUE);
    }


    static class TestThread implements Runnable{
        @Override
        public void run() {
            try{
                sleepSec(5);   // wait
                // 虽然此处的threadLocal是全局变量，但是这里的使用，就会把此值放入到当前线程中
                // 这跟其 具体的实现是紧密相关的
                ThreadLocal1.local1.set(Thread.currentThread().getName() +""+System.currentTimeMillis());
                ThreadLocal1.local2.set(Thread.currentThread().getName());
                printLocalInfo(1);
                sleepSec(5);
                printLocalInfo(2);
                sleepSec(2);
                printLocalInfo(3);
            }catch (Exception e){

            }
        }

        private void printLocalInfo(int i) {
            System.out.println("获取线程的threadLocal 值, 第 " + i +"  times");
            System.out.println(ThreadLocal1.local1.get());
            System.out.println(ThreadLocal1.local2.get());
            System.out.println("------------------------------------");
        }

        public void sleepSec(int seconds){
            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
