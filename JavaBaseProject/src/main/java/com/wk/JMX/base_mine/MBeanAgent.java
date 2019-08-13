package com.wk.JMX.base_mine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;

/***
 *  注册MBean
 *  并进行发布
 */
public class MBeanAgent {
    private static Logger logger = LoggerFactory.getLogger(MBeanAgent.class);
    public static void main(String[] args) throws Exception {
        startMBean(8082);
    }

    public static void startMBean(int port) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, IOException {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("jmxBean:name=Msg");
        Msg msg = new Msg();
        msg.setMessage("hello");
        beanServer.registerMBean(msg,objectName);

        LocateRegistry.createRegistry(port);
        JMXServiceURL jmxServiceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + port + "/jmxrmi/Msg");
        JMXConnectorServer jcs = JMXConnectorServerFactory.newJMXConnectorServer(jmxServiceURL, null, beanServer);
        jcs.start();
        logger.info("service:jmx:rmi:///jndi/rmi://localhost:" + port + "/jmxrmi/Msg---start");
    }
}
