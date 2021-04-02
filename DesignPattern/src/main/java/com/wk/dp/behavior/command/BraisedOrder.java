package com.wk.dp.behavior.command;

import com.sun.org.apache.xpath.internal.operations.Or;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:52
 * @Description
 */
// 红烧做法的订单
public class BraisedOrder extends AbstractOrder{

    @Override
    public void processOrder() {
        this.chief.braisedFood();
    }
}
