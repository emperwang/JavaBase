package com.wk.server;

import com.wk.tool.FilePathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

public class Job implements Runnable {
    private Logger log = LoggerFactory.getLogger(Auth.class);
    private Socket socket;
    private Properties prop = FilePathUtil.getProp("config.properties");

    public Job(Socket socket) throws IOException {
        this.socket = socket;
    }
    @Override
    public void run() {
        String encoding = prop.getProperty("serversocketencoding");
        try {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            int length = inputStream.readInt();
            byte[] data = new byte[length];
            inputStream.read(data,0,length);

            log.info("server receive data is:"+new String(data));

            String result = "login success";
            byte[] bytes = result.getBytes(encoding);
            int length1 = bytes.length;
            outputStream.writeInt(length1);
            outputStream.write(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
