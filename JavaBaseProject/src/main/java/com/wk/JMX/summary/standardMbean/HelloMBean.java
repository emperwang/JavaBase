package com.wk.JMX.summary.standardMbean;

public interface HelloMBean {

    String getName();

    void setName(String name);

    void helloWorld();

    void helloWorld(String name);

    String getTelephone();
}
