package com.wk.thread;

/**
 * @author: Sparks
 * @Date: 2021/3/13 17:32
 * @Description
 */
public class PrintABCUsingWaitNotify {

    private int times;
    private int state = 0;
    private Object objectA = new Object();
    private Object objectB = new Object();
    private Object objectC = new Object();

    public PrintABCUsingWaitNotify(int times){
        this.times = times;
    }

    public void PrintA(){
        print("A",0, objectA, objectB);
    }
    public void PrintB(){
        print("B", 1, objectB, objectC);
    }
    public void PrintC(){
        print("C", 2, objectC, objectA);
    }

    public void print(String name, int targetState, Object cur, Object next){
        for (int i=0; i< times;){
            synchronized (cur){
                while (state % 3 != targetState){
                    try {
                        cur.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                i++;
                state++;
                System.out.println(name);
                synchronized (next){
                    next.notify();
                }
            }
        }
    }

    public static void main(String[] args) {
        PrintABCUsingWaitNotify usingWaitNotify = new PrintABCUsingWaitNotify(3);
        new Thread(new Runnable() {
            @Override
            public void run() {
                usingWaitNotify.PrintA();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                usingWaitNotify.PrintB();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                usingWaitNotify.PrintC();
            }
        }).start();
    }
}
