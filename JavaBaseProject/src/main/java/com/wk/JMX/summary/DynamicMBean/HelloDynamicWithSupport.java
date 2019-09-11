package com.wk.JMX.summary.DynamicMBean;

import com.wk.JMX.ch5_Dynamic.DynamicMBeanSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanOperationInfo;

public class HelloDynamicWithSupport extends DynamicMBeanSupport {
    private static Logger log = LoggerFactory.getLogger(HelloDynamicWithSupport.class);

    private String name = "HelloDynamicTest";

    public HelloDynamicWithSupport(){
        addMBeanConstructor(this.getClass().getConstructors()[0],"this is HelloMBeanSupport constructor");
        addMBeanAttribute("Name",String.class.getName(),true,true,false,"this is name attribute");
        addMBeanOperation("printHello",null,null,null,
                "this is print operation","void", MBeanOperationInfo.INFO);
    }

    public void printHello(){
        log.info("this is printHello operation ,say hello to "+name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
