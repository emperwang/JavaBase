package semaphore.yzit.semaphore.exchange2;

import java.util.concurrent.Exchanger;

//��������
public class StarterMain {
	public static void main(String[] args) throws InterruptedException {
		Exchanger<String> exchanger = new Exchanger<>();
		ThreadA a1 = new ThreadA(exchanger);
		a1.setName("a1");
		a1.start();
		
		ThreadB b1 = new ThreadB(exchanger);
		b1.setName("b1");
		b1.start();
		
		System.out.println("main end");
	}
}
