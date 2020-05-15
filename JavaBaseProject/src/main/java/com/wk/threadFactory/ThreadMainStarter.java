package com.wk.threadFactory;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;

@Slf4j
public class ThreadMainStarter {

    public static void main(String[] args) {
        // 创建
        Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                log.error("thread :{}, uncaughtException:{}",t.getName(),e.getMessage());
            }
        };
        // 创建线程工厂
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("MineThread-%d")
                .setUncaughtExceptionHandler(handler).build();

        // 创建线程
        Thread thread = factory.newThread(new Runnable() {
            @Override
            public void run() {
                log.info("this is first thread name={}, groupname={}",Thread.currentThread().getName(),
                        Thread.currentThread().getThreadGroup().getName());
            }
        });

        thread.start();
    }
}
