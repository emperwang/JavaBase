package com.wk.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *  声明的服务
 */
public interface MessageService extends Remote{
    public String sendMessage(String clientMessage) throws RemoteException;

    public Message sendMessage(Message clientMessage) throws RemoteException;
}
