package semaphore.yzit.semaphore1;

import java.util.concurrent.Semaphore;

public class ServerDemo {
	//���ֻ������һ���߳��ܵõ�����ź���
	//private Semaphore semphore = new Semaphore(1);
	
	//�������߳̿��Եõ�����ź���  Ȼ��ȥ��������
	private Semaphore semphore = new Semaphore(2);
	public void testMethod(){
		try {
			//��ȡ�ź���
			semphore.acquire();
			System.out.println(Thread.currentThread().getName()+
					"  begin time="+System.currentTimeMillis());
			Thread.sleep(500);
			System.out.println(Thread.currentThread().getName()+
					" end time ="+System.currentTimeMillis());
			//�ͷ��ź���
			semphore.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
