package com.wk.javassist.dynamicAddMethod;

import javassist.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DynamicAddMethod {
    /**
     * 直接运行包错,重复加载类.duplicate class definition for name: "com/wk/javassist/dynamicAddMethod/UserInfo
     * @throws NotFoundException
     */
    public static void addMethod() throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String qualifiedName = UserInfo.class.getName();  // 全限定名
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.getCtClass(qualifiedName);
        // 创建方法 并 添加
        CtMethod sumMethod = CtNewMethod.make(Modifier.PUBLIC, CtClass.intType, "sum", new CtClass[]{CtClass.intType, CtClass.intType},
                null, "{return $1+$2;}", ctClass);
        ctClass.addMethod(sumMethod);

        Class aClass = ctClass.toClass();
        Object newInstance = aClass.newInstance();
        Method sum = aClass.getDeclaredMethod("sum", int.class, int.class);
        Object res = sum.invoke(newInstance, 1, 2);
        System.out.println(res);
    }

    /**
     *  使用自定义类加载器  进行加载,解决重复加载的问题
     * @param
     * @throws IllegalAccessException
     */
    public static void customLoaderToLoadClass() throws NotFoundException, CannotCompileException, IOException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        String qualifiedName = UserInfo.class.getName();  // 全限定名
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.getCtClass(qualifiedName);
        CtMethod sumMethod = CtNewMethod.make(Modifier.PUBLIC, CtClass.intType, "sum", new CtClass[]{CtClass.intType, CtClass.intType},
                null, "{return $1+$2;}", ctClass);
        ctClass.addMethod(sumMethod);
        // 把生成的类  写入文件
        ctClass.writeFile("H:\\FTPTest");

        UserInfo userInfo = new UserInfo();
        userInfo.setAge(20);
        // 使用自定义类加载器  进行加载
        AppCustomLoader appCustomLoader = AppCustomLoader.getInstance();
        Class<?> newClass = appCustomLoader.findClassByBytes(qualifiedName, ctClass.toBytecode());
        Object object = appCustomLoader.getObject(newClass, userInfo);
        Method sum = object.getClass().getDeclaredMethod("sum", int.class, int.class);
        Object invoke = sum.invoke(object, 1, 2);
        System.out.println(invoke);
    }

    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException, CannotCompileException, NotFoundException, NoSuchMethodException, IOException, NoSuchFieldException {
        // addMethod();
        customLoaderToLoadClass();
    }
}
