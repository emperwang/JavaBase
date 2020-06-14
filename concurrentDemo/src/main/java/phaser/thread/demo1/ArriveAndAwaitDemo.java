package phaser.thread.demo1;

import java.util.concurrent.Phaser;

public class ArriveAndAwaitDemo {
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
        phaser.arriveAndAwaitAdvance();
        System.out.println(Thread.currentThread().getName()+"methodB-A1 end="+System.currentTimeMillis());

        System.out.println(Thread.currentThread().getName()+"methodB-A2 begin="+System.currentTimeMillis());
        Thread.sleep(5000);
        phaser.arriveAndAwaitAdvance();
        System.out.println(Thread.currentThread().getName()+"methodB-A2 end="+System.currentTimeMillis());
    }
}
