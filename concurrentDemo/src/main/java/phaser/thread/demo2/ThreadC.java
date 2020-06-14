package phaser.thread.demo2;

import java.util.concurrent.Phaser;

public class ThreadC extends Thread {
    private Phaser phaser;

    public ThreadC(Phaser phaser){
        this.phaser = phaser;
    }

    public void run(){
        try {
            PrintTools.methodB();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
