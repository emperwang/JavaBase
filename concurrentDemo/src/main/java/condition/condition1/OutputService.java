package condition.condition1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class OutputService {

    private ReentrantLock lock = new ReentrantLock();
    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();

    public void awaitA() throws InterruptedException {
        lock.lock();
        System.out.println(getThreadName()+" awaitA");
        long begin = System.currentTimeMillis();
        conditionA.await();
        long end = System.currentTimeMillis();
        System.out.println(getThreadName()+" wait time:"+ (end - begin));
        lock.unlock();
    }

    public void awakA(){
        lock.lock();
        System.out.println("begin awake A");
        conditionA.signalAll();
        lock.unlock();
    }

    public void awaitB() throws InterruptedException {
        lock.lock();
        System.out.println(getThreadName()+" awaitB");
        long begin = System.currentTimeMillis();
        conditionB.await();
        long end = System.currentTimeMillis();
        System.out.println(getThreadName()+" wait time:"+ (end - begin));
        lock.unlock();
    }

    public void awakB(){
        lock.lock();
        System.out.println("begin awake A");
        conditionB.signalAll();
        lock.unlock();
    }


    public String getThreadName(){
        return Thread.currentThread().getName();
    }
}
