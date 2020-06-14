package cyclicBarrier.demo1;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ThreadA  extends Thread{
	private CyclicBarrier cyclicBarrier;
	
	public ThreadA(CyclicBarrier cyclicBarrier) {
		this.cyclicBarrier = cyclicBarrier;
	}

	@Override
	public void run() {
		
		try {
			Thread.sleep((int) (Math.random()*10000));
			System.out.println(Thread.currentThread().getName()+"到了"+System.currentTimeMillis());
			cyclicBarrier.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
	
	
}
