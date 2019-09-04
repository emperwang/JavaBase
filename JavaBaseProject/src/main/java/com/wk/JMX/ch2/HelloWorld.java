package com.wk.JMX.ch2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

/**
 * MB 实现类
 */
public class HelloWorld extends NotificationBroadcasterSupport implements HelloWorldMBean{
    private String greeting = null;
    private static final Logger log = LoggerFactory.getLogger(HelloWorld.class);
    public HelloWorld(){
        this.greeting = "Hello World! I am a Standard MBean";
    }

    public HelloWorld(String greeting){
        this.greeting = greeting;
    }

    /**
     *  有set方法说明有 write权限
     * @param greeting
     */
    @Override
    public void setGreeting(String greeting) {
        this.greeting = greeting;

        // 添加通知
        Notification notification = new Notification("jmxdemo.ch2.helloWorld.test",
                this, -1, System.currentTimeMillis(), greeting);
        sendNotification(notification);
    }

    /**
     *  有get方法说明有读取权限
     * @return
     */
    @Override
    public String getGreeting() {
        return greeting;
    }

    /**
     * 打印功能
     */
    @Override
    public void printGreeting() {
        log.info(greeting);
    }

}
