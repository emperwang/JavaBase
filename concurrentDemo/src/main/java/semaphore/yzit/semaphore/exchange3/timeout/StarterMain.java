package semaphore.yzit.semaphore.exchange3.timeout;

import java.util.concurrent.Exchanger;

//�޶�ʱ���ڽ�������    ���û�н����ɹ��ͱ���
public class StarterMain {
	public static void main(String[] args) throws InterruptedException {
		Exchanger<String> exchanger = new Exchanger<>();
		ThreadA a1 = new ThreadA(exchanger);
		a1.setName("a1");
		a1.start();
		
		
		System.out.println("main end");
	}
}
