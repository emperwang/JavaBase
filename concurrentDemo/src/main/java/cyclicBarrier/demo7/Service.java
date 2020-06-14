package cyclicBarrier.demo7;

import java.util.concurrent.CyclicBarrier;

public class Service {
	public CyclicBarrier cyclicBarrier = new CyclicBarrier(3,new Runnable() {
			
			@Override
			public void run() {
				System.out.println("");
			}
		});
}
