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

    public static void main(String[] args) {

    }


}
