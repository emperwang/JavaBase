package com.wk.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: Sparks
 * @Date: 2021/3/13 17:33
 * @Description
 */
public class PrintABCUsingCondition {
    private int times;
    private int state;

    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    public PrintABCUsingCondition(int times){
        this.times = times;
    }

    public void print(int target, Condition cur, Condition next){
        for (int i=0; i<times;){
            try {
                lock.lock();
                while (state % 3 != target){
                    cur.await();
                }
                System.out.println(Thread.currentThread().getName());
                state++;
                i++;
                next.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }

    public void printA(){
        print(0, c1, c2);
    }
    public void printB(){
        print(1, c2, c3);
    }
    public void printC(){
        print(2, c3, c1);
    }

    public static void main(String[] args) {
        PrintABCUsingCondition condition = new PrintABCUsingCondition(3);
        new Thread(new Runnable() {
            @Override
            public void run() {
                condition.printA();
            }
        },"A").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                condition.printB();
            }
        },"B").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                condition.printC();
            }
        },"C").start();
    }
}
