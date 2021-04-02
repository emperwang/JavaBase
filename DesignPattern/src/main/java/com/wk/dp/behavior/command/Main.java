package com.wk.dp.behavior.command;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:56
 * @Description
 */
public class Main {
    public static void main(String[] args) {
        Client client = new Client();

        client.order(new BraisedOrder());
        client.order(new SteamedOrder());
    }
}
