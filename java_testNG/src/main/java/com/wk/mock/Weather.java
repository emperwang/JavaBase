package com.wk.mock;

/**
 * @author: Sparks
 * @Date: 2021/5/23 9:23
 * @Description
 */
public class Weather {

    public String getWeather(){
        double random = Math.random();

        if (random < 0.25 ){
            return "晴";
        }else if (0.25 <= random && random < 0.5){
            return "多云";
        }else if (0.5 <= random && random < 0.75){
            return "阴";
        }else {
            return "雨";
        }
    }
}
