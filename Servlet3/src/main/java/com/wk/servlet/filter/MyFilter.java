package com.wk.servlet.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("MyFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();
        String scheme = servletRequest.getScheme();
        System.out.println("MyFilter doFilter scheme:"+scheme+":"+requestURI);
        filterChain.doFilter(servletRequest,servletResponse);  //放行
    }

    @Override
    public void destroy() {
        System.out.println("MyFilter destory");
    }
}
