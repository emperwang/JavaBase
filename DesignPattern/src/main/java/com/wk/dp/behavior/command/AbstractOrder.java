package com.wk.dp.behavior.command;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:53
 * @Description
 */
public class AbstractOrder implements Order {
    protected Chief chief;

    protected AbstractOrder(){
        this.chief = new Chief();
    }

    @Override
    public void processOrder() {

    }
}
