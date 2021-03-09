package com.wk.mybatis_plugin.plugin4;

import com.wk.mybatis_plugin.plugin3.Invocation;

/**
 * @author: Sparks
 * @Date: 2021/3/9 20:28
 * @Description
 */
public interface Interceptor {
    // 具体的拦截处理
    Object interceptor(Invocation invocation) throws Exception;
    // 插入目标类
    Object plugin(Object target);
}
