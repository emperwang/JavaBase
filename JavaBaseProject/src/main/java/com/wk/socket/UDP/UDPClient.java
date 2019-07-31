package com.wk.socket.UDP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UDPClient {
    private static final Logger logger = LoggerFactory.getLogger(UDPClient.class);
    public static final Integer Max_Bytes = 2048;
    public static final String Server_IP = "127.0.0.1";
    public static final Integer Server_Port = 10101;
    public static void main(String[] args) {
        UDPClient udpClient = new UDPClient();
        Scanner scanner = new Scanner(System.in);
        while (true){
            // 从终端接收信息
            String msg = scanner.nextLine();
            if ("##".equals(msg))
                break;
            String s = udpClient.sendAndReceiveMsg(Server_IP, Server_Port, msg);
            logger.info("receive msg is:{}",msg);
        }

    }

    /**
     *  发送并且接受信息
     * @param ip 对方的ip
     * @param port 端口
     * @param msg
     * @return
     */
    public String sendAndReceiveMsg(String ip,Integer port,String msg){
        String responseMsg = "";
        DatagramSocket client = null;
        try {
            client = new DatagramSocket();
            byte[] data = msg.getBytes();
            InetAddress address = InetAddress.getByName(ip);
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, port);
            client.send(sendPacket);
            byte[] resposeByte = new byte[Max_Bytes];
            DatagramPacket responsePacket = new DatagramPacket(resposeByte, resposeByte.length);
            client.receive(responsePacket);

            responseMsg = new String(responsePacket.getData(), 0, responsePacket.getLength());
            logger.info("receive msg is:{}",responseMsg);

        } catch (SocketException e) {
            logger.error("error msg is:{}",e.getMessage());
        } catch (UnknownHostException e) {
            logger.error("error msg is:{}",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (client != null){
                client.close();
            }
        }
        return responseMsg;
    }
}
