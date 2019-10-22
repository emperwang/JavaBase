package com.wk.thread.interrupt;

/**
 *  可以检测到中断，只是没有进行相关的处理
 *  可知：java中的interrupt只是一种标识，并不是一种需要强制进行处理的
 */
public class NoProcessInterrupt extends Thread {

    @Override
    public void run() {
        for (int i =0 ;i < Long.MAX_VALUE;i++){
            System.out.println("i = "+i);
            if (Thread.currentThread().isInterrupted()){
                System.out.println("interrupt");
            }
        }
    }
}
