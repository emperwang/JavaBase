package semaphore.yzit.semaphore.semaphorePool;

public class MainTest {
	public static void main(String[] args) {
		SemaphorePoolTest semaphorePoolTest = new SemaphorePoolTest();
		ThreadA[] a1 = new ThreadA[5];
		for(int i=0;i<a1.length;i++){
			a1[i] = new ThreadA(semaphorePoolTest);
		}
		
		for(int i=0;i<a1.length;i++){
			a1[i].start();
		}
	}
}
