package cyclicBarrier.demo6;

public class StartMain {
	public static void main(String[] args) {
		Service service = new Service();
		
		ThreadA a = new ThreadA(service);
		a.start();
		
		ThreadB b = new ThreadB(service);
		b.start();
	}
}
