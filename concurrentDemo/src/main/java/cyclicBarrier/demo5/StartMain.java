package cyclicBarrier.demo5;

import java.util.concurrent.CyclicBarrier;

public class StartMain {
	public static void main(String[] args) {
		CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new Runnable() {
			
			@Override
			public void run() {
				System.out.println("������!!! ");
			}
		});
		
		Service service = new Service(cyclicBarrier);
		ThreadA[] a1 = new ThreadA[4];
		for(int i=0;i<a1.length;i++){
			a1[i] = new ThreadA(service);
			a1[i].start();
		}
	}
}
