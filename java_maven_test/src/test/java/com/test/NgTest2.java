package com.test;

import org.testng.annotations.Test;

/**
 * @author: wk
 * @Date: 2020/12/11 15:29
 * @Description
 */
public class NgTest2 {
    @Test()
    public void aFastTest(){
        System.out.println("NgTest2 Fast test");
    }

    @Test()
    public void aSlowTest(){
        System.out.println("NgTest2 slow test");
    }
}
