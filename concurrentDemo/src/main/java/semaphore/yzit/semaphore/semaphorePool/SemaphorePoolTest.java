package semaphore.yzit.semaphore.semaphorePool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

//�������ͬʱ����   ���Ǵ�ӡ����ʱԭ���Եģ�һ����ִ�����
public class SemaphorePoolTest {
	private Integer poolMaxSize = 3;
	private Integer semaphorePermits = 5;
	private List<String> list = new ArrayList<>();
	private Semaphore concurrencySemaphore = new Semaphore(semaphorePermits);
	private ReentrantLock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();

	public SemaphorePoolTest() {
		for(int i =0;i<poolMaxSize;i++){
			list.add("pool"+(i+1));
		}
	}
	
	public String getString(){
		String getValue = null;
		try {
			concurrencySemaphore.acquire();
			lock.lock();
			while(list.size()==0){
				condition.await();
			}
			getValue = list.remove(0);
			lock.unlock();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return getValue;
	}
	
	public void put(String value){
		try {
			Thread.sleep(1000*5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lock.lock();
		list.add(value);
		condition.signalAll();
		lock.unlock();
		concurrencySemaphore.release();
	}
}
