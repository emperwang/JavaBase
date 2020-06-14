package semaphore.yzit.semaphore3;

import java.util.concurrent.Semaphore;

public class StarterMain {
	public static void main(String[] args) {
		
		Semaphore semaphore = new Semaphore(5);
		try {
			semaphore.acquire();
			semaphore.acquire();
			System.out.println(semaphore.availablePermits());
			semaphore.acquire();
			semaphore.acquire();
			semaphore.acquire();
			System.out.println(semaphore.availablePermits());
			semaphore.release();
			semaphore.release();
			semaphore.release();
			System.out.println(semaphore.availablePermits());
			semaphore.release();
			semaphore.release();
			System.out.println(semaphore.availablePermits());
			
			semaphore.release(4);
			System.out.println(semaphore.availablePermits());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
