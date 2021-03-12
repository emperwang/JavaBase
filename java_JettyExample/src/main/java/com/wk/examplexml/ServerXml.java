package com.wk.examplexml;

import com.wk.ExampleUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;

import java.io.IOException;

/**
 * @author: ekiawna
 * @Date: 2021/3/12 17:34
 * @Description
 */
public class ServerXml {

    public static Server createServer(int port) throws Exception {
        Resource resource = Resource.newSystemResource("exampleserver.xml");
        XmlConfiguration xml = new XmlConfiguration(resource);
        xml.getProperties().put("http.port", Integer.toString(port));

        Server server = (Server) xml.configure();
        return server;
    }

    public static void main(String[] args) throws Exception {
        int port = ExampleUtil.getPort(args, "http.port", 8080);
        Server server = createServer(port);

        server.start();
        server.join();
    }
}
