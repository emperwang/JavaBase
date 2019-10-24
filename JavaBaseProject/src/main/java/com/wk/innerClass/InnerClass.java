package com.wk.innerClass;

/**
 * 1. 内部类可以写的
 *   1）内部类可以使用  public  private protected  default修饰
 *   2）内部类可以定义 匿名代码块
 *   3）内部类可以定义 static final 常量
 *
 * 2. 内部类不可以使用
 *   1）不可以定义静态代码块
 *   2）不可以定义 静态变量
 *   3）不可以定义静态方法
 * 注意: 成员内部类中不能有静态属性和方法但可以有静态常量,(即用staic final修饰的属性),因为在类加载时会初始化static修饰的内容,
 * 而成员内部类必须依赖于内部类对象,即成员内部类要在外部类加载之后加载,所以不能包含静态的变量和方法. 使用final修饰的静态变量
 * 在类加载时会自动初始化其值,将其作为一个常量,所以可以出现在内部类中
 * 3. 实例化
 *   1）在外部类
 *          内部类  name = this.new Inner();
 *   2) 非外部类
 *          外部类名.内部类名  name = new 外部类名().new   内部类名()
 * 4. 外部类访问内部类
 *     外部类通过实例化内部类对象来访问内部类
 * 5. 内部类访问外部类
 *    内部类可以访问外部类的所有方法和属性
 *
 *
 * 注意: 如果成员内部类B和外部类A包含同名的成员,那么在类B中,this.v表示类B的成员
 *    A.this.v表示类A的成员
 *
 *
 *
 */
public class InnerClass {

    private  int a;
    private static int b;

    private static void say(){}
    public void test(){
        // 实例化内部类
        Inner inner = this.new Inner();
        inner.d = 1;
        inner.walk();
    }

    public class Inner{
        // **********     可以定义static  final类型的变量 ，但是不可以定义static类型的变量
        //public static int m = 1; // error
        public static  final  int m = 1;
        int d;

        {}
        //*********   不可以定义静态代码块 和 静态方法
        // static {}    // error
        // public static void go(){}    // error

        public void walk(){
            // 直接调用外部类的 属性和 方法
            // 内部类可以访问外部类的所有属性和方法   此内部类类似于一个属性
            a = 1;
            b = 1;
            say();
            test();
        }

    }
}
