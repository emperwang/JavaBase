package com.wk.proxy.API;

public class MathImpl implements MathCalc {

    @Override
    public int add(Integer a, Integer b) {
        int result = a + b;
        return result;
    }

    @Override
    public int sub(Integer a, Integer b) {
        int result = a - b;
        return result;
    }

    @Override
    public int mut(Integer a, Integer b) {
        int result = a * b;
        return result;
    }

    @Override
    public int div(Integer a, Integer b) {
        int result = a / b;
        return result;
    }
}
