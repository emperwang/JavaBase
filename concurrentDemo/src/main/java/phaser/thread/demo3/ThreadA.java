package phaser.thread.demo3;

import java.util.concurrent.Phaser;

/**
 * arriveAndAwaitAdvance  设置屏障并等待
 * getPhase  查看到达第几个屏障
 */
public class ThreadA extends Thread {
    private Phaser phaser;

    public ThreadA(Phaser phaser){
        this.phaser = phaser;
    }

    @Override
    public void run() {
        System.out.println("A begin  -- ");
        phaser.arriveAndAwaitAdvance();

        System.out.println("A end phaser value = "+phaser.getPhase());

        System.out.println("B begin");
        phaser.arriveAndAwaitAdvance();
        System.out.println("B end phaser value = "+phaser.getPhase());

        System.out.println("C begin");
        phaser.arriveAndAwaitAdvance();
        System.out.println("C end phaser value = "+phaser.getPhase());

        System.out.println("D begin");
        phaser.arriveAndAwaitAdvance();
        System.out.println("D end phaser value = "+phaser.getPhase());
    }
}
