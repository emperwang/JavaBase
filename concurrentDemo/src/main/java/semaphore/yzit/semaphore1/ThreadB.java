package semaphore.yzit.semaphore1;

public class ThreadB extends Thread{
	
	private ServerDemo serverDemo;
	
	public ThreadB(ServerDemo serverDemo) {
		super();
		this.serverDemo = serverDemo;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		serverDemo.testMethod();
	}
}
