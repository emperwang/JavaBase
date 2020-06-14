package phaser.thread.demo2;

import java.util.concurrent.Phaser;

public class PrintTools {
    public static Phaser phaser;

    public static void methodA(){
        System.out.println(Thread.currentThread().getName()+"methodA-A1 begin="+System.currentTimeMillis());
        phaser.arriveAndAwaitAdvance();
        System.out.println(Thread.currentThread().getName()+"methodA-A1 end="+System.currentTimeMillis());

        System.out.println(Thread.currentThread().getName()+"methodA-A2 begin="+System.currentTimeMillis());
        phaser.arriveAndAwaitAdvance();
        System.out.println(Thread.currentThread().getName()+"methodA-A2 end="+System.currentTimeMillis());
    }

    public static void methodB() throws InterruptedException {
        System.out.println(Thread.currentThread().getName()+"methodB-A1 begin="+System.currentTimeMillis());
        Thread.sleep(5000);
        System.out.println("A: "+phaser.getRegisteredParties());
        phaser.arriveAndDeregister();
        System.out.println("B: "+phaser.getRegisteredParties());
        System.out.println(Thread.currentThread().getName()+"methodB-A1 end="+System.currentTimeMillis());
    }
}

