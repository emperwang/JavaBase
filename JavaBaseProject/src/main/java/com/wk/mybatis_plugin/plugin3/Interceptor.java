package com.wk.mybatis_plugin.plugin3;

/**
 * @author: Sparks
 * @Date: 2021/3/9 20:00
 * @Description
 */
public interface Interceptor {
    Object intercept(Invocation invocation) throws Exception;
}
