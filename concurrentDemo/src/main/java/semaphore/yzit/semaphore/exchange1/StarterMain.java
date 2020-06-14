package semaphore.yzit.semaphore.exchange1;

import java.util.concurrent.Exchanger;

//ͨ��������Կ�����û�л�ȡ������ʱ  �ͻ�����
public class StarterMain {
	public static void main(String[] args) throws InterruptedException {
		Exchanger<String> exchanger = new Exchanger<>();
		ThreadA a1 = new ThreadA(exchanger);
		a1.start();
		
		System.out.println("main end");
	}
}
