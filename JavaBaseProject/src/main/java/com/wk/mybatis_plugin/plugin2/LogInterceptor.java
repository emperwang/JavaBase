package com.wk.mybatis_plugin.plugin2;

/**
 * @author: Sparks
 * @Date: 2021/3/9 19:51
 * @Description
 */
public class LogInterceptor implements Interceptor {
    @Override
    public void interceptor() {
        System.out.println("Log interceptor....");
    }
}
