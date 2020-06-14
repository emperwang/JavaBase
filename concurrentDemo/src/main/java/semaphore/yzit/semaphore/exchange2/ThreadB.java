package semaphore.yzit.semaphore.exchange2;

import java.util.concurrent.Exchanger;

public class ThreadB extends Thread{
	private Exchanger<String> exchanger;
	
	public ThreadB(Exchanger<String> exchanger) {
		this.exchanger = exchanger;
	}
	
	@Override
	public void run() {
		try {
			System.out.println(Thread.currentThread().getName()+"��ȡ����"+exchanger.exchange("�й���B"));
			System.out.println(Thread.currentThread().getName()+" end...");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
