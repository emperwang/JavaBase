package com.wk.util;

import org.springframework.context.ApplicationContext;

import java.util.Map;

public class PrintUtil {
    //打印注册的所有的bean的名字
    public static void printNamesInIOC(ApplicationContext applicationContext){
        String[] names = applicationContext.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
    }
    //获得注册的bean的个数
    public static void printBeanCounts(ApplicationContext applicationContext){
        int count = applicationContext.getBeanDefinitionCount();
        System.out.println("bean's num is :"+count);
    }
    //通过类型获得bean
    public static Map<String, ?> getBeanByType(ApplicationContext applicationContext,Class<?> clazz){
        Map<String, ?> beans = applicationContext.getBeansOfType(clazz);
        return beans;
    }
    //通过类型打印bean的名字
    public static void printBeansNameByType(ApplicationContext applicationContext,Class<?> clazz){
        String[] names = applicationContext.getBeanNamesForType(clazz);
        for (String name : names) {
            System.out.println(name);
        }
    }
    
}
