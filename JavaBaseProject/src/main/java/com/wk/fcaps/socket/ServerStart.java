package com.wk.fcaps.socket;

import com.wk.fcaps.socket.server.ServerSimulator;
import com.wk.fcaps.socket.util.Socketutil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class ServerStart {
    private static Integer port = 19000;
    private static Socket socket;
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(port);
        socket = serverSocket.accept();
        log.info("socket ready");
        ServerSimulator simulator = new ServerSimulator(socket);
        simulator.setName("server");
        simulator.start();

        simulator.join();

    }

    public static Socket getSocket(){
        return socket;
    }
}
