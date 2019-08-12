package com.wk.JMX;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;

public class MBeanClient {
    private static Logger logger = LoggerFactory.getLogger(MBeanClient.class);
    public static void main(String[] args) throws Exception{
        getName();
    }
    public static String getName() throws IOException, MalformedObjectNameException {
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:8082/jmxrmi/Msg");
        JMXConnector jmxConnector = JMXConnectorFactory.connect(url, null);
        MBeanServerConnection mbsc = jmxConnector.getMBeanServerConnection();
        ObjectName objectName = new ObjectName("jmxBean:name=Msg");
        MsgMBean msg = MBeanServerInvocationHandler.newProxyInstance(mbsc, objectName, MsgMBean.class, false);
        String message = msg.getMessage();
        logger.info("get msg is:"+message);
        return message;
    }
}
