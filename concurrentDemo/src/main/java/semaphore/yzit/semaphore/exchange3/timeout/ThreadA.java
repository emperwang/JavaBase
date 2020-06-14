package semaphore.yzit.semaphore.exchange3.timeout;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ThreadA extends Thread{
	private Exchanger<String> exchanger;
	
	public ThreadA(Exchanger<String> exchanger) {
		this.exchanger = exchanger;
	}
	
	@Override
	public void run() {
		try {
			//ָ��ʱ����ȥ��������   ���û�л�ȡ������  ��ô�ͻᱨ��
			System.out.println(Thread.currentThread().getName()+"�õ�������"+
			exchanger.exchange("�߳�A",5,TimeUnit.SECONDS));
			System.out.println(Thread.currentThread().getName()+" end...");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
