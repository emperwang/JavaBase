package com.wk.digester.demo;

public class Grade {
    private String name;
    private Class classes[] = new Class[0];
    private final Object serviceLock = new Object();

    public void addClass(Class c){
        synchronized(serviceLock){
            Class[] result = new Class[this.classes.length + 1];
            System.arraycopy(classes,0,result,0,classes.length);
            result[classes.length] = c;
            classes = result;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class[] getClasses() {
        return classes;
    }

    public void setClasses(Class[] classes) {
        this.classes = classes;
    }
}
