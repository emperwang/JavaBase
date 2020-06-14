package cyclicBarrier.demo6;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Service {
	private CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
		
		@Override
		public void run() {
			System.out.println("���׽�����");
		}
	});
	
	
	public void testMethod(){
		
		try {
		System.out.println(Thread.currentThread().getName()+"׼��"+
				System.currentTimeMillis());
		if(Thread.currentThread().getName().equals("Thread-0")){
			System.out.println("Thread-0ִ����cyclicBarrier.await(timeout, unit)");
			cyclicBarrier.await(5,TimeUnit.SECONDS);
		}
		if(Thread.currentThread().getName().equals("Thread-1")){
			System.out.println("Thread-1ִ����cyclicBarrier.await()");
			cyclicBarrier.await();
		}
		System.out.println(Thread.currentThread().getName()+" ��ʼ"+System.currentTimeMillis());
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName()+"���� InterruptedException");
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			System.out.println(Thread.currentThread().getName()+"���� BrokenBarrierException");
			e.printStackTrace();
		} catch (TimeoutException e) {
			System.out.println(Thread.currentThread().getName()+"���� TimeoutException");
			e.printStackTrace();
		}
	}
}
