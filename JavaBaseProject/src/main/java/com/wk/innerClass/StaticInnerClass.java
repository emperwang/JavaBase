package com.wk.innerClass;

/**
 * 1.静态内部类可以写
 *      1) 静态属性
 *      2）非静态属性
 *      3）静态方法
 *      4）非静态方法
 *      5）静态代码块
 *      6）匿名代码块
 *注意：不能在静态内部类写抽象方法
 * 2.静态内部类访问外部类
 *      1）可以直接访问外部类的静态属性和方法
 *      2）实例化外部对象类访问外部类的非静态属性和方法 以及  静态的属性和方法
 *
 * 3.外部类访问静态内部类
 *      1）直接调用内部类的静态属性和方法
 *      2）实例化内部类来访问内部类的非静态方法和属性 以及  静态属性和方法
 *      3） 通过" 外部类.内部类.属性(方法)" 此方式直接访问静态方法和属性
 *
 * 4.实例化静态内部类
 *      1) 在非外部类: 外部类名.内部类名  name =  new 外部类名.内部类名();
 *      2) 在外部类:   内部类名  name = new  内部类名
 *
 *
 *
 */
public class StaticInnerClass {
    // 定义一个实例变量  和  静态变量
    private int a;
    private static int b;

    // 定义一个静态方法 和 实例方法
    public static void say(){}
    public void test(){
        // 直接可以调用静态内部类的静态方法 和 静态变量
        StaticInnerClass.Inner.go();
        Inner.c = 1;

        // 实例化 静态内部变量
        Inner inner = new Inner();
        inner.d = 1;
        inner.walk();
    }
    // 静态内部类
    public static class Inner{
        // 在静态内部类中定义一个静态变量和一个实例变量
        static int c;
        int d;

        // 定义一个匿名代码块  和  静态代码块
        {}
        static {}

        // 定义一个静态方法 和  普通方法
        public static void go(){}
        public void walk(){
            // 外部类的 静态函数和 变量
            StaticInnerClass.say();
            StaticInnerClass.b = 2;
            int f = b;
            say();
            // 实例化 外部类 并调用 其实例化变量 以及 普通方法
            StaticInnerClass outer = new StaticInnerClass();
            int atmp = outer.a;
            outer.test();
        }

    }
}
