package com.wk.asm.demo1;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomLoader extends ClassLoader {

    private String className;
    private byte[] classData;

    public CustomLoader(String className,byte[] classData){
        this.className = className;
        this.classData = classData;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (classData != null){
            return defineClass(className,classData,0,classData.length);
        }
        log.info("there is no class availde");
        return null;
    }
}
