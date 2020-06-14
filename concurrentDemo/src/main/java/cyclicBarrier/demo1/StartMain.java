package cyclicBarrier.demo1;

import java.util.concurrent.CyclicBarrier;

public class StartMain {
	public static void main(String[] args) {
		CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new Runnable() {
			@Override
			public void run() {
				System.out.println("ȫ������");
			}
		});
		
		ThreadA[] a = new ThreadA[5];
		for(int i=0;i<a.length;i++){
			a[i] = new ThreadA(cyclicBarrier);
		}
		for(int i=0;i<a.length;i++){
			a[i].start();
		}
	}
}
