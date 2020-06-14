package cyclicBarrier.demo2;

import java.util.concurrent.CyclicBarrier;

public class StartMain {
	public static void main(String[] args) throws InterruptedException {
		ThreadA[]  a = new ThreadA[6];
		CyclicBarrier cyclicBarrier = new CyclicBarrier(2, new Runnable() {
			
			public void run() {
				System.out.println("ȫ����");
			}
		});
		
		for(int i=0;i<6;i++){
			a[i] = new ThreadA(cyclicBarrier);
			a[i].start();
			Thread.sleep(2000);
		}
	}
}
