package countdown.demo2;

import java.util.concurrent.CountDownLatch;

public class ThreadA extends Thread{
	private CountDownLatch countDownLatch;
	
	public ThreadA(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}
	
	@Override
	public void run() {
		try {
			System.out.println(Thread.currentThread().getName()+"running");
			Thread.sleep(1000);
			countDownLatch.countDown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
