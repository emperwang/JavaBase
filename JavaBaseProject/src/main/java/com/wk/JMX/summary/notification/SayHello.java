package com.wk.JMX.summary.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

/**
 *  继承NotificationBroadcasterSupport，获得发送消息的能力
 */
public class SayHello extends NotificationBroadcasterSupport
                        implements SayHelloMBean {
    private static Logger log = LoggerFactory.getLogger(SayHello.class);
    private int seq = 0;

    @Override
    public void sayHelloToEveryOne() {
        log.info("say Hello to Every One");
        // create Notification
        Notification notification = new Notification("SayHello", // notification Name,Type,消息的类型
                this,       // who send
                ++seq,              // sequence number
                System.currentTimeMillis(), // send time
                "This is Hello from SayHello");  // message content

        sendNotification(notification);
    }
}
















