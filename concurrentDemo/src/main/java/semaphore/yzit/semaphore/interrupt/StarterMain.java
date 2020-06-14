package semaphore.yzit.semaphore.interrupt;

public class StarterMain {
	public static void main(String[] args) throws InterruptedException {
		ServerDemo serverDemo = new ServerDemo();
		ThreadA a = new ThreadA(serverDemo);
		a.setName("a");
		a.start();		
		ThreadB b =new ThreadB(serverDemo);
		b.setName("b");
		b.start();
		Thread.sleep(1000);
		b.interrupt();
		
	}
}
