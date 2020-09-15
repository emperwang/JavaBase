package com.wk.client;

import com.wk.thrift_demo.UserService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class ClientMain {
    public static final String sip = "localhost";
    public static final int sprot=2345;
    public static final int timeout=3000;

    public static void main(String[] args) throws TException {
        System.out.println("Start client");
        startClient();
    }

    public static void startClient() throws TException {
        TTransport transport = new TSocket(sip,sprot,timeout);
        TBinaryProtocol protocol = new TBinaryProtocol(transport);
        UserService.Client client = new UserService.Client(protocol);
        transport.open();
        System.out.println(client.getName(1));
        System.out.println(client.isExist("xiaomi"));

        transport.close();
    }
}
