package com.wk.mybatis_plugin.plugin3;

/**
 * @author: Sparks
 * @Date: 2021/3/9 20:02
 * @Description
 */
public class TransactionInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Exception {
        System.out.println("------insert before code ---------");
        Object res = invocation.process();
        System.out.println("------insert after code ---------");
        return res;
    }
}
