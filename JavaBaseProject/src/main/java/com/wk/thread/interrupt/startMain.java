package com.wk.thread.interrupt;

public class startMain {

    public void testNoProcessInterrupt(){
        NoProcessInterrupt noProcessInterrupt = new NoProcessInterrupt();
        noProcessInterrupt.setName("noProcessInterrupt");
        noProcessInterrupt.start();
        noProcessInterrupt.interrupt();
    }

    public void stopThreadWhenInterrupt(){
        Processinterrupt processinterrupt = new Processinterrupt();
        processinterrupt.setName("processInterrupt");
        processinterrupt.start();
        processinterrupt.sleepSecond(2);
        processinterrupt.interrupt();
    }

    /**
     *  结果: 可以看到已经唤醒了，但是进入InterruptedExceptionsleep 发现已经没有了中断标志
     *   before into sleep....
         running........
         before into sleep....
         into  InterruptedExceptionsleep interrupted
         running........
         before into sleep....
     */
    public void eraseInterruptFlag(){
        EraseThreadInterrupt eraseThreadInterrupt = new EraseThreadInterrupt();
        eraseThreadInterrupt.setName("eraseThread");
        eraseThreadInterrupt.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        eraseThreadInterrupt.interrupt();
    }

    public static void main(String[] args) {
        startMain startMain = new startMain();
        // startMain.testNoProcessInterrupt();
       // startMain.stopThreadWhenInterrupt();
        startMain.eraseInterruptFlag();
    }
}
