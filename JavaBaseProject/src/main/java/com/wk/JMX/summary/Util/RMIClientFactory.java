package com.wk.JMX.summary.Util;

import com.wk.JMX.ch3.util.ExceptionUtil;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 *  获取到 rmi - server的一个连接
 */
public class RMIClientFactory {
    private static String Server_Url = "service:jmx:rmi:///jndi/rmi://localhost:2099/server";
    public static MBeanServerConnection getConnection(){
        JMXServiceURL jmxServiceURL = null;
        JMXConnector connector = null;
        MBeanServerConnection serverConnection = null;
        try {
             jmxServiceURL = new JMXServiceURL(Server_Url);
             connector =  JMXConnectorFactory.connect(jmxServiceURL);
             serverConnection = connector.getMBeanServerConnection();
        } catch (Exception e) {
            ExceptionUtil.printException(e);
        }
        return serverConnection;
    }
}
