package com.wk.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *  收发文件请求
 */
public class SocketServerSendReceiveFile {
    private  static  final Logger logger = LoggerFactory.getLogger(SocketServerSendReceiveFile.class);
    private static final String CharSet = "UTF-8";
    private static final Integer PORT = 9001;

    private static final String FileToSend = "D:/gc.log";
    private static final String SavePath = "D:/image";
    // 发送接收缓冲区
    private static final Integer SendBufSize = 8192;
    private static final Integer RevBufSize = 8192;

    public void serverStart(){
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            logger.info("server is ready.");
            while (true) {
                String msg = checkIO(serverSocket);
                if (msg.equals("read")) {
                    // 发送文件
                    logger.info("starting send file..");
                    sendFile(FileToSend, serverSocket);
                }
                if (msg.equals("write")) {
                    // 接收文件
                    logger.info("starting receive file...");
                    saveFile(SavePath,serverSocket);
                }
            }
        } catch (IOException e) {
            logger.error("start server error,error msg is:{}",e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     *  检查客户端是什么请求
     * @param serverSocket
     * @return
     */
    public String checkIO(ServerSocket serverSocket){
        try {
            Socket soc = serverSocket.accept();
            InputStream inputStream = soc.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            char[] buf = new char[10];
            int len = 0;
            StringBuilder builder = new StringBuilder();
            while (true){
                if (reader != null){
                    len = reader.read(buf);
                }
                if (len == -1){
                    break;
                }
                builder.append(new String(buf,0,len));
            }
            inputStream.close();
            soc.close();
            logger.info("receive msg is:{}",builder.toString());
            return builder.toString();
        } catch (IOException e) {
            logger.error("receive socket error,error msg is:{}",e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  发送一个文件
     * @param file
     */
    public void sendFile(String file,ServerSocket serverSocket){
        if (serverSocket == null){
            logger.error("serverSocket can not be null");
        }
        if (file == null || file.length() == 0){
            logger.error("file path must be set");
        }
        try {
            Socket socket = serverSocket.accept();
            File file1 = new File(file);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(outputStream));
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            byte[] buf = new byte[SendBufSize];
            String name = file1.getName();
            long length = file1.length();
            logger.info("file name is:{},the file length is:{}",name,length);
            // 写入文件名和文件长度
            dataOutputStream.writeUTF(name);
            dataOutputStream.writeLong(length);
            int count = 0;
            // 读取数据并写入
            while (true){
                if (dataInputStream != null){
                    count = dataInputStream.read(buf);
                }
                if (count == -1){
                    break;
                }
                dataOutputStream.write(buf,0,count);
            }
            dataOutputStream.flush();

            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            logger.error("accept socet error,error msg is:{}",e.getMessage());
        }
    }

    /**
     *  接收一个文件
     * @param savePath
     */
    public void saveFile(String savePath,ServerSocket serverSocket){
        if (serverSocket == null){
            logger.error("serverSocket can not be null");
        }
        if (savePath == null || savePath.length() == 0){
            logger.error("file path must be set");
        }
        try {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));
            String name = dataInputStream.readUTF();
            long length = dataInputStream.readLong();
            // 路径是否是  d:/server/download/  这种形式
            if (savePath.endsWith("/")){
                savePath += name;
            }else {
                savePath = savePath + "/" +name;
            }
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(savePath)));
            byte[] buf = new byte[RevBufSize];
            int count = 0;
            while ((count = dataInputStream.read(buf)) != -1){
                dataOutputStream.write(buf,0,count);
            }
            dataOutputStream.flush();

            dataInputStream.close();
            dataOutputStream.close();
            socket.close();

        } catch (IOException e) {
            logger.error("accept socet error,error msg is:{}",e.getMessage());
        }
    }

    public static void main(String[] args) {
        SocketServerSendReceiveFile server = new SocketServerSendReceiveFile();
        server.serverStart();
    }
}
