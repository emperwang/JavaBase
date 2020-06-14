package semaphore.yzit.semaphore.tryacquireInTime;

public class ThreadB extends Thread{
	private TryAcquireTest tryAcquireTest;
	
	public ThreadB(TryAcquireTest tryAcquireTest) {
		this.tryAcquireTest = tryAcquireTest;
	}
	
	@Override
	public void run() {
		tryAcquireTest.testMethod();
	}
}
