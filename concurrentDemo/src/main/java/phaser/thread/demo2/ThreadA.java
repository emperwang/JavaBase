package phaser.thread.demo2;

import java.util.concurrent.Phaser;

public class ThreadA extends Thread {
    private Phaser phaser;

    public ThreadA(Phaser phaser){
        this.phaser = phaser;
    }

    public void run(){
        PrintTools.methodA();
    }
}
