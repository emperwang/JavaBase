package com.wk.dp.behavior.strategy;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 9:40
 * @Description
 */
public class TaxiStrategy implements Strategy {
    @Override
    public void outStyle() {
        System.out.println("take a taxi.");
    }
}
