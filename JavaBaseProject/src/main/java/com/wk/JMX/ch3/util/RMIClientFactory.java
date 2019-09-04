package com.wk.JMX.ch3.util;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class RMIClientFactory {

    public static MBeanServerConnection getConnection(){
        JMXServiceURL jmxServiceURL = null;
        JMXConnector connector = null;
        MBeanServerConnection serverConnection = null;
        try {
             jmxServiceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:2099/server");
             connector =  JMXConnectorFactory.connect(jmxServiceURL);
             serverConnection = connector.getMBeanServerConnection();
        } catch (Exception e) {
            ExceptionUtil.printException(e);
        }
        return serverConnection;
    }
}
