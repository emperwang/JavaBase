package semaphore.yzit.semaphore.unfairsemaphore;

public class ThreadA extends Thread{

	private UnfairSemaphoreTest unfairSemaphoreTest;
	public ThreadA(UnfairSemaphoreTest unfairSemaphoreTest) {
		this.unfairSemaphoreTest = unfairSemaphoreTest;
	}
	
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+"������");
		unfairSemaphoreTest.testMethod();
	}
}
