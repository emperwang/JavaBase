package com.wk.dp.creative.factorymethod;

/**
 * @author: ekiawna
 * @Date: 2021/3/31 17:10
 * @Description
 */
public interface ObjectFactory<T> {
    /*
       定义工厂方法的接口类, 针对不同的对象,创建不同的工厂实现类
     */
    T createObject();
}
