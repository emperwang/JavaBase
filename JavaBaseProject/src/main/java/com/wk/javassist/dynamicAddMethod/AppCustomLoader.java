package com.wk.javassist.dynamicAddMethod;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class AppCustomLoader extends ClassLoader {

    private static class SingletomHolder{
        public static final AppCustomLoader instance = new AppCustomLoader();
    }

    public static AppCustomLoader getInstance(){
        return SingletomHolder.instance;
    }

    private AppCustomLoader(){}

    /**
     *  通过 classBytes 加载类
     * @param className
     * @param classBytes
     * @return
     */
    public Class<?> findClassByBytes(String className,byte[] classBytes){
        return defineClass(className,classBytes,0,classBytes.length);
    }

    /**
     *  赋值原对象的所有属性，并返回一个新对象
     * @param clazz
     * @param srcObj
     * @return
     */
    public Object getObject(Class<?> clazz,Object srcObj) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        Object instance = clazz.getDeclaredConstructor().newInstance();
        Field[] fields = srcObj.getClass().getDeclaredFields();
        for (Field oldField : fields) {
            String fieldName = oldField.getName();
            oldField.setAccessible(true);
            Field newField = instance.getClass().getDeclaredField(fieldName);
            newField.setAccessible(true);
            newField.set(instance,oldField.get(srcObj));
        }
        return instance;
    }
}
