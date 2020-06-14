package semaphore.yzit.semaphore.manyToOne;

import java.util.concurrent.Semaphore;

//�������ͬʱ����   �ֲ��ͬʱ�������
public class ManyToOneTest {
	private Semaphore semaphore = new Semaphore(3);
	
	public void testMethod(){
		try {
			//��ָ��ʱ����ȥ��ȡ����ź��� ��ô�ͻ᷵��true   ��ȡ�����ͷ���false
			semaphore.acquire();
			System.out.println(Thread.currentThread().getName()+
					"��ȡ���ź���");
			System.out.println(Thread.currentThread().getName()+
					"begin");
			for(int i=0;i<3;i++){
				System.out.println(Thread.currentThread().getName()+
						"say hello");
			}
			semaphore.release();
			System.out.println(Thread.currentThread().getName()+"end...");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
		}
		
	}
}
