package com.wk.proxy.staticProxy;

import com.wk.proxy.API.MathCalc;

public class staticProxy implements MathCalc {
    @Override
    public int add(Integer a, Integer b) {
        long start = System.currentTimeMillis();
        int result = a + b;
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        return result;
    }

    @Override
    public int sub(Integer a, Integer b) {
        long start = System.currentTimeMillis();
        int result = a - b;
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        return result;
    }

    @Override
    public int mut(Integer a, Integer b) {
        long start = System.currentTimeMillis();
        int result = a * b;
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        return result;
    }

    @Override
    public int div(Integer a, Integer b) {
        long start = System.currentTimeMillis();
        int result = a / b;
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        return result;
    }
}
