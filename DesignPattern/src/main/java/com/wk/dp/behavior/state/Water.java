package com.wk.dp.behavior.state;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:12
 * @Description
 */
public class Water {
    private State state;
    private int temperature = 10;
    public Water(){
        this.state = new LiquidState();
    }

    public void stateInfo(){
        state.showInfo();
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
        if (temperature < 0){ // 低于0°, 转换为液态
            state = new SolidState();
        }else if (temperature >0 && temperature <= 98){
            state = new LiquidState();
        }else {
            state = new GaseousState();
        }
    }

    public void behavior(){
        state.behavior();
    }
}
