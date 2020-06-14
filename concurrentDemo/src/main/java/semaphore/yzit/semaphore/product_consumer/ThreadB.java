package semaphore.yzit.semaphore.product_consumer;

//������
public class ThreadB extends Thread{
	
	private RepastService service;
	
	public ThreadB(RepastService service) {
		this.service = service;
	}
	
	@Override
	public void run() {
		service.get();
	}
}
