package semaphore.yzit.semaphore.drainPermits;

import java.util.concurrent.Semaphore;

public class DrainPermitsTest {
	private Semaphore semaphore = new Semaphore(10);
	
	public void testMethod(){
		try {
			semaphore.acquire();
			System.out.println(semaphore.availablePermits());
			System.out.println(semaphore.drainPermits()+" drain1  "+semaphore.availablePermits());
			System.out.println(semaphore.drainPermits()+" drain2  "+semaphore.availablePermits());
			System.out.println(semaphore.drainPermits()+" drain3  "+semaphore.availablePermits());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			semaphore.release();
		}
	}
}
