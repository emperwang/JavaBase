package cyclicBarrier.demo2;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ThreadA extends Thread{
	private CyclicBarrier cyclicBarrier;
	
	public ThreadA(CyclicBarrier cyclicBarrier) {
		this.cyclicBarrier = cyclicBarrier;
	}
	
	@Override
	public void run() {
		try {
			System.out.println(Thread.currentThread()+getName()+" begin:"+System.currentTimeMillis()+
					"�ȴ���������������");
			cyclicBarrier.await();
			System.out.println(Thread.currentThread().getName()+" end"+System.currentTimeMillis()+
					"�Ѿ�����������������");
		} catch (InterruptedException | BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
