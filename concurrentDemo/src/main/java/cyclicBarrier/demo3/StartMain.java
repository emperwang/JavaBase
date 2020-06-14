package cyclicBarrier.demo3;

import java.util.concurrent.CyclicBarrier;

public class StartMain {
	public static void main(String[] args) throws InterruptedException {
		
		CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
		
		ThreadA a1 = new ThreadA(cyclicBarrier);
		a1.start();
		Thread.sleep(500);
		System.out.println(cyclicBarrier.getNumberWaiting());
		System.out.println("--------------------");
		ThreadA a2 = new ThreadA(cyclicBarrier);
		a2.start();
		Thread.sleep(500);
		System.out.println("a2 "+cyclicBarrier.getNumberWaiting());
		System.out.println("--------------------");
		ThreadA a3 = new ThreadA(cyclicBarrier);
		a3.start();
		Thread.sleep(500);
		System.out.println(cyclicBarrier.getNumberWaiting());
		System.out.println("--------------------");
		ThreadA a4 = new ThreadA(cyclicBarrier);
		a4.start();
		Thread.sleep(500);
		System.out.println(cyclicBarrier.getNumberWaiting());
	}
}
