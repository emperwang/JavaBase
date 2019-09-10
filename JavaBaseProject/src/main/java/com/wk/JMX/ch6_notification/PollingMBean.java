package com.wk.JMX.ch6_notification;

public interface PollingMBean {

    void start();

    void stop();

    void setInterval(long time);
}
