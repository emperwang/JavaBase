package semaphore.yzit.semaphore.fairsemaphore;

public class ThreadA extends Thread{

	private FairSemaphoreTest fairSemaphoreTest;
	public ThreadA(FairSemaphoreTest fairSemaphoreTest) {
		this.fairSemaphoreTest = fairSemaphoreTest;
	}
	
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+"������");
		fairSemaphoreTest.testMethod();
	}
}
