package com.wk.jvm.classloader;

/**
 *  System.out.println(child.str);
 *  调用类的静态变量，会初始化此类
 *
 *  System.out.println(child.fvar);
 *  调用 final 静态变量，此变量会拷贝到调用此变量的函数的类的常量池中，故此操作并不会引起类
 *  child的初始化。
 *
 */
public class TestLoader2 {
    public static void main(String[] args) {
        System.out.println(child.str);

        System.out.println(child.fvar);
    }
}


class child{
    public static String str="hello";

    public static final String fvar="world";

    static {
        System.out.println("child static block");
    }
}