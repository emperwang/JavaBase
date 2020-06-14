package phaser.thread.demo4;

public class ThreadB extends Thread{

    private MyService myService;
    public ThreadB(MyService myService){
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
