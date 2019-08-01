package com.wk.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class FacpsOMCServer {
    private static final String CharSet = "UTF-8";
    private static final Integer PORT = 9001;

    private static final String FileToSend = "D:/gc.log";
    private static final String SavePath = "D:/image";
    // 发送接收缓冲区
    private static final Integer SendBufSize = 8192;
    private static final Integer RevBufSize = 8192;

    public void serverStart() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            log.info("server is ready.");
            Boolean flag = false;
            while (true) {
                Socket soc = serverSocket.accept();
                flag = true;
                while (flag) {
                    String msg = SocketUtil.receiveMsgNotClose(soc);
                    if (msg.equals("read")) {
                        log.info("starting send file..");
                        SocketUtil.sendMsgNotClose("starting send file", soc);
                    }
                    if (msg.equals("write")) {
                        log.info("starting receive file...");
                        SocketUtil.sendMsgNotClose("starting receiving file", soc);
                    }
                }
            }
        } catch (IOException e) {
            log.error("start server error,error msg is:{}", e.getMessage());
        }
    }

    public static void main(String[] args) {
        FacpsOMCServer server = new FacpsOMCServer();
        server.serverStart();
    }
}
