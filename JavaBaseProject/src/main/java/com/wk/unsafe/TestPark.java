package com.wk.unsafe;

import sun.misc.Unsafe;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author: ekiawna
 * @Date: 2021/3/17 16:21
 * @Description
 */
public class TestPark {

    private static Thread mainThread;

    public void testPark() throws NoSuchFieldException, IllegalAccessException {
        Unsafe unSafe = UnSafeUtil.getUnSafe();
        this.mainThread = Thread.currentThread();
        System.out.println(String.format("park %s", mainThread.getName()));
        /*
    // 终止挂起的线程，恢复正常.java.util.concurrent包中挂起操作都是在LockSupport类实现的，其底层正是使用这两个方法
    public native void unpark(Object thread);
    // 线程调用该方法，线程将一直阻塞直到超时，或者是中断条件出现。
    public native void park(boolean isAbsolute, long time);
         */
        System.out.println("before park: " + LocalDateTime.now());
        unSafe.park(false, TimeUnit.SECONDS.toNanos(3));
        System.out.println("after park: " + LocalDateTime.now());
        new Thread(() -> {
            System.out.println(String.format("%s unpark %s", Thread.currentThread().getName(), mainThread.getName()));
            unSafe.unpark(mainThread);
        }).start();
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        TestPark testPark = new TestPark();
        testPark.testPark();
    }
}
