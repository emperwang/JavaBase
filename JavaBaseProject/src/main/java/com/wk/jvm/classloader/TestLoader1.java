package com.wk.jvm.classloader;

/**
 *  System.out.println(MyChild.str);
 *  此调用并不会触发MyChild的初始化，因为str是父类的静态变量，所以仅仅会初始化父类，
 *  子类会加载但不会初始化
 *
 *  System.out.println(MyChild.cstr);
 *  此操作就会导致父类和子类都会进行初始化。 因为调用子类的静态变量，会初始化子类，
 *  而子类初始化之前会先初始化父类
 *
 *  ## 虚拟机参数
 *  -XX:+TraceClassLoading   打印加载的类
 *  -XX:-TraceClassLoading    关闭打印操作
 */
public class TestLoader1 {
    public static void main(String[] args) {
        System.out.println(MyChild.str);
        System.out.println(MyChild.cstr);
    }
}


class MyParent1{
    public static String str="hello world";

    static {
        System.out.println("MyParent static block");
    }
}

class MyChild extends MyParent1{
    public static String cstr="hello child";
    static {
        System.out.println("MyChild static block");
    }
}