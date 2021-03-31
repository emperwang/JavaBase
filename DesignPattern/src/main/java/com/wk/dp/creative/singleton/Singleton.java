package com.wk.dp.creative.singleton;

/**
 * @author: ekiawna
 * @Date: 2021/3/31 17:08
 * @Description
 */
public class Singleton {
    // 饿汉式单例
    static class RedCar{
        private static final RedCar car = new RedCar();
        private RedCar(){}
        public static RedCar getCar(){
            return car;
        }
    }
    // 懒汉式
    static class RedCar2{
        private static RedCar2 car = null;
        private RedCar2(){}
        public static RedCar2 getCar(){
            if (car == null){
                car = new RedCar2();
            }
            return car;
        }
    }
    // 双重判定锁
    static class RedCar3{
        private static RedCar3 car = null;
        private RedCar3(){}
        public static RedCar3 getCar(){
            if (car == null){
                synchronized (RedCar3.class){
                    if (car == null) {
                        car = new RedCar3();
                    }
                }
            }
            return car;
        }
    }

    // 静态内部类 懒加载初始化,线程安全
    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }
    // 私有化 构造器
    private Singleton(){}
    public static final Singleton getInstance(){
        return SingletonHolder.INSTANCE;
    }
}
