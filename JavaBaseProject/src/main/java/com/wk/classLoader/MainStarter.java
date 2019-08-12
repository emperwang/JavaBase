package com.wk.classLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainStarter {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        String classPath = "D:\\test\\com\\wk\\classLoader\\TestClass.class";
        String className = "com.wk.classLoader.TestClass";
        // 创建类加载器，并设置class文件路径
        MyClassLoader classLoader = new MyClassLoader(classPath);
        // 加载类
        Class<?> testClass = classLoader.findClass(className);
        // 打印加载器
        System.out.println("classLoader is:"+testClass.getClassLoader());
        System.out.println("------------------------------------------------");
        // 反射获取main方法
        Method mainMethod = testClass.getDeclaredMethod("main", String[].class);
        Object object = testClass.newInstance();
        String[] arg = {"ad"};
        mainMethod.invoke(object,(Object)arg);
    }
}
