package com.wk.servlet;

import com.wk.Service.Hello;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.util.Set;
//容器启动的时候会将@HandlesTypes指定的这个类型下面的子类(实现类,子接口)传递过来,传递到onStartup函数的set集合
@HandlesTypes(value = {Hello.class})
public class MyServletContainerInitializer implements ServletContainerInitializer {
    /**
     * 应用启动运行的方法
     * @param set
     * @param servletContext
     * @throws ServletException
     */
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        System.out.println("Interesting type......................");
        for (Class<?> aClass : set) {
            System.out.println(aClass);
        }
    }
}
