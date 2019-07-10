package com.wk.RMI;

import org.junit.BeforeClass;
import org.junit.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

// 模拟客户端发送信息到服务端
public class JavaRMIIntegrationTest{

/*    @Before
    // 服务端服务启动
    public void serverStarts(){
        MessageServiceImpl service = new MessageServiceImpl();
        try {
            service.createStubAndBind();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }*/
    @BeforeClass
    public static void serverStarts(){
        MessageServiceImpl service = new MessageServiceImpl();
        try {
            service.createStubAndBind();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    // 发送数据到服务端
    @Test
    public void clientsSendsMessageToServer(){
        try {
            // 此处因为server和client在一台机器上，故没有写ip和端口
            // 如果服务在不同机器上,此处就需要填写ip额和端口
            Registry registry = LocateRegistry.getRegistry();
            // 这里要和发布的服务名字一样
            MessageService service = (MessageService) registry.lookup("MessageService");
            String serverMessage = service.sendMessage("Client Message");
            System.out.println(serverMessage);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

}
