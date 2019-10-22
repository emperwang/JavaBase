package com.wk.thread.interrupt;

/**
 *  当线程处于 sleep join  wait等操作时，会抛出InterruptException异常，并把中断标志擦除
 */
public class EraseThreadInterrupt extends Thread{

    private volatile boolean runFlag = true;

    @Override
    public void run() {
        while (runFlag){
            System.out.println("running........");
            try {
                System.out.println("before into sleep....");
                Thread.sleep(500);
            } catch (InterruptedException e) { // InterruptedException
                System.out.println("into  InterruptedException"+e.getMessage());
                if (Thread.currentThread().isInterrupted()){
                    System.out.println("occur interrupt.......");
                }
            }

        }
    }
}
