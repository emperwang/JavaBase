package phaser.thread.demo3;

import java.util.concurrent.Phaser;

public class StarterMain {
    public static void main(String[] args) {
        Phaser phaser = new Phaser(1);

        ThreadA a = new ThreadA(phaser);
        a.start();
    }
}
