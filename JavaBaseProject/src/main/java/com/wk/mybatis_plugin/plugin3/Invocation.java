package com.wk.mybatis_plugin.plugin3;

import java.lang.reflect.Method;

/**
 * @author: Sparks
 * @Date: 2021/3/9 20:00
 * @Description
 */
public class Invocation {
    // 目标对象
    private Object target;
    // 执行的方法
    private Method method;
    // 方法的参数
    private Object[] args;


    public Invocation(Object target, Method method, Object[] args) {
        this.target = target;
        this.method = method;
        this.args = args;
    }
    // 执行目标对象的方法
    public Object process() throws Exception{
        return method.invoke(target, args);
    }
}
