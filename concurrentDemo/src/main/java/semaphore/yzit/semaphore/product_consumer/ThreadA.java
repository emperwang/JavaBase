package semaphore.yzit.semaphore.product_consumer;

//������
public class ThreadA extends Thread{
	private RepastService service;
	
	public ThreadA(RepastService service) {
		this.service = service;
	}
	
	@Override
	public void run() {
		service.set();
	}
}
