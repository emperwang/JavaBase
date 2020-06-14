package cyclicBarrier.demo8;

import java.util.concurrent.CyclicBarrier;

public class Service {
	public CyclicBarrier cyclicBarrier = new CyclicBarrier(3,new Runnable() {
		
		@Override
		public void run() {
			System.out.println("");
		}
	});
	
	public void testMethod(){
		try {
			System.out.println();
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
