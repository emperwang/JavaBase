package semaphore.yzit.semaphore2;

import java.util.concurrent.Semaphore;

public class ServerDemo {
	//���ֻ������һ���߳��ܵõ�����ź���
	//private Semaphore semphore = new Semaphore(1);
	
	//�������߳̿��Եõ�����ź���  Ȼ��ȥ��������
	private Semaphore semphore = new Semaphore(10);
	public void testMethod(){
		try {
			//��ȡ�ź���
			semphore.acquire(2);
			System.out.println(Thread.currentThread().getName()+
					"  begin time="+System.currentTimeMillis());
			Thread.sleep(1000*5);
			System.out.println(Thread.currentThread().getName()+
					" end time ="+System.currentTimeMillis());
			//�ͷ��ź���
			semphore.release(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
