package countdown.demo3;

import java.util.concurrent.CountDownLatch;

public class Service {
	private CountDownLatch countDownLatch = new CountDownLatch(1);
	
	public void testMethod(){
		try {
			System.out.println(Thread.currentThread().getName()+"׼��");
			countDownLatch.await();
			System.out.println(Thread.currentThread().getName()+"����");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void downMethod(){
		System.out.println("��ʼ");
		countDownLatch.countDown();
	}
}
