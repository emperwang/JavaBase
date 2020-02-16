package com.wk.jvm.classloader;

import java.util.UUID;

/**
 * 1. 一个类的初始化 并不会先初始化其实现的接口
 * 2. 一个接口的初始化，并不会先初始化其继承的接口类
 * 3.
 */
public class testLoad3 {
    public static void main(String[] args) {
        // 此调用会把 child3的变量放到  testLoad3的常量池中，并不会导致child3及其父类的初始化
        // System.out.println(child3.vv);
        // System.out.println(parent3.ss);
        System.out.println(child4.cc4);
    }
}


/********************************************************/
interface parent{
    public static final String ss = "ss-world";
}


interface child3 extends parent{
    public static final String vv = "child3";
}

/********************************************************/
// ss属于运行时才能确定的变量，故调用此ss变量，会导致parent3类的初始化
interface parent3{
    public static final String ss = UUID.randomUUID().toString();
    public static final Thread tt = new Thread(){
        {
            System.out.println("init parent3");
        }
    };
}

/**
 *  调用此类的cc4时会初始化child4，但是child4的初始化并没有先初始化parent3
 */
interface child4 extends parent3{
    public static final String cc4 = UUID.randomUUID().toString();
    public static final Thread tt = new Thread(){
        {
            System.out.println("init child4");
        }
    };
}

/********************************************************/