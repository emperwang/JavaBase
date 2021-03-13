package com.wk.thread;

import lombok.SneakyThrows;

/**
 * @author: Sparks
 * @Date: 2021/3/13 17:45
 * @Description
 */
public class PrintABCIsAlive {

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
                System.out.println("B");
            }
        });

        Thread c = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("C");
            }
        });
        int i= 1;
        while (i>0){
            a.run();
            while (a.isAlive()){}
            b.run();
            while (b.isAlive()){}
            c.run();
            while (c.isAlive()){}
            i--;
        }
    }
}
