package com.wk.dp.behavior.state;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:16
 * @Description
 */
public class Main {
    public static void main(String[] args) {
        Water water = new Water();
        water.setTemperature(50);
        water.behavior();

        water.setTemperature(120);
        water.behavior();

        water.setTemperature(-1);
        water.behavior();
    }
}
