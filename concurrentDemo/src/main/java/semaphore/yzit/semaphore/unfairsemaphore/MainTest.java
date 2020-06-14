package semaphore.yzit.semaphore.unfairsemaphore;

public class MainTest {
	public static void main(String[] args) {
		UnfairSemaphoreTest unfairSemaphoreTest = new UnfairSemaphoreTest();
		ThreadA a1 = new ThreadA(unfairSemaphoreTest);
		a1.start();
		
		ThreadA[] a = new ThreadA[10];
		for(int i=0;i<=9;i++){
			a[i] = new ThreadA(unfairSemaphoreTest);
			a[i].start();
		}
	}
}
