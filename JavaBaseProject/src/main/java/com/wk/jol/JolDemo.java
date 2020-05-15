package com.wk.jol;

import org.openjdk.jol.info.ClassLayout;

public class JolDemo {

    public static void main(String[] args) {
        Object o = new Object();
        // 打印对象的内存布局
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        // 给对象加个锁,然后再打印对象的内存布局
        synchronized (o){
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }
}
