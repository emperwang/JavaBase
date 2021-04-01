package com.wk.dp.behavior.observer;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 15:57
 * @Description
 */
public class ShowerObservor extends Observor {

    @Override
    void updateState(Observerable observerable) {
        System.out.println("Shower observor process");
        if (observerable instanceof Heater){
            Integer temperature = ((Heater) observerable).getTemperature();
            if (temperature >40 && temperature <= 90){
                System.out.println("shower temperature......");
            }
        }
    }
}
