package semaphore.yzit.semaphore2;

public class ThreadA extends Thread{
	
	private ServerDemo serverDemo;
	
	public ThreadA(ServerDemo serverDemo) {
		super();
		this.serverDemo = serverDemo;
		
	}
	
	@Override
	public void run() {
		serverDemo.testMethod();
	}
}
