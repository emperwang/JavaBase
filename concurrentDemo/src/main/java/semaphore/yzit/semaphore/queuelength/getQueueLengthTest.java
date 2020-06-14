package semaphore.yzit.semaphore.queuelength;

import java.util.concurrent.Semaphore;

public class getQueueLengthTest {
	private Semaphore semaphore = new Semaphore(1);
	
	public void testMethod(){
		try {
			semaphore.acquire();
			Thread.sleep(1000);
			System.out.println("���д�Լ "+semaphore.getQueueLength()+" ���߳��ڵȴ�");
			System.out.println("�Ƿ����ֳ��ڵȴ��ź���? "+semaphore.hasQueuedThreads());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			semaphore.release();
		}
		
	}
}
