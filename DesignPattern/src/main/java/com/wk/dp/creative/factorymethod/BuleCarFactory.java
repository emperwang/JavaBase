package com.wk.dp.creative.factorymethod;

/**
 * @author: ekiawna
 * @Date: 2021/3/31 17:41
 * @Description
 */
public class BuleCarFactory implements ObjectFactory<BuleCar> {
    @Override
    public BuleCar createObject() {
        return new BuleCar();
    }
}
