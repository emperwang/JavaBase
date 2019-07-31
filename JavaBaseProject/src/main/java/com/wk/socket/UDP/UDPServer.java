package com.wk.socket.UDP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;

public class UDPServer {
    private static final Logger logger = LoggerFactory.getLogger(UDPServer.class);
    public static final String Server_IP = "127.0.0.1";

    public static final Integer Server_Port = 10101;

    public static final Integer Max_Bytes = 2048;

    public void startServer(String ip,Integer port){
        DatagramSocket client = null;
        try {
            // 构建地址
            InetAddress address = InetAddress.getByName(ip);
            client = new DatagramSocket(port, address);

            byte[] buf = new byte[Max_Bytes];
            // 接收信息packet
            DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
            while (true){
                // 接收信息
                client.receive(receivePacket);
                // 解析获取的信息
                byte[] data = receivePacket.getData();
                String msg = new String(data, 0, receivePacket.getLength());
                logger.info("UDPServer receive msg is:{}",msg);
                // 获取客户端的ip和端口
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // 返回响应信息
                String response = "Hello client,the time is:"+System.currentTimeMillis();
                byte[] reponseByte = response.getBytes();
                // 封装响应信息
                DatagramPacket responsePacket = new DatagramPacket(reponseByte, reponseByte.length,clientAddress,clientPort);
                // 发送信息
                client.send(responsePacket);
            }
        } catch (UnknownHostException e) {
            logger.error("error msg is:{}",e.getMessage());
        } catch (SocketException e) {
            logger.error("error msg is:{}",e.getMessage());
        } catch (IOException e) {
            logger.error("error msg is:{}",e.getMessage());
        }finally {
            if (client != null){
                client.close();
            }
        }
    }

    public static void main(String[] args) {
        UDPServer udpServer = new UDPServer();
        udpServer.startServer(Server_IP,Server_Port);
    }
}
