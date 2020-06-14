package phaser.thread.demo4;

import java.util.concurrent.Phaser;

public class MyService {
    private Phaser phaser;

    public MyService(Phaser phaser){
        this.phaser = phaser;
    }

    public void testMethod() throws InterruptedException {
        System.out.println("A begin ThreadName="+Thread.currentThread().getName()+
        System.currentTimeMillis());
        if(Thread.currentThread().getName().equals("B")){
            Thread.sleep(5000);
        }

        phaser.arriveAndAwaitAdvance();
        System.out.println("A end ThreadName="+Thread.currentThread().getName()+"" +
                "end phaser value="+phaser.getPhase()+
        " "+System.currentTimeMillis());
        System.out.println("B begin ThreadName = "+ Thread.currentThread().getName()+" "
        +System.currentTimeMillis());
        if(Thread.currentThread().getName().equals("B")){
            Thread.sleep(5000);
        }
        phaser.arriveAndAwaitAdvance();
        System.out.println("B end ThreadName="+Thread.currentThread().getName()
        +"end phaser="+phaser.getPhase()+" "+System.currentTimeMillis());

        System.out.println("C begin ThreadName = "+ Thread.currentThread().getName()+" "
                +System.currentTimeMillis());
        if(Thread.currentThread().getName().equals("B")){
            Thread.sleep(5000);
        }
        phaser.arriveAndAwaitAdvance();
        System.out.println("C end ThreadName="+Thread.currentThread().getName()
                +"end phaser="+phaser.getPhase()+" "+System.currentTimeMillis());

    }
}
