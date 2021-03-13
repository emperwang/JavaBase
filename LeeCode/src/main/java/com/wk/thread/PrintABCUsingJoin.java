package com.wk.thread;

import lombok.SneakyThrows;

/**
 * @author: Sparks
 * @Date: 2021/3/13 17:32
 * @Description
 */
public class PrintABCUsingJoin {

    public static void main(String[] args) {
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("A");
            }
        });

        Thread b = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    a.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("B");
            }
        });

        Thread c = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                b.join();
                System.out.println("C");
            }
        });
        a.start();
        b.start();
        c.start();
    }
}
