package semaphore.yzit.semaphore.manyToOne;

public class MainTest {
	public static void main(String[] args) {
		ManyToOneTest manyToOneTest = new ManyToOneTest();
		
		ThreadA[] a1 = new ThreadA[10];
		for(int i=0;i<10;i++){
			a1[i] = new ThreadA(manyToOneTest);
			a1[i].start();
		}
	}
}
