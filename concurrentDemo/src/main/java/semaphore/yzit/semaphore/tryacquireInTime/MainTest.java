package semaphore.yzit.semaphore.tryacquireInTime;

public class MainTest {
	public static void main(String[] args) {
		TryAcquireTest tryAcquireTest = new TryAcquireTest();
		ThreadA a1 = new ThreadA(tryAcquireTest);
		a1.setName("a1");
		a1.start();
		
		ThreadB b1 = new ThreadB(tryAcquireTest);
		b1.setName("b1");
		b1.start();
	}
}
