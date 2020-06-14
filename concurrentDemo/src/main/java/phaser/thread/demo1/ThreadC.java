package phaser.thread.demo1;

import java.util.concurrent.Phaser;

public class ThreadC extends Thread {
    private Phaser phaser;

    public ThreadC(Phaser phaser){
        this.phaser = phaser;
    }

    public void run(){
        try {
            ArriveAndAwaitDemo.methodB();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
