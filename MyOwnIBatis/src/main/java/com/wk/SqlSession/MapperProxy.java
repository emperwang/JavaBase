package com.wk.SqlSession;

import com.wk.Config.Function;
import com.wk.Config.MapperBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class MapperProxy implements InvocationHandler {

    private MySqlSession mySqlSession;
    private MyConfiguration myConfiguration;

    public MapperProxy(MySqlSession mySqlSession, MyConfiguration myConfiguration) {
        this.mySqlSession = mySqlSession;
        this.myConfiguration = myConfiguration;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MapperBean mapperBean = myConfiguration.readMapper("UserMapper.xml");
        //是否是xml对应的接口
        if (!method.getDeclaringClass().getName().equals(mapperBean.getInterfaceName())){
            return null;
        }
        List<Function> list = mapperBean.getList();
        if (null != list || 0 != list.size()){
            for (Function function:list){
                //id是否和接口方法名一样
                if (method.getName().equals(function.getFunctionName())){
                    Object o = mySqlSession.selectOne(function.getSql(), String.valueOf(args[0]));
                    return o;
                }
            }
        }

        return null;
    }
}
