package com.wk.mock;

/**
 * @author: Sparks
 * @Date: 2021/5/23 9:25
 * @Description
 */
public class Work {

    private Weather weather;

    public int getWorkTime(){
        String weather = this.weather.getWeather();
        switch (weather){
            case "晴":
                return 6;
            case "多云":
                return 7;
            case "阴":
                return 8;
            default:
                return 9;
        }
    }
}
