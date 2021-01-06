package com.wk.classLoader;

/**
 * @author: wk
 * @Date: 2021/1/5 17:55
 * @Description
 */
public class LoaderPath {
    public static void main(String[] args) {
        /*
        file:/C:/code-workspace/source/JavaBase/JavaBaseProject/target/classes/com/wk/classLoader/
        file:/C:/code-workspace/source/JavaBase/JavaBaseProject/target/classes/
        file:/C:/code-workspace/source/JavaBase/JavaBaseProject/target/classes/
        null
         */
        System.out.println(LoaderPath.class.getResource(""));
        System.out.println(LoaderPath.class.getResource("/"));
        System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
        System.out.println(Thread.currentThread().getContextClassLoader().getResource("/"));
    }
}
