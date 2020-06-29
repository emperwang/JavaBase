package condition.condition1;

import java.util.concurrent.TimeUnit;

public class StarterMain {
    public static void main(String[] args) throws InterruptedException {
        OutputService service = new OutputService();
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(service.getThreadName()+" begin.");
                    service.awaitA();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadA.setName("threadA");

        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(service.getThreadName()+" begin");
                    service.awaitB();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadB.setName("threadB");
        threadA.start();
        threadB.start();

        System.out.println("main sleep");
        TimeUnit.SECONDS.sleep(10);
        System.out.println("wake up thread");
        service.awakA();
        service.awakB();
    }
}
