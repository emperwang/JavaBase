package com.wk.example2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: ekiawna
 * @Date: 2021/3/12 17:11
 * @Description
 */
public class HelloServlet extends HttpServlet {

    private String greeting;

    public HelloServlet(){
        this("hello world");
    }

    public HelloServlet(String greeting){
        this.greeting = greeting;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println("<h1>" + greeting + " from HelloServlet </h1>");
    }
}
