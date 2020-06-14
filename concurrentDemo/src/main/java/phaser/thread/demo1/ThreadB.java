package phaser.thread.demo1;

import java.util.concurrent.Phaser;

public class ThreadB extends Thread {
    private Phaser phaser;

    public ThreadB(Phaser phaser){
        this.phaser = phaser;
    }

    public void run(){
        ArriveAndAwaitDemo.methodA();
    }
}
