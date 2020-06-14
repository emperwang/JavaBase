package cyclicBarrier.demo5;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Service {
	private CyclicBarrier cyclicBarrier;
	
	public Service(CyclicBarrier cyclicBarrier) {
		this.cyclicBarrier = cyclicBarrier;
	}
	
	public void beginRun(int count){
		try {
			System.out.println(Thread.currentThread().getName()+" ���� �ڵȴ������˶�����");
			
			if(Thread.currentThread().getName().equals("Thread-2")){
				System.out.println("Thread-2 ����");
				Thread.sleep(5000);
				//Integer.parseInt("a");
				Thread.currentThread().interrupt();
			}
			cyclicBarrier.await();
			System.out.println("�����ˣ���ʼ��");
			System.out.println(Thread.currentThread().getName()+"�����յ㣬��������"+count+"����");
			
		} catch (InterruptedException e) {
			System.out.println("������InterruptedException "+cyclicBarrier.isBroken());
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			System.out.println("������BrokenBarrierException"+cyclicBarrier.isBroken());
			e.printStackTrace();
		}
	}
	
	public void testA(){
		for(int i=0;i<1;i++){
			beginRun(i);
		}
	}
}
