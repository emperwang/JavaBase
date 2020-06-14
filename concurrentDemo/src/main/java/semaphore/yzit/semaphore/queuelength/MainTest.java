package semaphore.yzit.semaphore.queuelength;

public class MainTest {
	public static void main(String[] args) {
		getQueueLengthTest service = new getQueueLengthTest();
		
		ThreadA[] a = new ThreadA[10];
		for(int i=0;i<=9;i++){
			a[i] = new ThreadA(service);
			a[i].start();
		}
	}
}
