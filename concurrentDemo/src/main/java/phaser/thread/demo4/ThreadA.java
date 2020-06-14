package phaser.thread.demo4;

public class ThreadA extends Thread {

    private MyService myService;
    public ThreadA(MyService myService){
        this.myService = myService;
    }

    public void run(){
        try {
            myService.testMethod();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
