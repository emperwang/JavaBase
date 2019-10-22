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

    public static void main(String[] args) {
        startMain startMain = new startMain();
        // startMain.testNoProcessInterrupt();
        startMain.stopThreadWhenInterrupt();
    }
}
