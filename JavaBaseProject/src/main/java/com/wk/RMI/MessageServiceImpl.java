package com.wk.RMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *  对应具体服务端的实现
 */
public class MessageServiceImpl implements MessageService{

    @Override
    public String sendMessage(String clientMessage) {
        String serverMessage = null;
        if (clientMessage.equals("Client Message")){
            serverMessage = "Server Message";
        }
        return serverMessage;
    }

    @Override
    public Message sendMessage(Message clientMessage) {
        Message serverMessage = null;
        if (clientMessage.getMessageText().equals("Client Message")){
            serverMessage = new Message("Server Message","Text/plain");
        }
        return serverMessage;
    }

    // 服务端发布服务
    public void createStubAndBind() throws RemoteException {
        // 创建stub
        MessageService stub = (MessageService) UnicastRemoteObject.exportObject(this, 0);
        Registry registry = LocateRegistry.createRegistry(1099);
        // 服务绑定
        registry.rebind("MessageService",stub);
    }
    // 在test测试中,模拟client请求
}
















