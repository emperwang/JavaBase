package com.wk.dp.behavior.state;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:10
 * @Description
 */
public class SolidState implements State {
    @Override
    public void showInfo() {
        System.out.println("这是水的固态.");
    }

    @Override
    public void behavior() {
        System.out.println("温度低于0°, 此时水呈固态.");
    }
}
