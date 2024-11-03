package com.wk.example;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: ekiawna
 * @Date: 2021/3/12 16:56
 * @Description
 */
public class HelloWorld  extends AbstractHandler {

    //@Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest,
                       HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html;charset-utf-8");

        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().println("<h1>Hello World</h1>");

        request.setHandled(true);
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        Server server = new Server(port);
        server.setHandler(new HelloWorld());
        server.start();
        server.join();
    }

    @Override
    public void handle(String s, Request request, jakarta.servlet.http.HttpServletRequest httpServletRequest, jakarta.servlet.http.HttpServletResponse httpServletResponse) throws IOException, jakarta.servlet.ServletException {

    }
}
