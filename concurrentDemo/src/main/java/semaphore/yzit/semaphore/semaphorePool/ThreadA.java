package semaphore.yzit.semaphore.semaphorePool;

public class ThreadA extends Thread{
	private SemaphorePoolTest semaphorePoolTest;
	
	public ThreadA(SemaphorePoolTest semaphorePoolTest) {
		this.semaphorePoolTest = semaphorePoolTest;
	}
	
	@Override
	public void run() {
		for(int i=0;i<Integer.MAX_VALUE;i++){
			String getValue = semaphorePoolTest.getString();
			System.out.println(Thread.currentThread().getName()+"ȡ��ֵ:"+getValue);
			semaphorePoolTest.put(getValue);
		}
	}
	
}
