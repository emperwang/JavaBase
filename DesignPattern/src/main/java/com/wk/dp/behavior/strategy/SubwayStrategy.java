package com.wk.dp.behavior.strategy;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 9:39
 * @Description
 */
public class SubwayStrategy implements Strategy {
    @Override
    public void outStyle() {
        System.out.println("take subway to dest.");
    }
}
