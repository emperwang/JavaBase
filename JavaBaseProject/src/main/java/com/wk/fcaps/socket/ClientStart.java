package com.wk.fcaps.socket;

import com.wk.fcaps.socket.bean.SourceBo;
import com.wk.fcaps.socket.client.SocketTask;

public class ClientStart {
    private static Integer port = 19000;
    public static void main(String[] args) throws InterruptedException {
        int taskId = 10;
        SourceBo sourceBo = new SourceBo();
        sourceBo.setSocketIp("192.168.30.1");
        sourceBo.setSocketPassword("123456");
        sourceBo.setSocketPort(port);
        sourceBo.setSocketUser("admin");
        SocketTask socketTask = new SocketTask(sourceBo, taskId);
        socketTask.setName("socketClient");
        socketTask.start();
        socketTask.join();
    }

}
