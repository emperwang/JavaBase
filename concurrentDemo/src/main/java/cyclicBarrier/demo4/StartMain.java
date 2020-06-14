package cyclicBarrier.demo4;

import java.util.concurrent.CyclicBarrier;

public class StartMain {
	public static void main(String[] args) {
		CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
		Service service = new Service(cyclicBarrier);
		
		ThreadA a1 = new ThreadA(service);
		a1.setName("a1");
		a1.start();
		
		ThreadA a2 = new ThreadA(service);
		a2.setName("a2");
		a2.start();
		
		ThreadA a3 = new ThreadA(service);
		a3.setName("a3");
		a3.start();
		
		ThreadA a4 = new ThreadA(service);
		a4.setName("a4");
		a4.start();
		
	}
}
