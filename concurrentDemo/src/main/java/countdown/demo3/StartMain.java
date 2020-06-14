package countdown.demo3;

public class StartMain {
	public static void main(String[] args) {
		Service service = new Service();
		
		ThreadA[] a1 = new ThreadA[10];
		for(int i=0;i<a1.length;i++){
			a1[i] = new ThreadA(service);
			a1[i].setName("phaser/thread" + i);
			a1[i].start();
		}
		try {
			Thread.sleep(2000);
			service.downMethod();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
