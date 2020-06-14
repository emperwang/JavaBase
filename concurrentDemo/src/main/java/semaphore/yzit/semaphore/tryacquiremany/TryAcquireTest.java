package semaphore.yzit.semaphore.tryacquiremany;

import java.util.concurrent.Semaphore;

public class TryAcquireTest {
	private Semaphore semaphore = new Semaphore(3);
	
	public void testMethod(){
		try {
			//��ȡ���� ��ô�ͻ᷵��true   ��ȡ�����ͷ���false
			if(semaphore.tryAcquire(3)){
				System.out.println(Thread.currentThread().getName()+
						"��ȡ���ź���");
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
