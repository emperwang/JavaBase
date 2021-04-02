package com.wk.dp.behavior.strategy;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 9:39
 * @Description
 */
public class WalkStrategy implements Strategy {
    @Override
    public void outStyle() {
        System.out.println("walk to dest.");
    }
}
