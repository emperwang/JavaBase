package com.wk.thread;

/**
 *  此线程只运行一次
 */
public class RunThread extends Thread{

    @Override
    public void run() {
        System.out.println("this is thread run");
    }
}
