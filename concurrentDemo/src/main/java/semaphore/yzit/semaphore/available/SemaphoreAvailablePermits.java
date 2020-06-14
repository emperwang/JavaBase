package semaphore.yzit.semaphore.available;

import java.util.concurrent.Semaphore;

public class SemaphoreAvailablePermits {
	private Semaphore semaphore = new Semaphore(1);
	
	public void testMethod(){
		try {
			semaphore.acquire();
			System.out.println(semaphore.availablePermits());
			System.out.println(semaphore.availablePermits());
			System.out.println(semaphore.availablePermits());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			semaphore.release();
		}
	}
}
