package countdown.demo2;

import java.util.concurrent.CountDownLatch;

public class StartMain {
	public static void main(String[] args) {
		CountDownLatch countDownLatch = new CountDownLatch(10);
		ThreadA[] array = new ThreadA[Integer.parseInt(""+countDownLatch.getCount())];
		for(int i =0;i<array.length;i++){
			array[i] = new ThreadA(countDownLatch);
			array[i].setName("�߳�"+i);
			array[i].start();
		}
		
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("come back������");
	}
}
