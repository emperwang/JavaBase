package countdown.awaiTime;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Service {
	public CountDownLatch countDownLatch = new CountDownLatch(1);
	
	public void testMethod(){
		
		try {
			System.out.println(Thread.currentThread().getName()+"×¼±¸"+System.currentTimeMillis());
			countDownLatch.await(3, TimeUnit.SECONDS);
			System.out.println(Thread.currentThread().getName()+"½áÊø"+System.currentTimeMillis());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
