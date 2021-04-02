package com.wk.dp.behavior.command;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:49
 * @Description
 */
public class Client {

    private Waitor waitor;
    public Client(){
        this.waitor = new Waitor();
    }

    public void order(Order order){
        waitor.recvOrder(order);
    }

}
