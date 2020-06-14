package semaphore.yzit.semaphore.fairsemaphore;

import java.util.concurrent.Semaphore;

public class FairSemaphoreTest {
	private boolean fair = true;
	private Semaphore semaphore = new Semaphore(1,fair);
	
	public void testMethod(){
		try {
			semaphore.acquire();
			System.out.println(Thread.currentThread().getName()+
					"start time"+System.currentTimeMillis());
			Thread.sleep(1000);
			System.out.println(Thread.currentThread().getName()+
					"end time"+System.currentTimeMillis());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			semaphore.release();
		}
		
	}
}
