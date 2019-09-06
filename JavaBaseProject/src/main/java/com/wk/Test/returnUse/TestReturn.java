package com.wk.Test.returnUse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestReturn {

    public static int test1(){
        try{
            throw new Exception("something wrong");
        } catch (Exception e) {
            log.error(e.getMessage());
            return 2;
        }finally {
            return 3;
        }
    }
    public static int test2(){
        try{
            throw new Exception("something wrong");
        } catch (Exception e) {
            log.error(e.getMessage());
            return 2;
        }
    }
    public static int test3(){
        int a = 0;
        try{
            a++;
            return a;
        } finally {
            ++a;
            log.info("a = "+a);
        }
    }

    public static void main(String[] args) {
        int i = test1();
        log.info("i is :"+i);

        int i1 = test2();
        log.info("res is:"+i1);

        int i2 = test3();
        log.info("res is:"+i2);
    }
}
