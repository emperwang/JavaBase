package com.wk.JMX.modeler;

public interface HelloMBean {

    String getName();

    void setName(String name);

    void printHello();

    void printHello(String name);
}
