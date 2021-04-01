package com.wk.dp.structural.facade;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 14:46
 * @Description
 */
public class ApplicationContextDefault {
    public String getContextName(){
        System.out.println("get Context Name");
        return "contextName";
    }

    public String getAttribute(String name){
        System.out.println("get attribute");
        return name;
    }

    public void setAttribute(String name,String value){
        System.out.println("set attribute.");
    }

    public void setContextName(){
        System.out.println("set Context name");
    }

    public void setParentContext(){
        System.out.println("set Parent Context");
    }
}
