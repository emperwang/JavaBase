package com.wk.concurrent.lock;

import java.util.concurrent.locks.StampedLock;

/**
 * @author: wk
 * @Date: 2020/11/25 13:57
 * @Description
 */
public class StampLockUse {
    private StampedLock lock = new StampedLock();
    private int x;
    private int y;
    public void readLock(){
        // 先尝试乐观读取,即没有上锁的操作
        long stamp = lock.tryOptimisticRead();
        // 获取临界值
        int curX = x;
        int curY = y;
        // 验证在读取期间没有进行过修改,则正在; 如果修改过,则获取锁重新读,悲观锁
        try {
            if (!lock.validate(stamp)) {
                stamp = lock.readLock();
                curX = x;
                curY = y;
            }
        }finally {
            lock.unlockRead(stamp);
        }

    }

    public void writeLock(){
        // 获取写锁
        final long stamp = lock.writeLock();
        // 临界操作
        int curX = x;
        int curY = y;
        // 释放写锁
        lock.unlockWrite(stamp);
    }

    public static void main(String[] args) {
        System.out.println(1 << 1);
        final String s = Integer.toBinaryString(1 << 7);
        System.out.println(s);
        System.out.println(Integer.toBinaryString((1 << 7)-1));
        System.out.println(Integer.toBinaryString((1 << 7)-1-1));
        System.out.println((1 << 7));
        System.out.println((1<<7)-1);
        System.out.println((1<<7)-1-1);
    }
}
