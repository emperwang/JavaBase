package countdown.demo1;

public class StartMain {
	public static void main(String[] args) {
		Service service = new Service();
		ThreadA a1 = new ThreadA(service);
		a1.setName("a1");
		a1.start();
		
		System.out.println("main thread");
		service.downMethod();
	}
}
