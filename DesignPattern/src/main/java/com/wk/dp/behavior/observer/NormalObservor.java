package com.wk.dp.behavior.observer;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 15:58
 * @Description
 */
public class NormalObservor extends Observor {

    @Override
    void updateState(Observerable observerable) {
        System.out.println("normal Observor process...");
        if (observerable instanceof Heater){
            Integer temperature = ((Heater) observerable).getTemperature();
            if (-1 <= temperature && temperature <= 40){
                System.out.println("normal water..");
            }
        }
    }
}
