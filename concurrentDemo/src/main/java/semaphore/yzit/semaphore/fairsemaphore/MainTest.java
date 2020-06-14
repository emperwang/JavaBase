package semaphore.yzit.semaphore.fairsemaphore;

public class MainTest {
	public static void main(String[] args) {
		FairSemaphoreTest fairSemaphoreTest = new FairSemaphoreTest();
		ThreadA a1 = new ThreadA(fairSemaphoreTest);
		a1.start();
		
		ThreadA[] a = new ThreadA[10];
		for(int i=0;i<=9;i++){
			a[i] = new ThreadA(fairSemaphoreTest);
			a[i].start();
		}
	}
}
