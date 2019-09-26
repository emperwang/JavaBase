package com.wk.localIp;

import java.net.*;
import java.util.Enumeration;
import java.util.List;

public class GetHostNameAndIP {

    /**
     *  获取本地IP，但是发现获取的是 虚拟机Vnet8 ip
     * @throws UnknownHostException
     */
    public static void getLoaclIP() throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        String loopbackAddr = InetAddress.getLoopbackAddress().getHostAddress();
        System.out.println("loopBack addr : "+ loopbackAddr);
        String hostName = localHost.getHostName();
        String hostAddress = localHost.getHostAddress();
        System.out.println("hostName : "+hostName);
        System.out.println("hostAddress : "+hostAddress);
    }

    /**
     *  获取所有的IPv4  的地址
     */
    public static void getAllIP4() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

        while (networkInterfaces.hasMoreElements()){
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            String displayName = networkInterface.getDisplayName();
            int mtu = networkInterface.getMTU();
            String name = networkInterface.getName();
            int index = networkInterface.getIndex();
            byte[] hardwareAddress = networkInterface.getHardwareAddress();
            System.out.println("displayName  :" + displayName);
            System.out.println("mtu  :" + mtu);
            System.out.println("name  :" + name);
            System.out.println("index  :" + index);
            if (hardwareAddress != null && hardwareAddress.length > 0) {
                System.out.println("hardwareAddress  :" + new String(hardwareAddress));
            }
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()){
                InetAddress address = inetAddresses.nextElement();
                String canonicalHostName = address.getCanonicalHostName();
                String hostAddress = address.getHostAddress();
                String hostName = address.getHostName();

                System.out.println("getInetAddresses, canonicalHostName: " + canonicalHostName);
                System.out.println("getInetAddresses, hostAddress : "+hostAddress);
                System.out.println("getInetAddresses, hostName : " + hostName);
            }
            List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
            interfaceAddresses.forEach( interfaceAddress -> {
                InetAddress address = interfaceAddress.getAddress();
                String hostAddress = address.getHostAddress();
                String hostName = address.getHostName();
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast != null) {
                    String broadcastHostAddress = broadcast.getHostAddress();
                    String broadcastHostName = broadcast.getHostName();
                    System.out.println("lamda broadcastHostAddress : " + broadcastHostAddress);
                    System.out.println("lamda broadcastHostName : " + broadcastHostName);
                }
                System.out.println("lamda hostAddress : "+hostAddress);
                System.out.println("lamda hostName : "+hostName);
            });
            System.out.println("------------------------------------------------------------");
        }
    }

    public static void main(String[] args) throws UnknownHostException, SocketException {
        // getLoaclIP();
        getAllIP4();
    }
}
