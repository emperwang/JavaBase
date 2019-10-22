package com.wk.thread.interrupt;

/**
 *  此线程中有具体的中断处理函数，当发生中断时，终止当前线程的执行
 */
public class Processinterrupt extends Thread {

    private volatile boolean runFlag = true;

    @Override
    public void run() {

        while (runFlag){
            System.out.println("running.............");
            if (Thread.currentThread().isInterrupted()){ // 当发生中断时，终止当前线程的执行
                System.out.println("into interrupt process....");
                this.runFlag = false;
            }
        }
    }

    public void sleepSecond(int seconds){
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
