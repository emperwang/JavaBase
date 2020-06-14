package semaphore.yzit.semaphore.tryacquiremanyInTime;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TryAcquireTest {
	private Semaphore semaphore = new Semaphore(3);
	
	public void testMethod(){
		try {
			//��ָ��ʱ����ȥ��ȡ����ź��� ��ô�ͻ᷵��true   ��ȡ�����ͷ���false
			if(semaphore.tryAcquire(3,3,TimeUnit.SECONDS)){
				System.out.println(Thread.currentThread().getName()+
						"��ȡ���ź���");
				//ȥ�������ʱ�����������̶߳��ܻ�ȡ���ź���
				Thread.sleep(1000*5);
			}else{
				System.out.println(Thread.currentThread().getName()+
						"û�л�ȡ���ź���");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			semaphore.release(3);
		}
		
	}
}
