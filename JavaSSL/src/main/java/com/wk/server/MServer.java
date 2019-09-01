package com.wk.server;

import com.wk.client.SocketClient;
import com.wk.tool.FilePathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *  添加虚拟机参数 :-Djavax.net.debug=ssl  或  -Djavax.net.debug=all  可以打开ssl调试模式
 *  添加虚拟机参数 -javaagent:jSSLKeyLog.jar=/path/to/your_logfile.log 记录SSL的通信session key
 */
public class MServer {
    private Logger log = LoggerFactory.getLogger(MServer.class);
    private SSLContext sslContext;
    private SSLServerSocketFactory sslServerSocketFactory;
    private SSLServerSocket sslServerSocket;
    private Executor executor;
    private Properties prop = FilePathUtil.getProp("config.properties");
    private String host = prop.getProperty("host");
    public MServer() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        Integer serverListenPort = Integer.parseInt(prop.getProperty("server.port"));
        Integer serverThreadPoolSize = Integer.parseInt(prop.getProperty("server.thread.pool.size"));
        Integer serverRequestQueueSize = Integer.parseInt(prop.getProperty("server.request.queue.size"));
        Integer authority = Integer.parseInt(prop.getProperty("authority"));

        executor = Executors.newFixedThreadPool(serverThreadPoolSize);

        sslContext = Auth.getSSLContext();
        sslServerSocketFactory = sslContext.getServerSocketFactory();
        // 只是创建哟个TCP 连接，ssl handshake 还没有开始
        // 客户端或服务端第一次试图获取socket输入流 或 输出流时
        // SSL handshake 才开始
        sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket();

        String[] supportedCipherSuites = sslServerSocketFactory.getSupportedCipherSuites();
        sslServerSocket.setEnabledCipherSuites(supportedCipherSuites);;
        // 默认时client mode，必须在握手开始之前调用
        sslServerSocket.setUseClientMode(false);
        if (authority.intValue() == 2){
            // 只有设置server mode，给配置才能生效
            // 如果客户端不提供证书，通信将会结束
            sslServerSocket.setNeedClientAuth(true);
        }else {
            // 只有设置为server mode，该配置才会生效
            // 即使客户端不提供其证书，通信也将继续
            sslServerSocket.setWantClientAuth(true);
        }
        sslServerSocket.setReuseAddress(true);
        sslServerSocket.setReceiveBufferSize(128*1024);
        sslServerSocket.setPerformancePreferences(3,2,1);

        sslServerSocket.bind(new InetSocketAddress(host,serverListenPort),serverRequestQueueSize);

        System.out.println("server start up");
        System.out.println("server port is:"+serverListenPort);
    }

    public void service() throws IOException {
        while (true){
            SSLSocket sslSocket = null;
            System.out.println("wait for client request !!!");
            sslSocket = (SSLSocket) sslServerSocket.accept();
            Job job = new Job(sslSocket);
            executor.execute(job);
        }
    }

    public static void main(String[] args) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        MServer mServer = new MServer();
        mServer.service();
    }
}
