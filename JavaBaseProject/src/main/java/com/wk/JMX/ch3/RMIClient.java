package com.wk.JMX.ch3;

import com.wk.JMX.ch3.util.ExceptionUtil;
import com.wk.JMX.ch3.util.RMIClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class RMIClient {
    private static final Logger log = LoggerFactory.getLogger(RMIClient.class);

    public void printBeanMesage(){
        MBeanServerConnection connection = RMIClientFactory.getConnection();
        ObjectName name = null;
        try {
            name = new ObjectName("JMXBookAgent:name=helloWorld");
            // print domains
            String defaultDomain = connection.getDefaultDomain();
            log.info("defaultDomain is:"+defaultDomain);

            // Mbean count
            Integer mBeanCount = connection.getMBeanCount();
            log.info("MBean count is:"+mBeanCount);

            // invoke via rmi
            connection.invoke(name,"printGreeting",null,null);

            // get MBean information
            MBeanInfo helloInfo = connection.getMBeanInfo(name);
            MBeanOperationInfo[] operations = helloInfo.getOperations();
            for (MBeanOperationInfo operation : operations) {
                log.info(operation.getName());
            }

        } catch (Exception e) {
            ExceptionUtil.printException(e);
        }
    }

    public static void main(String[] args) {
        RMIClient rmiClient = new RMIClient();
        rmiClient.printBeanMesage();
    }
}
