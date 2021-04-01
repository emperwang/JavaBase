package com.wk.dp.behavior.observer;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 15:57
 * @Description
 */
public class DrinkObservor extends Observor{
    @Override
    void updateState(Observerable observerable) {
        System.out.println("drink Observor process");
        if (observerable instanceof Heater){
            Integer temperature = ((Heater) observerable).getTemperature();
            if (temperature >= 95){
                System.out.println("drink water..");
            }
        }
    }
}
