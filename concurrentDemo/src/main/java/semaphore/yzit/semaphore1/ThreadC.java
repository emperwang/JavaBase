package semaphore.yzit.semaphore1;

public class ThreadC extends Thread{
	
	private ServerDemo serverDemo;
	
	public ThreadC(ServerDemo serverDemo) {
		// TODO Auto-generated constructor stub
		super();
		this.serverDemo = serverDemo;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		serverDemo.testMethod();
	}

}
