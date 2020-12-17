package com.test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author: wk
 * @Date: 2020/12/11 15:25
 * @Description
 */
public class NgTest {

    @BeforeClass
    public void setUp(){
        System.out.println("setUp before class");
    }
    @AfterClass
    public void tearDown(){
        System.out.println("tearDown after class");
    }

    @Test(groups = {"fast"})
    public void aFastTest(){
        System.out.println("Fast test");
    }

    @Test(groups = {"slow"})
    public void aSlowTest(){
        System.out.println("slow test");
    }

    @Test
    public void testMaxInt(){
        System.out.printf("max int %d",Integer.MAX_VALUE);
    }
}
