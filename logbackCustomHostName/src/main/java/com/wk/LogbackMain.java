package com.wk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LogbackMain {
    private static Logger log = LoggerFactory.getLogger(LogbackMain.class);

    public static void getHostName(){
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String hostName = localHost.getHostName();
            String hostAddress = localHost.getHostAddress();
            log.info("hostName is = "+hostAddress +" , hostAddress is :"+hostAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getHostName();
    }
}
