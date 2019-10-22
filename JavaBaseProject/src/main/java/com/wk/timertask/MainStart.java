package com.wk.timertask;

import java.util.Timer;

public class MainStart {
    public static void main(String[] args) {
        Timer timer = new Timer("myThread");
        MyTask myTask = new MyTask();
        timer.scheduleAtFixedRate(myTask,100,100);   // delay and period in ms
    }
}
