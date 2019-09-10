package com.wk.JMX.ch6_notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class Polling extends NotificationBroadcasterSupport implements PollingMBean,Runnable {
    private static Logger log = LoggerFactory.getLogger(Polling.class);
    private boolean stop = true;
    private int index = 0;
    private long interval = 1000;

    @Override
    public void start() {
        stop = false;
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void stop() {
        stop = true;
    }

    @Override
    public void setInterval(long time) {
        long temp = this.interval;
        this.interval = time;
        AttributeChangeNotification notification = new AttributeChangeNotification(this, 0, System.currentTimeMillis(), "Attribute Change",
                "interval", "long", new Long(temp), new Long(interval));

        sendNotification(notification);
    }

    @Override
    public void run() {
        while (!stop){
            try {
                Thread.sleep(interval);
                log.info("Polling");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Notification notification = new Notification("ch6.PollingMBean.counter", this, index++);

            sendNotification(notification);
        }
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] type = {"ch6.PollingMBean.counter"};
        String[] attChanges = {AttributeChangeNotification.ATTRIBUTE_CHANGE};
        MBeanNotificationInfo[] infos = new MBeanNotificationInfo[2];
        infos[0] = new MBeanNotificationInfo(type,"javax.management.Notification",
                "The Polling MBean counter");
        infos[1] = new MBeanNotificationInfo(attChanges,"javax.management.AttributeChangeNotification",
                "The Polling MBean counter");
        return infos;
    }
}










