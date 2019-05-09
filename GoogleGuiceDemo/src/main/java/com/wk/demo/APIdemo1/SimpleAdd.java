package com.wk.demo.APIdemo1;

/**
 * 接口实现类
 */
public class SimpleAdd implements AddFunc {
    @Override
    public int add(int a, int b) {
        return a+b;
    }
}
