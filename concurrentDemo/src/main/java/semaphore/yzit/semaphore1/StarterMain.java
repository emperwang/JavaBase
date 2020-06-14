package semaphore.yzit.semaphore1;

public class StarterMain {
	public static void main(String[] args) {
		ServerDemo serverDemo = new ServerDemo();
		ThreadA a = new ThreadA(serverDemo);
		a.setName("a");
		
		ThreadB b = new ThreadB(serverDemo);
		b.setName("b");
		
		ThreadC c = new ThreadC(serverDemo);
		c.setName("c");
		
		a.start();
		b.start();
		c.start();
	}
}
