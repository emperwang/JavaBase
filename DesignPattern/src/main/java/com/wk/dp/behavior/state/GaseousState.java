package com.wk.dp.behavior.state;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:10
 * @Description
 */
public class GaseousState implements State{
    @Override
    public void showInfo() {
        System.out.println("这是水的气态状态.");
    }

    @Override
    public void behavior() {
        System.out.println("温度超过100°, 飞起来喽.");
    }
}
