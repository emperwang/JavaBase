package com.wk;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

public class StartMain {
    public static void main(String[] args) {
        Tomcat tomcat = new Tomcat();
        HttpServlet httpServlet = new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                resp.getWriter().write("Hello, this is embed tomcat");
            }

            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                doGet(req, resp);
            }
        };

        tomcat.setHostname("localhost");
        tomcat.setPort(8080);
        Context context = tomcat.addContext("/embed", null);
        tomcat.addServlet(context, "dispatch", httpServlet);
        context.addServletMappingDecoded("/dispatch", "dispatch");
        Filter filter = new Filter() {

            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
                System.out.println("init filter");
            }

            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                System.out.println("doFilter");
                filterChain.doFilter(servletRequest, servletResponse);
            }

            @Override
            public void destroy() {
                System.out.println("filter destory");
            }
        };

        FilterDef filterDef = new FilterDef();
        filterDef.setFilter(filter);
        filterDef.setFilterName("myFilter");
        filterDef.addInitParameter("userName","Allen");

        FilterMap filterMap = new FilterMap();
        filterMap.addURLPatternDecoded("/*");
        filterMap.addServletName("*");
        filterMap.setFilterName("myFilter");
        filterMap.setCharset(Charset.forName("UTF-8"));

        context.addFilterDef(filterDef);
        context.addFilterMap(filterMap);


        try {
            tomcat.init();
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }


    }
}
