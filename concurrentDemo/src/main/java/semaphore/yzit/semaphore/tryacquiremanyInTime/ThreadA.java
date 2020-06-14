package semaphore.yzit.semaphore.tryacquiremanyInTime;

public class ThreadA extends Thread{

	private TryAcquireTest tryAcquireTest;
	public ThreadA(TryAcquireTest tryAcquireTest) {
		this.tryAcquireTest = tryAcquireTest;
	}
	
	@Override
	public void run() {
		tryAcquireTest.testMethod();
	}
}
