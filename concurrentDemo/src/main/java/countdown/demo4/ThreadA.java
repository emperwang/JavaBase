package countdown.demo4;

import java.util.concurrent.CountDownLatch;

public class ThreadA extends Thread{
	//�ȴ������˶�Ա����
		private CountDownLatch comingTag;
		//�ȴ�����˵׼����ʼ
		private CountDownLatch waitTag;
		//�ȴ�����
		private CountDownLatch waitRunTag;
		//����
		private CountDownLatch beginTag;
		//�����˶�Ա�����յ�
		private CountDownLatch endTag;
		
		public ThreadA(CountDownLatch comingTag,CountDownLatch waitTag,
				CountDownLatch waitRunTag,CountDownLatch beginTag,CountDownLatch endTag) {
			this.comingTag = comingTag;
			this.waitTag = waitTag;
			this.waitRunTag = waitRunTag;
			this.beginTag = beginTag;
			this.endTag = endTag;
		}
		
		@Override
		public void run() {
			try {
				System.out.println("�˶�Աʹ�ò�ͬ��ͨ���߲�ͬ�ٶȵ��������ߣ����������");
				Thread.sleep((int)Math.random()*10000);
				System.out.println(Thread.currentThread().getName());
				comingTag.countDown();
				System.out.println("�ȴ�����˵׼��");
				waitTag.await();
				System.out.println("���͸�λ��׼����������");
				Thread.sleep((int)Math.random()*10000);
				waitRunTag.countDown();
				beginTag.await();
				System.out.println(Thread.currentThread().getName()+
						"�˶�Ա���ܣ������ʱ�䲻ȷ��");
				Thread.sleep((int)Math.random()*10000);
				endTag.countDown();
				System.out.println(Thread.currentThread().getName()+"�˶�Ա�����յ�");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
}

