package countdown.awaiTime;

public class StartMain {
	public static void main(String[] args) {
		ThreadA[] a = new ThreadA[3];
		
		Service service = new Service();
		for(int i=0;i<a.length;i++){
			a[i] = new ThreadA(service);
		}
		
		for(int i=0;i<a.length;i++){
			a[i].start();
		}
	}
}
