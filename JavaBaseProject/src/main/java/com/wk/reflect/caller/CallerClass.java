package com.wk.reflect.caller;

import sun.reflect.CallerSensitive;

/**
 * @author: wk
 * @Date: 2020/11/23 14:09
 * @Description
 */
public class CallerClass {

    public static void main(String[] args) {
        final Invoked invoked = new Invoked();
        invoked.print();
        final String property = System.getProperty("package.access");
        System.out.println(property);
    }
}
