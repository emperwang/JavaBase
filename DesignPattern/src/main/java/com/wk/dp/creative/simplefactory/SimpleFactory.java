package com.wk.dp.creative.simplefactory;

/**
 * @author: ekiawna
 * @Date: 2021/3/31 17:08
 * @Description
 */
public class SimpleFactory {
    // 简单工厂类, 用于创建 redCar
    public RedCar buildCar(){
        return new RedCar();
    }
}
