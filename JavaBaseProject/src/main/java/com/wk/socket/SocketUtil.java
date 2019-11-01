package com.wk.socket;

import com.wk.socket.fcapsTest.OMCSocketMsgHeaders;
import com.wk.socket.fcapsTest.SocketPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class SocketUtil {
    private static final Logger log = LoggerFactory.getLogger(SocketUtil.class);
    private static final String HostAddress = "127.0.0.1";

    private static final Integer HostPort = 10234;

    private static final String CharSet = "UTF-8";

    private static Integer SoTimeOut = 30000;

    private static Integer SendBufSize = 1024;

    private static Integer ReceiveBufSize = 2014;

    private static final String FileToSend = "D:/gc.log";
    private static final String SavePath = "D:/image";

    public static Socket connectServer(String hostAddres, Integer port) {
        Socket socket = null;
        try {
            socket = new Socket(hostAddres, port);
            log.info("create socket connect....");
        } catch (IOException e) {
            log.info("create connect error,the msg is:{}", e.getMessage());
        }
        return socket;
    }

    /**
     * 向服务端使用TCP发送数据
     *
     * @param data
     * @return
     */
    public static void sendAndReceiveData(String data, Socket socket) throws IOException {
        if (socket == null) {
            connectServer(HostAddress, HostPort);
        }
        // 向服务端发送信息
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(data.getBytes());
        outputStream.flush();
        log.info("发送完毕！");

        // 接收服务端的返回值
        InputStream inputStream = socket.getInputStream();
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }
        byte[] recv = new byte[inputStream.available()];
        inputStream.read(recv);

        String str_recv = new String(recv);
        log.info("客户端：接收到服务端的文字：" + str_recv);

        outputStream.close();
        inputStream.close();
        closeConnect(socket);
    }

    /**
     * 发送消息
     */
    public static void sendMsg(String msg, Socket socket) {
        if (msg == null || msg.length() == 0) {
            log.error("msg can not be null");
        }
        if (socket == null) {
            // connectServer(HostAddress, HostPort);
            log.info("socket is null ..... error");
            return;
        }
        try {
            OutputStream outputStream = socket.getOutputStream();
            Writer writer = new OutputStreamWriter(outputStream);
            writer.write(msg);
            writer.flush();

            writer.close();
            closeConnect(socket);
        } catch (IOException e) {
            log.error("send msg error,error msg is:{}", e.getMessage());
        }
    }

    public static void sendMsg2(String msg, Socket socket) {
        if (msg == null || msg.length() == 0) {
            log.error("msg can not be null");
        }
        if (socket == null) {
            connectServer(HostAddress, HostPort);
        }
        try {
            OutputStream outputStream = socket.getOutputStream();
            Writer writer = new OutputStreamWriter(outputStream);
            writer.write(msg);
            writer.flush();

            writer.close();
            closeConnect(socket);
        } catch (IOException e) {
            log.error("send msg error,error msg is:{}", e.getMessage());
        }
    }

    /**
     * 发送数据包
     *
     * @param socketPacket
     * @param socket
     * @throws Exception
     */
    public static void sendSocket(SocketPacket socketPacket, Socket socket) throws IOException {
        OutputStream output = null;
        output = socket.getOutputStream();
        // 写数据发送报文
        output.write(socketPacket.getByteStream());
        output.flush();
    }

    /**
     * 接收消息
     *
     * @return
     */
    public static String receiveSocket(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        byte[] msgHeader = null;
        byte[] startSign = null;
        byte[] msgTyps = null;
        byte[] timeStamp = null;
        byte[] bodyLen = null;
        byte[] bodyBytes = null;

        msgHeader = getDataBytesFromStream(inputStream, 9);
        startSign = subBytes(msgHeader, 0, 2);
        msgTyps = subBytes(msgHeader, 2, 1);
        timeStamp = subBytes(msgHeader, 3, 4);
        bodyLen = subBytes(msgHeader, 7, 2);
        int lenght = bytesToShort(bodyLen);
        byte[] body = getDataBytesFromStream(inputStream, lenght);

        String revBody = new String(body, CharSet);
        return revBody;
    }

    public static short bytesToShort(byte[] b) {
        short value = (short) (b[1] & 0xFF | (b[0] & 0xFF) << 8);
        return value;
    }

    /**
     * 读取输入流中的数据
     *
     * @param inputStream
     * @param len
     * @return
     */
    public static byte[] getDataBytesFromStream(InputStream inputStream, int len) throws IOException {
        byte[] bytes = new byte[len];
        int count = 0;
        //while ((count = inputStream.available()) > 0) ;
        inputStream.read(bytes, 0, len);
        return bytes;
    }

    /**
     * 截取byte数组
     *
     * @param src
     * @param begin
     * @param count
     * @return
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] data = new byte[count];
        for (int i = begin; i < begin + count; i++) {
            data[i - begin] = src[i];
        }
        return data;
    }

    /**
     * 发送消息
     */
    public static void sendMsgNotClose(String msg, Socket socket) {
        if (socket == null) {
            socket = connectServer(HostAddress, HostPort);
        }
        PrintWriter writer = null;
        try {
            OutputStream outputStream = socket.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream));
            writer.println(msg);
            writer.flush();
        } catch (IOException e) {
            log.error("send msg error,error msg is:{}", e.getMessage());
        } finally {

        }
    }

    /**
     * 接收消息
     *
     * @param socket
     * @return
     */
    public static String receiveMsgNotClose(Socket socket) {
        if (socket == null) {
            socket = connectServer(HostAddress, HostPort);
        }
        BufferedReader reader = null;
        try {
            InputStream inputStream = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            int count = 0;
            while ((count = inputStream.available()) > 0) ;

            String revMsg = reader.readLine();
            return revMsg;
        } catch (IOException e) {
            log.error("send msg error,error msg is:{}", e.getMessage());
        }
        return null;
    }

    /**
     * 关闭连接
     *
     * @param socket
     */
    public static void closeConnect(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
                log.info("close socket connection..");
            } catch (IOException e) {
                log.error("close socket error,erroe msg is:{}", e.getMessage());
            }
        }
    }

    /**
     * 发送文件
     */
    public static void sendFile(String file, Socket socket) {
        if (file == null || file.length() == 0) {
            log.error("file must be set");
        }
        if (socket == null) {
            socket = connectServer(HostAddress, HostPort);
            log.debug("socket can not be null");
        }
        File file1 = new File(file);
        String name = file1.getName();
        long length = file1.length();
        try {
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(outputStream));
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            dataOutputStream.writeUTF(name);
            dataOutputStream.writeLong(length);
            byte[] buf = new byte[SendBufSize];
            int count = -1;
            while ((count = dataInputStream.read(buf)) != -1) {
                dataOutputStream.write(buf, 0, count);
            }
            dataOutputStream.flush();

            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            log.error("socket occur error,error msg is:{}", e.getMessage());
        }
    }

    /**
     * 接收文件
     *
     * @param savePath
     * @param
     */
    public static void revFileByClose(String savePath, Socket socket) {
        if (savePath == null || savePath.length() == 0) {
            log.error("file must be set");
        }
        if (socket == null) {
            socket = connectServer(HostAddress, HostPort);
            log.debug("socket can not be null");
        }
        try {
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));
            String name = dataInputStream.readUTF();
            long length = dataInputStream.readLong();
            if (savePath.endsWith("/")) {
                savePath += name;
            } else {
                savePath += "/" + name;
            }
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(savePath)));
            log.info("savepath is:{},file name is:{},file length is:{}", savePath, name, length);
            byte[] buf = new byte[ReceiveBufSize];
            int count = 0;
            while ((count = dataInputStream.read(buf)) != -1) {
                dataOutputStream.write(buf, 0, count);
            }
            dataOutputStream.flush();

            dataOutputStream.close();
            dataInputStream.close();
            socket.close();
        } catch (IOException e) {
            log.error("socket occur error,error msg is:{}", e.getMessage());
        }
    }

    /**
     * 分析登录的结果
     * 结果: ackLoginAlarm;result=fail;resDesc=username-error
     *
     * @param response
     * @return
     */
    private static boolean analysisLoginResult(String response) {
        if (response == null || response.length() == 0) {
            return false;
        }
        String[] split = response.split(";");
        String res = split[1].split("=")[1].toLowerCase();
        if (res.trim().equals("fail")) {
            return false;
        }
        if (res.trim().equals("succ")) {
            return true;
        }
        return false;
    }

    public static boolean login(String userName, String password, Socket socket) throws Exception {
        boolean flag = true;
        int failCount = 0;

        // 2. 登录
        SocketPacket loginPacket = OMCSocketMsgHeaders.configLoginPacket(userName, password, OMCSocketMsgHeaders.SocketType);
        try {
            sendSocket(loginPacket, socket);
            String loginResult = receiveSocket(socket);
            log.info("socket login msg body is:{}", loginResult);
            while (!analysisLoginResult(loginResult)) {
                failCount++;
                Thread.sleep(2000);
                loginPacket = OMCSocketMsgHeaders.configLoginPacket(userName, password, OMCSocketMsgHeaders.SocketType);
                sendSocket(loginPacket, socket);
                loginResult = receiveSocket(socket);
                if (failCount >= 3) {
                    flag = false;
                    throw new Exception("socket login failed more than three times");
                }
            }
        } catch (IOException e) {
            log.error("socket login IOException error,error msg is:{}", e.getMessage());
        } catch (InterruptedException e) {
            log.error("socket login InterruptedException error,error msg is:{}", e.getMessage());
        }
        return flag;
    }


    public static void startReceiveMsg(Socket socket) throws Exception {
        //  此时认定登录完成
        while (true) {
            String alarmMsg = receiveSocket(socket);
            System.out.println(alarmMsg);
        }
        //closeSocketConnect();
    }

    public static void main(String[] args) throws Exception {
        testIpv6();
    }

    private static void testIpv6(){
        String hostAddressIpv4 = "127.0.0.1";
        String hostAddressIpv6 = "fd15:4ba5:5a2b:1008:99e:1cf9:5ff4:8029";
        Socket socket = connectServer(hostAddressIpv6, 9001);
        sendMsg("this is ipv6 test",socket);
    }

    private static void testFcaps() throws Exception {
        Socket socket = connectServer(HostAddress, HostPort);
        boolean omc = login("omc", "123", socket);
        log.info("登录服务.." + omc);
        while (omc) {
            startReceiveMsg(socket);
            Thread.sleep(1000);
            /*sendMsgNotClose("write", socket);
            //sendFile(FileToSend, socket);

            String s = receiveMsgNotClose(socket);
            log.info("rev from server :{}",s);

            sendMsgNotClose("read", socket);
            s = receiveMsgNotClose(socket);
            log.info("rev from server2 :{}",s);*/
        }
    }
}
