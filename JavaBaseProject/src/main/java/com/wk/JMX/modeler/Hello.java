package com.wk.JMX.modeler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hello implements HelloMBean {
    private static Logger log = LoggerFactory.getLogger(Hello.class);

    private String name = "init Name";
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void printHello() {
        log.info("printHello to field :"+name);
    }

    @Override
    public void printHello(String name) {
        log.info("printHello to param :"+name);
    }
}
