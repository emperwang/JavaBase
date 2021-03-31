package com.wk.dp.creative.factorymethod;

/**
 * @author: ekiawna
 * @Date: 2021/3/31 17:10
 * @Description
 */
public interface ObjectFactory<T> {
    /*
       定义工厂方法的接口类, 针对不同的对象,创建不同的工厂实现类
       通过对不同的对象定义不同的工厂类, 当需要创建的对象比较多时,容易造成类爆炸
       这也导致了后面出现抽象工厂模式,针对某一类对象来提取出 一个公共的 抽象工厂
     */
    T createObject();
}
