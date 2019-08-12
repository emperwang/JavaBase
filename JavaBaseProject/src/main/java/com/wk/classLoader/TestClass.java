package com.wk.classLoader;

public class TestClass {
    public static void main(String[] args) {
        ClassLoader loader = TestClass.class.getClassLoader();
        System.out.println(loader);
        while (loader.getParent() != null) {
            loader = loader.getParent();
            System.out.println(loader);
        }
    }
}
