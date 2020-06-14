package phaser.thread.demo7;

import java.util.concurrent.Phaser;

public class MainStarter {
    public static void main(String[] args) throws InterruptedException {
        Phaser phaser = new Phaser(7);
        ThreadA[] threadAS = new ThreadA[5];

        for(int i=0;i<threadAS.length;i++){
            threadAS[i] = new ThreadA(phaser);
            threadAS[i].setName("Thread"+(i+1));
            threadAS[i].start();
        }

        Thread.sleep(2000);
        System.out.println("已到达:"+phaser.getArrivedParties());
        System.out.println("未到达:"+phaser.getUnarrivedParties());
    }
}
