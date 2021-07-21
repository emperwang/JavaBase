package com.wk.bd.callsite;

import java.lang.invoke.*;
import java.util.Arrays;

/**
 * @author: Sparks
 * @Date: 2021/7/20 18:26
 * @Description
 */
public class CallSiteDemo {

    private static void printArgs(Object...args){
        System.out.println(Arrays.deepToString(args));
    }

    private static MethodHandle printArgshandler;
    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        Class<?> thisClass = lookup.lookupClass();
        try {
            printArgshandler = lookup.findStatic(thisClass,"printArgs", MethodType.methodType(void.class,Object[].class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static CallSite bootstrapDynamic(Class caller, String name, MethodType type){
        return new ConstantCallSite(printArgshandler);
    }

    public static void main(String[] args) throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        Class<?> aClass = lookup.lookupClass();
        CallSite main = bootstrapDynamic(aClass, "main", null);
        main.dynamicInvoker().invoke(1,2,3);
    }
}
