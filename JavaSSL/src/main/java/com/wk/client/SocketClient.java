package com.wk.client;

import com.wk.tool.FilePathUtil;
import com.wk.tool.MyHandshakeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Properties;

public class SocketClient {
    private Logger log = LoggerFactory.getLogger(SocketClient.class);
    private Properties prop = FilePathUtil.getProp("config.properties");
    private SSLContext sslContext;
    private int port = Integer.parseInt(prop.getProperty("server.port"));
    private String host = prop.getProperty("host");
    private SSLSocket socket;

    public SocketClient() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        this.sslContext = Auth.getSslContext();
        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        socket = (SSLSocket) socketFactory.createSocket();
        // socket可以使用的支持的加密套件
        String[] supportedCipherSuites = socket.getSupportedCipherSuites();
        // 设置可以使用所有支持的加密套件
        socket.setEnabledCipherSuites(supportedCipherSuites);

        socket.setUseClientMode(true);
        InetSocketAddress socketAddress = new InetSocketAddress(host, port);
        socket.connect(socketAddress,0);
        MyHandshakeListener myHandshakeListener = new MyHandshakeListener();
        socket.addHandshakeCompletedListener(myHandshakeListener);
    }

    public void request() throws IOException {
        String encoding = prop.getProperty("serversocketencoding");
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        String user = "name";
        byte[] bytes = user.getBytes(encoding);
        int length = bytes.length;
        int pwd = 123;

        outputStream.writeInt(length);
        outputStream.write(bytes);
        // outputStream.write(pwd);

        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        int length2 = inputStream.readInt();
        byte[] data = new byte[length2];
        inputStream.read(data,0,length2);
        log.info("request result is:"+new String(data,encoding));

        socket.close();
    }

    public static void main(String[] args) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        SocketClient socketClient = new SocketClient();
        socketClient.request();
    }
}





