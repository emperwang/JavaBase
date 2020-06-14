package semaphore.yzit.semaphore.queuelength;

public class ThreadA extends Thread{
	private getQueueLengthTest queueLengthTest;
	
	public ThreadA(getQueueLengthTest queueLengthTest) {
		this.queueLengthTest = queueLengthTest;
	}
	
	@Override
	public void run() {
		queueLengthTest.testMethod();
	}
}
