package phaser.thread.demo1;

import java.util.concurrent.Phaser;

public class StarterMain {

    public static void main(String[] args) {
        Phaser phaser = new Phaser(3);
        ArriveAndAwaitDemo.phaser = phaser;

        ThreadA threadA = new ThreadA(phaser);
        threadA.setName("A--");
        threadA.start();

        ThreadB threadB = new ThreadB(phaser);
        threadB.setName("B--");
        threadB.start();

        ThreadC threadC = new ThreadC(phaser);
        threadC.setName("C--");
        threadC.start();
    }
}
