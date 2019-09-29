package com.wk.main;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NetworkTrafficServerConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 *  启动
 *  此启动是手动定义好需要的bean,并把彼此的依赖注入
 *  之后调用server的启动函数
 */
public class StartApp {
    public static void main(String[] args) {
        try {
            // 创建线程池
            QueuedThreadPool queuedThreadPool = new QueuedThreadPool();
            queuedThreadPool.setMaxThreads(50);
            queuedThreadPool.setMinThreads(10);
            // server 注入 线程池
            Server server = new Server(queuedThreadPool);
            // 创建 connector 并注入server
            NetworkTrafficServerConnector trafficServerConnector = new NetworkTrafficServerConnector(server);
            trafficServerConnector.setPort(8089);
            // handler 创建
            HandlerList handlerList = new HandlerList();
            ContextHandlerCollection handlerCollection = new ContextHandlerCollection();
            WebAppContext webAppContext = new WebAppContext();
            webAppContext.setThrowUnavailableOnStartupException(true);
            webAppContext.setContextPath("/");
            webAppContext.setConfigurationDiscovered(true);
            webAppContext.setDefaultsDescriptor("web.xml");
            webAppContext.setResourceBase("/");
            handlerCollection.setHandlers(new Handler[]{webAppContext});
            DefaultHandler defaultHandler = new DefaultHandler();
            handlerList.setHandlers(new Handler[]{handlerCollection,defaultHandler});
            // 把connector handler 注入到server
            server.setHandler(handlerList);
            server.setConnectors(new Connector[]{trafficServerConnector});
            // server 启动
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
