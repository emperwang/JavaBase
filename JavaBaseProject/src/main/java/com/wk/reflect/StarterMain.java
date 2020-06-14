package com.wk.reflect;

public class StarterMain {
    public static void main(String[] args) {
        getInnerClass();
    }

    // 获取一个class的内部类
    public static void getInnerClass(){
        // 此可以获取到所有的内部类,包括私有的
        Class<?>[] declaredClasses = UserBean.class.getDeclaredClasses();
        for (Class<?> aClass : declaredClasses) {
            System.out.println(aClass.getSimpleName());
        }

        System.out.println("---------------------------------------------------");
        //Class<?> declaringClass = UserBean.class.getDeclaringClass();
        //System.out.println("declaringClass: " + declaringClass.getName());


        System.out.println("---------------------------------------------------");
        // 此只能获取到 public static class类型的内部类
        Class<?>[] classes = UserBean.class.getClasses();
        for (Class<?> aClass : classes) {
            System.out.println(aClass.getSimpleName());
        }
    }
}
