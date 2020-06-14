package phaser.thread.demo7;

import java.util.concurrent.Phaser;

public class ThreadA extends Thread{

    private Phaser phaser;

    public ThreadA(Phaser phaser){
        this.phaser = phaser;
    }

    public void run(){
        System.out.println(Thread.currentThread().getName()+"A1 begin;"+
        System.currentTimeMillis());
        phaser.arriveAndAwaitAdvance();
        System.out.println(Thread.currentThread().getName()+"A1 end;"+
                System.currentTimeMillis());
    }
}
