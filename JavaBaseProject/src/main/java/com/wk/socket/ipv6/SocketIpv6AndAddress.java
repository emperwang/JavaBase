package com.wk.socket.ipv6;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SocketIpv6AndAddress {

    public static void getPreferHostAddress() throws UnknownHostException {
        //System.setProperty("java.net.preferIPv4Stack","true");
        System.setProperty("java.net.preferIPv6Addresses","true");
        String hostAddress = InetAddress.getLoopbackAddress().getHostAddress();
        System.out.println("getLoopbackAddress : "+hostAddress);
        String hostAddr = " fd15:4ba5:5a2b:1008:99e:1cf9:5ff4:8029";
        String host2 = "fe80::99e:1cf9:5ff4:8029";    // ok
        String host3 = "fe80::1142:87b7:11a3:f84a";    // ok
        InetAddress byName = InetAddress.getByName(host3);
        /**
         *  hostAddress  : fe80:0:0:0:1142:87b7:11a3:f84a
            hostName : my-THINK
            scope id : 0
            CanonicalHostName :my-THINK
         */
        if (byName instanceof Inet6Address){
            System.out.println("IPV6 ... ");
            Inet6Address byName1 = (Inet6Address) byName;
            System.out.println("hostAddress  : " + byName1.getHostAddress());
            System.out.println("hostName : " + byName1.getHostName());
            System.out.println("scope id : " + byName1.getScopeId());
            System.out.println("CanonicalHostName :" + byName1.getCanonicalHostName());
        }
        System.out.println("=================================================================");
        if (byName instanceof Inet4Address){
            System.out.println("Ipv4 ...");
            Inet4Address byName1 = (Inet4Address) byName;
            System.out.println("hostAddress  : " + byName1.getHostAddress());
            System.out.println("hostName : " + byName1.getHostName());
            System.out.println("CanonicalHostName :" + byName1.getCanonicalHostName());
        }

    }


    public static void main(String[] args) throws Exception {
        getPreferHostAddress();
    }
}
