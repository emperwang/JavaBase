package com.wk.thread.sleep;

import java.util.concurrent.TimeUnit;

/**
 * @author: ekiawna
 * @Date: 2021/3/11 12:48
 * @Description
 */
public class SleepUtil {

    public static void sleepSeconds(int sec){
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepMills(int ms){
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void sleepHour(int hour){
        try {
            TimeUnit.HOURS.sleep(hour);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepMinutes(int min){
        try {
            TimeUnit.MINUTES.sleep(min);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
