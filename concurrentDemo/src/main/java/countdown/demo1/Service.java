package countdown.demo1;

import java.util.concurrent.CountDownLatch;

public class Service {
	private CountDownLatch countDownLatch = new CountDownLatch(1);
	
	public void testMethod(){
		try {
			System.out.println("AAAAAAAAA");
			//判断计数是否为0    如果为0  那么久继续执行   否则等待
			countDownLatch.await();
			System.out.println("BBBBBBBBB");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void downMethod(){
		try {
			Thread.sleep(1000*5);
			System.out.println("count down");
			countDownLatch.countDown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
