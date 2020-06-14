package phaser.thread.demo4;

import java.util.concurrent.Phaser;

public class StarterMain {
    public static void main(String[] args) {
        Phaser phaser = new Phaser(2){
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.println("protected boolean onAdvance 被调用");
                return false;
                //return true;
                //返回true, 不等待了,phaser呈无效/销毁状态
                //返回false  则phaser继续工作
            }
        };

        MyService myService = new MyService(phaser);
        ThreadA threadA = new ThreadA(myService);
        threadA.setName("A");
        threadA.start();

        ThreadB threadB = new ThreadB(myService);
        threadB.setName("B");
        threadB.start();
    }
}
