package semaphore.yzit.semaphore.uninterrupt;

public class ThreadB extends Thread{
	private ServerDemo serverDemo;
	
	public ThreadB(ServerDemo serverDemo) {
		this.serverDemo = serverDemo;
	}
	
	@Override
	public void run() {
		serverDemo.testMethod();
	}
}
