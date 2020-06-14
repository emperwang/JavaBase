package semaphore.yzit.semaphore2;

public class StarterMain {
	public static void main(String[] args) {
		ServerDemo serverDemo = new ServerDemo();
		ThreadA[] a = new ThreadA[10];
		for(int i=0;i<10;i++){
			a[i] = new ThreadA(serverDemo);
			a[i].start();
		}
		
	}
}
