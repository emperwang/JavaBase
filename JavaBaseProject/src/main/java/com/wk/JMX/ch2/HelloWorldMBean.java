package com.wk.JMX.ch2;

/**
 *  MB接口
 */
public interface HelloWorldMBean {
    public void setGreeting(String greeting);

    public String getGreeting();

    public void printGreeting();
}
