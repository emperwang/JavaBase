package com.wk.callStack;

import org.junit.Test;

public class GetCallStack {

    @Test
    public void test1(){
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            System.out.println("classname = "+element.getClassName());
            System.out.println("methodname = " + element.getMethodName());
        }
    }
}
