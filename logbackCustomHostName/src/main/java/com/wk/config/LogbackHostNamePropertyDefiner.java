package com.wk.config;

import ch.qos.logback.core.PropertyDefinerBase;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *  自定义一些logback的设置，此处是用于获取主机名
 */
public class LogbackHostNamePropertyDefiner extends PropertyDefinerBase {

    @Override
    public String getPropertyValue() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String hostName = localHost.getHostName();
            return hostName;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
