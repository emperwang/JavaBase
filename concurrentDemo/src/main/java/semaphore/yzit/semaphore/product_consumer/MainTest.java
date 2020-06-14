package semaphore.yzit.semaphore.product_consumer;

public class MainTest {
	public static void main(String[] args) throws InterruptedException {
		RepastService service = new RepastService();
		ThreadA[] a1 = new ThreadA[50];
		ThreadB[] b1 = new ThreadB[50];
		
		for(int i=0;i<50;i++){
			a1[i] = new ThreadA(service);
			b1[i] = new ThreadB(service);
		}
		Thread.sleep(2000);
		for(int i=0;i<50;i++){
			a1[i].start();
			b1[i].start();
		}
	}
}
