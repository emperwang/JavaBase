package semaphore.yzit.semaphore.oneToOne;

public class ThreadA extends Thread{

	private ManyToOneTest manyToOneTest;
	public ThreadA(ManyToOneTest manyToOneTest) {
		this.manyToOneTest = manyToOneTest;
	}
	
	@Override
	public void run() {
		manyToOneTest.testMethod();
	}
}
