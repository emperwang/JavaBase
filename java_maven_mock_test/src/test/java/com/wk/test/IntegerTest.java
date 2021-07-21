package com.wk.test;

import org.junit.Test;

/**
 * @author: Sparks
 * @Date: 2021/7/20 9:06
 * @Description
 */
public class IntegerTest {

    @Test
    public void test1(){
        Integer t1 = 15;
        Integer t2 = Integer.valueOf(15);
        System.out.println(t1 == t2);
        System.out.println("---------------------------------------");
        Integer t3 = new Integer(15);
        System.out.println(t1 == t3);
        System.out.println(t2 == t3);
        System.out.println("---------------------------------------");
        int t4 = 15;
        System.out.println(t1 == t4);
        System.out.println(t2 == t4);
        System.out.println(t3 == t4);
    }
}
