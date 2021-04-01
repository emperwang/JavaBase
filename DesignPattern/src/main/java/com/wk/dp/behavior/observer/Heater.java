package com.wk.dp.behavior.observer;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 15:55
 * @Description
 */
// 热水器
public class Heater extends Observerable{
    private Integer temperature=0;

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
        notifyObservor();
    }

    public Integer getTemperature() {
        return temperature;
    }
}
