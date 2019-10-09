package com.wk.hookfunction;

public class ShutDownHook {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("process is running");

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("Process is down. now you can close the releated resource");
        }));

        Thread.sleep(Long.MAX_VALUE);
    }
}
