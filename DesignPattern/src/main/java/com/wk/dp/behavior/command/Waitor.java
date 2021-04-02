package com.wk.dp.behavior.command;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:52
 * @Description
 */
public class Waitor {

    // 接收订单
    public void recvOrder(Order order){
        // 订单处理
        order.processOrder();
    }
}
