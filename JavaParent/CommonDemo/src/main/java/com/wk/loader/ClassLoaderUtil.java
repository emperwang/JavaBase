package com.wk.loader;

public class ClassLoaderUtil {

    /**
     *  获取classLoader
     * @return
     */
    public static ClassLoader getClassLoader(){
        return getContextClassLoader();
    }

    /**
     *  获取一个classLoader
     * @return
     */
    public static ClassLoader getContextClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载一个类
     * @param className  要加载的类名
     * @return
     */
    public static Class<?> toClass(String className){
        try{
            return getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }
}
