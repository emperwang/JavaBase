package semaphore.yzit.semaphore.tyracquire;

import java.util.concurrent.Semaphore;

public class TryAcquireTest {
	private Semaphore semaphore = new Semaphore(1);
	
	public void testMethod(){
		try {
			//��ȡ���� ��ô�ͻ᷵��true   ��ȡ�����ͷ���false
			if(semaphore.tryAcquire()){
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
			semaphore.release();
		}
		
	}
}
