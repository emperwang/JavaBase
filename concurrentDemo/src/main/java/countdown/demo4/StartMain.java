package countdown.demo4;

import java.util.concurrent.CountDownLatch;

public class StartMain {
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch comingTag = new CountDownLatch(10);
		CountDownLatch waitTag = new CountDownLatch(1);
		CountDownLatch waitRunTag = new CountDownLatch(10);
		CountDownLatch beginTag = new CountDownLatch(1);
		CountDownLatch endTag = new CountDownLatch(10);
	
		ThreadA[] a1 = new ThreadA[10];
		for(int i=0;i<a1.length;i++){
			a1[i] = new ThreadA(comingTag, waitTag, waitRunTag, beginTag, endTag);
			a1[i].start();
		}
		System.out.println("����Ա���ڵȴ�ѡ�ֵ���");
		comingTag.await();
		System.out.println("���п��������˶�Ա������Ѳ��5��");
		Thread.sleep(5000);
		waitTag.countDown();
		System.out.println("���͸�λ!");
		waitRunTag.await();
		Thread.sleep(2000);
		System.out.println("����ǹ��!!");
		beginTag.countDown();
		endTag.await();
		System.out.println("�����˶�Ա���ͳ�Ʊ�������");
	}
}	
