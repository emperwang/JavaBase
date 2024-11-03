package com.wk.example2;

import com.wk.ExampleUtil;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * @author: ekiawna
 * @Date: 2021/3/12 17:10
 * @Description
 */
public class ExampleServer2 {

    public static Server createServer(int port){
        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new Connector[]{connector});

        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setContextPath("/");
        //contextHandler.addServlet(HelloServlet.class, "/hello");
        //contextHandler.addServlet(AsyncEchoServlet.class, "/echo/*");

        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[]{contextHandler, new DefaultHandler()});

        server.setHandler(handlers);

        return server;
    }

    public static void main(String[] args) throws Exception {
        int port = ExampleUtil.getPort(args, "http.port", 8080);
        Server server = createServer(port);

        server.start();
        server.join();
    }
}
