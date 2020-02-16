package com.wk.jvm.classloader;

public class testLoad4 {
    public static void main(String[] args) {
        System.out.println(child5.cc5);
    }
}

interface parent4{
    public static final String pp = "parent str";
    public static final Thread tt = new Thread(){
        {
            System.out.println("init parent4");
        }
    };
}

/**
 *  调用cc5会知道类child5的初始化，但child5的初始化并不会先初始化parent4
 */
class child5 implements parent4{
    public static String cc5 = "child5";
    {
        System.out.println("init child5");
    }
}