package cyclicBarrier.demo4;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Service {
	private CyclicBarrier cyclicBarrier;
	
	public Service(CyclicBarrier cyclicBarrier) {
		this.cyclicBarrier = cyclicBarrier;
	}
	
	public void beginRun(){
		try {
			long sleepValue = (int)(Math.random()*10000);
			Thread.sleep(sleepValue);
			System.out.println(Thread.currentThread().getName()+" "+
					System.currentTimeMillis()+" begin �ܵ�һ�׶�"+
					(cyclicBarrier.getNumberWaiting()+1));
			cyclicBarrier.await();
			System.out.println(Thread.currentThread().getName()+" "+
			System.currentTimeMillis()+" end �ܵ�һ�׶�"+" "+
					cyclicBarrier.getNumberWaiting());
			
			sleepValue = (int)(Math.random()*10000);
			Thread.sleep(sleepValue);
			System.out.println(Thread.currentThread().getName()+" "+
			System.currentTimeMillis()+" begin�ܵڶ��׶�"+
					cyclicBarrier.getNumberWaiting()+1);
			cyclicBarrier.await();
			System.out.println(Thread.currentThread().getName()+" "+
			System.currentTimeMillis()+" end�ܵڶ��׶�"+
					cyclicBarrier.getNumberWaiting());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
}
