package com.wk.bridgemethod;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author: ekiawna
 * @Date: 2021/2/23 13:36
 * @Description
 */
@Slf4j
public class Parent {

    public static String modifierToString(Integer modifier){
        StringBuilder builder = new StringBuilder();
        if ((modifier & 0x01)==0x01){
            builder.append("public ");
        }
        if ((modifier & 0x02)==0x02){
            builder.append("private ");
        }
        if ((modifier & 0x04)==0x04){
            builder.append("protected ");
        }
        if ((modifier & 0x08)==0x08){
            builder.append("static ");
        }
        if ((modifier & 0x10)==0x10){
            builder.append("final ");
        }
        if ((modifier & 0x40)==0x40){
            builder.append("volatile ");
        }
        if ((modifier & 0x80)==0x80){
            builder.append("transient ");
        }
        if ((modifier & 0x1000)==0x1000){
            builder.append("synchetic ");
        }
        if ((modifier & 0x4000)==0x4000){
            builder.append("enum ");
        }
        log.info("modifier: {}", builder.toString());
        return builder.toString();
    }

    public static void main(String[] args) {
        modifierToString(4161);
        PPClass p = new ChildClass();
//        Object id = p.id(new Object());
//        if (id instanceof Object){
//            System.out.printf("id is object");
//        }
        Method[] methods = ChildClass.class.getDeclaredMethods();
        for (Method method : methods) {
            StringBuilder builder = new StringBuilder();
            String name = method.getName();
            int modifiers = method.getModifiers();
            builder.append(modifierToString(modifiers)).append(" ").append(name).append(" (");
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                String panme = parameter.getName();
                String typeName = parameter.getParameterizedType().getTypeName();
                builder.append(typeName).append(" ").append(panme).append(", ");
            }
            String s = builder.toString();
            s = s.substring(0,s.length()-2);
            s = s+" )";
            log.info("method: {}" , s);
        }
    }
}
