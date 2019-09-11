package com.wk.JMX.summary.standardMbean;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hello implements HelloMBean {
    private Logger log = LoggerFactory.getLogger(Hello.class);
    private String name;
    private String telephone = "15518881999";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void helloWorld() {
        log.info("hello ......" + name);
    }

    @Override
    public void helloWorld(String name) {
        log.info("say hello to ...."+name);
    }

    @Override
    public String getTelephone() {
        return telephone;
    }
}
