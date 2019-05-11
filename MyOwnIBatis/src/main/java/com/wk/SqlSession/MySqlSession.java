package com.wk.SqlSession;

import java.lang.reflect.Proxy;

public class MySqlSession {
    private Excutor excutor = new MyExcutor();

    private MyConfiguration myConfiguration = new MyConfiguration();

    public <T> T selectOne(String statement,Object parameter){
        Object query = excutor.query(statement, parameter);
        return (T)query;
    }

    public <T> T getMapper(Class<T> clazz){
        //动态调用
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
                new MapperProxy(this, myConfiguration));
        return (T) o;
    }
}
