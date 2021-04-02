package com.wk.dp.behavior.state;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:10
 * @Description
 */
public class LiquidState implements State {
    @Override
    public void showInfo() {
        System.out.println("这是水的液态.");
    }

    @Override
    public void behavior() {
        System.out.println("温度为1-99°, 此时呈现液态.");
    }
}
