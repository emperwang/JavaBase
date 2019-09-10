package com.wk.JMX.ch6_notification;

import com.wk.JMX.ch3.util.ExceptionUtil;
import com.wk.JMX.ch3.util.RMIClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;

public class PollingSetup implements NotificationListener{
    private static Logger log = LoggerFactory.getLogger(PollingSetup.class);
    public PollingSetup(){
        try {
            MBeanServerConnection connection = RMIClientFactory.getConnection();
            ObjectName objectName = new ObjectName("JMXBookAgent:name=polling");

            connection.createMBean("com.wk.JMX.ch6_notification.Polling",
                    objectName);

            connection.addNotificationListener(objectName,this,null,null);
        } catch (Exception e) {
            ExceptionUtil.printException(e);
        }
    }

    @Override
    public void handleNotification(Notification notification, Object handback) {
        String type = notification.getType();
        log.info("polling receive msg is:"+type);
    }

    public static void main(String[] args) {
        PollingSetup pollingSetup = new PollingSetup();
    }
}
