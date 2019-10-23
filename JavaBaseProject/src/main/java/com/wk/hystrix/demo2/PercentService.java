package com.wk.hystrix.demo2;

public class PercentService {

    public String ex(int seed){
        if (seed < 10){
            throw new RuntimeException("测试提高异常率");
        }
        return seed +" return?";
    }
}
