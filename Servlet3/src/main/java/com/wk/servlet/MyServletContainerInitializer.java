package com.wk.servlet;

import com.wk.Service.Hello;
import com.wk.servlet.filter.MyFilter;
import com.wk.servlet.listener.MyListener;

import javax.servlet.*;
import javax.servlet.annotation.HandlesTypes;
import java.util.EnumSet;
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
        //添加一些三大组件 filter，servlet，listener
        //添加servlet
        ServletRegistration.Dynamic myServlet = servletContext.addServlet("MyServlet33", new MyServlet());
        //配置servlet的映射
        myServlet.addMapping("/my");

        //添加filter
        FilterRegistration.Dynamic myFilter = servletContext.addFilter("MyFilter", MyFilter.class);
        //配置filter的映射路径
        myFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST),true,"/*");

        //添加listener
        servletContext.addListener(MyListener.class);
    }
}
