package com.wk.server;

import com.wk.thrift_demo.UserService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;

public class ServerClient {
    public static void main(String[] args) {
        startServer();
    }

    public static void startServer(){
        UserService.Processor<UserService.Iface> processor = new UserService.Processor<>(new UserServiceServer());

        try{
            TServerTransport tServerSocket = new TServerSocket(2345);
            TThreadPoolServer.Args args1 = new TThreadPoolServer.Args(tServerSocket);
            args1.processor(processor);
            args1.protocolFactory(new TBinaryProtocol.Factory());
            args1.transportFactory(new TTransportFactory());
            args1.minWorkerThreads(10);
            args1.maxWorkerThreads(20);
            TThreadPoolServer server = new TThreadPoolServer(args1);
            System.out.println("server started");
            server.serve();
        }catch (Exception e){
            System.out.println(e.getCause());
        }
    }
}
