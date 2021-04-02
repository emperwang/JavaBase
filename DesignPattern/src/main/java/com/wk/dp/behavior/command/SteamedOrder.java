package com.wk.dp.behavior.command;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:52
 * @Description
 */
public class SteamedOrder extends AbstractOrder {

    @Override
    public void processOrder() {
        this.chief.steamedFood();
    }
}
