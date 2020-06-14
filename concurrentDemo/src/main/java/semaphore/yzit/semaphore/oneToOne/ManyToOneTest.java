package semaphore.yzit.semaphore.oneToOne;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

//�������ͬʱ����   ���Ǵ�ӡ����ʱԭ���Եģ�һ����ִ�����
public class ManyToOneTest {
	private Semaphore semaphore = new Semaphore(3);
	private ReentrantLock lock = new ReentrantLock();
	public void testMethod(){
		try {
			//��ָ��ʱ����ȥ��ȡ����ź��� ��ô�ͻ᷵��true   ��ȡ�����ͷ���false
			semaphore.acquire();
			lock.lock();
			System.out.println(Thread.currentThread().getName()+
					"��ȡ���ź���");
			System.out.println(Thread.currentThread().getName()+
					"begin");
			for(int i=0;i<3;i++){
				System.out.println(Thread.currentThread().getName()+
						"say hello");
			}
			lock.unlock();
			semaphore.release();
			System.out.println(Thread.currentThread().getName()+"end...");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
		}
		
	}
}
