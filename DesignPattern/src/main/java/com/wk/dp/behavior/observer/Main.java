package com.wk.dp.behavior.observer;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 16:14
 * @Description
 */
public class Main {
    public static void main(String[] args) {
        Heater heater = new Heater();
        heater.addObservor(new DrinkObservor());
        heater.addObservor(new NormalObservor());
        heater.addObservor(new ShowerObservor());
        heater.setTemperature(50);
    }
}
