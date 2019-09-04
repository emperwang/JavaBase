package com.wk.JMX.ch2;

import com.sun.jdmk.comm.HtmlAdaptorServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;

public class HelloAgent implements NotificationListener{
    private static final Logger log = LoggerFactory.getLogger(HelloAgent.class);
    private MBeanServer mbs = null;

    public HelloAgent(){
        // MBAgent
        // HelloAgent 表示此agent的domain name
        mbs = MBeanServerFactory.createMBeanServer("HelloAgent");
        // 适配器
        HtmlAdaptorServer adaptor = new HtmlAdaptorServer();

        HelloWorld helloWorld = new HelloWorld();

        ObjectName adaptorName = null;
        ObjectName helloWorldName = null;
        try {
            // 此名字为:HelloAgent表示domain Name，其他的为key-value对，相当于对bean的一个描述
            helloWorldName = new ObjectName("HelloAgent:name=helloworld1");
            // 注册bean
            mbs.registerMBean(helloWorld,helloWorldName);

            adaptorName = new ObjectName("HelloAgent:name=htmladaptor,port=9092");

            adaptor.setPort(9092);

            mbs.registerMBean(adaptor,adaptorName);

            helloWorld.addNotificationListener(this,null,null);

            adaptor.start();

        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace();
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        log.info("HelloAgent is running");
        HelloAgent helloAgent = new HelloAgent();
    }

    @Override
    public void handleNotification(Notification notification, Object handback) {
        log.info("Receive notification ....");
        log.info("type is :" + notification.getType());
        log.info("message is :"+ notification.getMessage());
        log.info("Sequence Number is:" + String.valueOf(notification.getSequenceNumber()));
        log.info("UserData is:" + notification.getUserData().toString());
    }
}
