package com.wk.dp.creative.factorymethod;

/**
 * @author: ekiawna
 * @Date: 2021/3/31 17:41
 * @Description
 */
public class RedCarFactory implements ObjectFactory<RedCar> {
    @Override
    public RedCar createObject() {
        return new RedCar();
    }
}
