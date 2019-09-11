package com.wk.JMX.summary.notification;

import com.wk.JMX.summary.standardMbean.Hello;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.Notification;
import javax.management.NotificationListener;

/**
 *  监听器
 */
public class SayHelloListener implements NotificationListener {
    private static Logger log = LoggerFactory.getLogger(SayHelloListener.class);

    @Override
    public void handleNotification(Notification notification, Object handback) {
        log.info("Type = "+notification.getType()
                +";\n source="+notification.getSource()
                +";\n SequenceNumbet="+notification.getSequenceNumber()
                +";\n send Time = "+notification.getTimeStamp()
                +";\n Message = "+notification.getMessage());

        if (handback != null){
            if (handback instanceof Hello){
                Hello hello = (Hello) handback;
                hello.helloWorld(notification.getMessage());
            }
        }
    }
}

























