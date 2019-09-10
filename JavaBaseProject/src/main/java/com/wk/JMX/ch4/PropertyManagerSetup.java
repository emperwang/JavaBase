package com.wk.JMX.ch4;

import com.wk.JMX.ch3.util.ExceptionUtil;
import com.wk.JMX.ch3.util.RMIClientFactory;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class PropertyManagerSetup {

    public PropertyManagerSetup(){
        MBeanServerConnection connection = RMIClientFactory.getConnection();

        try {
            ObjectName propertyName = new ObjectName("JMXBookAgent:name=property");
            connection.createMBean("com.wk.JMX.ch4.PropertyManager",propertyName);
        } catch (Exception e) {
            ExceptionUtil.printException(e);
        }

    }
}
