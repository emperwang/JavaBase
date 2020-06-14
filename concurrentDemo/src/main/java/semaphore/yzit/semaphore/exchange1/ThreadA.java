package semaphore.yzit.semaphore.exchange1;

import java.util.concurrent.Exchanger;

public class ThreadA extends Thread{
	private Exchanger<String> exchanger;
	
	public ThreadA(Exchanger<String> exchanger) {
		this.exchanger = exchanger;
	}
	
	@Override
	public void run() {
		try {
			//ȥ��������   ���û�л�ȡ������  ��ô�ͻ��������
			System.out.println(Thread.currentThread().getName()+"�õ�������"+exchanger.exchange("�߳�A"));
			System.out.println(Thread.currentThread().getName()+" end...");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
